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
        
        let latitude = location.coordinate.latitude
        let longitude = location.coordinate.longitude
        let altitude = location.altitude
        let speed = location.speed >= 0 ? location.speed : 0.0
        let bearing = location.course >= 0 ? location.course : 0.0
        
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { placemarks, error in
            if let placemark = placemarks?.first {
                // 直接取 name，就是完整的格式化地址
                let address = placemark.name ?? ""
                let placeName = placemark.name ?? ""  // 如果 placeName 就是显示名，可以复用

                var locationData: [String: Any] = [
                    "latitude": location.coordinate.latitude,
                    "longitude": location.coordinate.longitude,
                    "altitude": location.altitude,
                    "speed": location.speed,
                    "address": address,
                    "placeName": placeName,
                    "province": placemark.administrativeArea ?? "",
                    "city": placemark.locality ?? "",
                    "district": placemark.subLocality ?? "",
                    "street": placemark.thoroughfare ?? "",
                    "cityCode": "",   // iOS 没有 cityCode
                    "adCode": ""      // iOS 没有 adCode
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
