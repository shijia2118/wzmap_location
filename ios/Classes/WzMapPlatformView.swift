import Foundation
import UIKit
import MapKit
import Flutter

class WzMapPlatformView: NSObject, FlutterPlatformView, MKMapViewDelegate {
    
    private var mapView: MKMapView
    private var channel: FlutterMethodChannel
    private var markers: [MKPointAnnotation] = []
    
    init(frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, messenger: FlutterBinaryMessenger) {
        mapView = MKMapView(frame: frame)
        channel = FlutterMethodChannel(name: "wzmap_view_\(viewId)", binaryMessenger: messenger)
        
        super.init()
        
        mapView.delegate = self
        mapView.showsCompass = true
        mapView.showsScale = true
        mapView.showsTraffic = true
        mapView.showsBuildings = true
        
        channel.setMethodCallHandler(handle)
        
        // 点击手势
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(mapTapped(_:)))
        mapView.addGestureRecognizer(tapGesture)
        
        // 调用 onMapCreated
        channel.invokeMethod("onMapCreated", arguments: nil)
    }
    
    func view() -> UIView {
        return mapView
    }
    
    // MARK: - Flutter 方法调用
    private func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "setCenterCoordinate":
            guard let args = call.arguments as? [String: Any],
                  let lat = args["lat"] as? CLLocationDegrees,
                  let lon = args["lon"] as? CLLocationDegrees else {
                result(FlutterError(code: "INVALID_ARGS", message: "Missing lat/lon", details: nil))
                return
            }
            let center = CLLocationCoordinate2D(latitude: lat, longitude: lon)
            let region = MKCoordinateRegion(center: center, latitudinalMeters: 500, longitudinalMeters: 500)
            mapView.setRegion(region, animated: true)
            result(nil)
            
        case "addMarker":
            guard let args = call.arguments as? [String: Any],
                  let lat = args["lat"] as? CLLocationDegrees,
                  let lon = args["lon"] as? CLLocationDegrees,
                  let title = args["title"] as? String else {
                result(FlutterError(code: "INVALID_ARGS", message: "Missing lat/lon/title", details: nil))
                return
            }
            let annotation = MKPointAnnotation()
            annotation.coordinate = CLLocationCoordinate2D(latitude: lat, longitude: lon)
            annotation.title = title
            mapView.addAnnotation(annotation)
            markers.append(annotation)
            result(title) // 返回 title 作为 id
            
        case "clearMarkers":
            mapView.removeAnnotations(markers)
            markers.removeAll()
            result(nil)
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    // MARK: - 点击手势
    @objc private func mapTapped(_ gesture: UITapGestureRecognizer) {
        let point = gesture.location(in: mapView)
        let coordinate = mapView.convert(point, toCoordinateFrom: mapView)
        channel.invokeMethod("onMapClick", arguments: ["lat": coordinate.latitude, "lon": coordinate.longitude])
    }
}
