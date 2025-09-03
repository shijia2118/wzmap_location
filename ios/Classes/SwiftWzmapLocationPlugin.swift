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
            var address = ""
            var placeName = ""
            var country = ""
            var province = ""
            var city = ""
            var district = ""
            var street = ""
            
            if let placemark = placemarks?.first {
                placeName = placemark.name ?? ""
                country = placemark.country ?? ""
                province = placemark.administrativeArea ?? ""
                city = placemark.locality ?? ""
                district = placemark.subLocality ?? ""
                street = placemark.thoroughfare ?? ""
                
                // 拼接一个完整地址
                address = [country, province, city, district, street].filter { !$0.isEmpty }.joined(separator: " ")
            }
            
            let locationData: [String: Any] = [
                "latitude": latitude,
                "longitude": longitude,
                "altitude": altitude,
                "speed": speed,
                "bearing": bearing,
                "address": address,
                "placeName": placeName,
                "country": country,
                "province": province,
                "city": city,
                "cityCode": "",  // iOS 无法提供
                "adCode": "",    // iOS 无法提供
                "district": district,
                "street": street
            ]
            
            self.channel?.invokeMethod("onLocationChanged", arguments: locationData)
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
