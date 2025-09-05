import Flutter
import UIKit
import CoreLocation

public class SwiftWzmapLocationPlugin: NSObject, FlutterPlugin, CLLocationManagerDelegate {
    
    var locationManager: CLLocationManager?
    var channel: FlutterMethodChannel?
    
    override init() {
        super.init()
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.desiredAccuracy = kCLLocationAccuracyBest
        locationManager?.distanceFilter = kCLDistanceFilterNone
    }
    
    // Flutter 插件注册
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "wzmap_location", binaryMessenger: registrar.messenger())
        let instance = SwiftWzmapLocationPlugin()
        instance.channel = channel
        registrar.addMethodCallDelegate(instance, channel: channel)
        
        // ✅ 保留 PlatformView 注册，后续可扩展地图
        let factory = WzMapViewFactory(messenger: registrar.messenger())
        registrar.register(factory, withId: "wzmap_view")
    }

    // Flutter 调用入口
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "startLocation":
            locationManager?.requestWhenInUseAuthorization()
            locationManager?.requestLocation() // ✅ 单次定位
            result("定位开始")
            
        case "stopLocation":
            // 单次定位其实不需要 stop，这里只是保留接口
            locationManager?.stopUpdatingLocation()
            result("定位停止")
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    // MARK: - CLLocationManagerDelegate
    public func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        
        // 原始 WGS-84 坐标
        let wgsLat = location.coordinate.latitude
        let wgsLon = location.coordinate.longitude
        
        // ✅ 转换成 GCJ-02（高德/腾讯地图用的火星坐标系）
        let gcjCoord = CoordinateConverter.wgs2gcj(wgsLat: wgsLat, wgsLon: wgsLon)
        
        let latitude = gcjCoord.latitude
        let longitude = gcjCoord.longitude
        let altitude = location.altitude
        let speed = location.speed >= 0 ? location.speed : 0.0
        let bearing = location.course >= 0 ? location.course : 0.0
        
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { placemarks, error in
            if let placemark = placemarks?.first {
                let province = placemark.administrativeArea ?? ""
                let city = placemark.locality ?? ""
                let district = placemark.subLocality ?? ""
                let street = placemark.thoroughfare ?? ""
                let number = placemark.subThoroughfare ?? ""  // 门牌号
                
                let address = [province, city, district, street + number]
                    .filter { !$0.isEmpty }
                    .joined()
                
                var locationData: [String: Any] = [
                    "latitude": latitude,   // ✅ 用 GCJ-02
                    "longitude": longitude, // ✅ 用 GCJ-02
                    "altitude": altitude,
                    "speed": speed,
                    "address": address,
                    "placeName": placemark.name ?? "",
                    "province": province,
                    "city": city,
                    "district": district,
                    "street": street,
                    "cityCode": "",
                    "adCode": ""
                ]
                
                self.channel?.invokeMethod("onLocationChanged", arguments: locationData)
            } else if let error = error {
                self.channel?.invokeMethod("onLocationError", arguments: ["message": error.localizedDescription])
            }
        }
    }


    
    public func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        channel?.invokeMethod("onLocationError", arguments: ["message": error.localizedDescription])
    }
    
    // MARK: - 占位方法（保持接口不报错）
    public func onReceivedGeocode(result: [Any]) {
        channel?.invokeMethod("onGeocodeResult", arguments: [])
    }
    
    public func onReceivedPoiSearch(result: [Any]) {
        channel?.invokeMethod("onPoiSearchResult", arguments: [])
    }
}
