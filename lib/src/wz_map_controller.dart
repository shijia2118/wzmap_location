import 'package:flutter/services.dart';

import '../latlng.dart';

class WzMapController {
  final MethodChannel _channel;

  WzMapController(int id) : _channel = MethodChannel('wzmap_view_$id');

  WzMapController.fromChannel(this._channel);

  /// 设置地图中心点
  Future<void> setCenterCoordinate(LatLng latLng) async {
    await _channel.invokeMethod('setCenterCoordinate', {
      'lat': latLng.latitude,
      'lon': latLng.longitude,
    });
  }

  /// 添加 Marker
  Future<String> addMarker(LatLng latLng, {String title}) async {
    return await _channel.invokeMethod('addMarker', {
      'lat': latLng.latitude,
      'lon': latLng.longitude,
      'title': title ?? 'Marker',
    });
  }

  /// 清除所有 Marker
  Future<void> clearMarkers() async {
    await _channel.invokeMethod('clearMarkers');
  }
}
