import Flutter
import UIKit
import wzLib

public class SwiftWzmapLocationPlugin: NSObject, FlutterPlugin,LocationDelegate {
    public func onReceivedLocation(wzLocation: wzLib.LocationRes) {
        
    }
    
    public func onLocationError(msg: String) {
        
    }
    
    public func onReceivedGeocode(result: [wzLib.GeocodeRes]) {
        
    }
    
    public func onReceivedPoiSearch(result: [wzLib.GeocodeRes]) {
        
    }
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "wzmap_location", binaryMessenger: registrar.messenger())
    let instance = SwiftWzmapLocationPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
