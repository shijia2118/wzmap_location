import 'dart:io';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';
import 'package:wzmap_location/service/wayz_service.dart';
import 'package:wzmap_location/wayz_models.dart';
import 'package:wzmap_location/wayz_poi_models.dart';

import 'location.dart';
export 'src/wz_map_view.dart';
export 'src/wz_map_controller.dart';
export 'latlng.dart';

class WzmapLocation {
  static const MethodChannel _channel = MethodChannel('wzmap_location');

  static Future<void> startLocation({
    @required Function(Location) onLocation,
    @required Function(String) onError,
  }) async {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case "onLocationChanged":
          if (onLocation != null) {
            final Map<String, dynamic> map =
            Map<String, dynamic>.from(call.arguments);

            // 构造 Location 对象
            final location = Location(
              address: map['address'] ?? '',
              latitude: map['latitude'] ?? 0.0,
              longitude: map['longitude'] ?? 0.0,
              altitude: map['altitude'] ?? 0.0,
              bearing: map['bearing'] ?? 0.0,
              country: map['country'] ?? '',
              province: map['province'] ?? '',
              city: map['city'] ?? '',
              cityCode: map['cityCode'] ?? '',
              adCode: map['adCode'] ?? '',
              district: map['district'] ?? '',
              street: map['street'] ?? '',
              speed: map['speed']?? 0.0,
              placeName: map['placeName']?? '',
            );

            onLocation(location);

            stopLocation();

          }
          break;

        case "onLocationError":
          if (onError != null) {
            final Map<String, dynamic> map =
            Map<String, dynamic>.from(call.arguments);
            onError(map['message']);
          }
          break;
      }
    });

    await _channel.invokeMethod("startLocation");
  }


  static Future<void> stopLocation() async {
    await _channel.invokeMethod("stopLocation");
  }

  static Future<void> addMarker({
    @required double latitude,
    @required double longitude,
    String title,
  }) async {
    await _channel.invokeMethod("addMarker", {
      "lat": latitude,
      "lon": longitude,
      "title": title ?? "Marker",
    });
  }

  static Future<void> clearMarkers() async {
    await _channel.invokeMethod("clearMarkers");
  }

  static Future<void> moveCamera({
    @required double latitude,
    @required double longitude,
  }) async {
    await _channel.invokeMethod("moveCamera", {
      "lat": latitude,
      "lon": longitude,
    });
  }

  /// 调用 WAYZ 逆地理编码
  static Future<WayzResponse> reverseGeocode({
    @required double latitude,
    @required double longitude,
    @required String uuid,
  }) async {
    return await WayzService.reverseGeocode(
      latitude: latitude,
      longitude: longitude,
      uuid: uuid,
    );
  }

  static Future<List<WayzPoi>> searchPoi({
    @required String keyword,
    String city,
  }) async {
    return await WayzService.searchPoi(
      keyword: keyword,
      city: city,
    );
  }

  /// 周边搜索
  static Future<List<WayzPoi>> nearbySearch({
    @required double latitude,
    @required double longitude,
    String keywords,
  }) async {
    return await WayzService.nearbySearch(
      latitude: latitude,
      longitude: longitude,
      keywords: keywords,
    );
  }
}
