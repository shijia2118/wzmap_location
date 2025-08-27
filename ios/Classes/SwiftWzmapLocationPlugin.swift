import Flutter
import UIKit
import wzLib

public class SwiftWzmapLocationPlugin: NSObject, FlutterPlugin, LocationDelegate {
    
    var wzClientOption: WzClientLocation?
    var channel: FlutterMethodChannel?
    
    override init() {
        super.init()
        wzClientOption = WzClientLocation()
        wzClientOption?.setAgreePrivacy(isAgree: true)   // ✅ 改成实例方法调用
        wzClientOption?.initKey(accesskey: "hGYINWz43i65SaCRUzUgSBmClDD7l2C3")
        wzClientOption?.callDelegate = self
    
    }
    
    // Flutter 插件注册
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "wzmap_location", binaryMessenger: registrar.messenger())
        let instance = SwiftWzmapLocationPlugin()
        instance.channel = channel
        registrar.addMethodCallDelegate(instance, channel: channel)
        
        // 注册 PlatformView
        let factory = WzMapViewFactory(messenger: registrar.messenger())
        registrar.register(factory, withId: "wzmap_view")
    }

    // Flutter 调用入口
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "startLocation":
            wzClientOption?.isLocateOnce = true
            wzClientOption?.startLocation()
            result("定位开始")
            
        case "stopLocation":
            wzClientOption?.stopLocation()
            result("定位停止")
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    // MARK: - 定位回调
    public func onReceivedLocation(wzLocation: wzLib.LocationRes) {
        let detail = wzLocation.location
        
        var locationData: [String: Any] = [
            "id": wzLocation.id,
            "asset": wzLocation.asset,
            "timestamp": detail.timestamp,
            "latitude": detail.position.point.latitude,
            "longitude": detail.position.point.longitude
        ]
        
        // address
        if let addr = detail.address {
            locationData["address_name"] = addr.name
            locationData["address_context"] = addr.context.map { ctx in
                [
                    "type": ctx.type,
                    "name": ctx.name,
                    "code": ctx.code
                ]
            }
        }
        
        // place
        if let place = detail.place {
            locationData["place_name"] = place.name
            locationData["place_type"] = place.type
            locationData["place_distance"] = place.distance.line
        }
        
        channel?.invokeMethod("onLocationChanged", arguments: locationData)
    }
    
    public func onLocationError(msg: String) {
        channel?.invokeMethod("onLocationError", arguments: ["message": msg])
    }
    
    public func onReceivedGeocode(result: [wzLib.GeocodeRes]) {
        let data = result.map { geo in
            [
                "id": geo.id,
                "name": geo.name,
                "type": geo.type,
                "code": geo.code,
                "relevance": geo.relevance,
                "distance": geo.distance,
                "geoPoint": geo.geoPoint
            ] as [String: Any]
        }
        channel?.invokeMethod("onGeocodeResult", arguments: data)
    }
    
    public func onReceivedPoiSearch(result: [wzLib.GeocodeRes]) {
        let data = result.map { poi in
            [
                "id": poi.id,
                "name": poi.name,
                "type": poi.type,
                "code": poi.code,
                "relevance": poi.relevance,
                "distance": poi.distance,
                "geoPoint": poi.geoPoint
            ] as [String: Any]
        }
        channel?.invokeMethod("onPoiSearchResult", arguments: data)
    }
}
