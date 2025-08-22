import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';
import 'package:wzmap_location/latlng.dart';
import 'package:wzmap_location/src/wz_map_controller.dart';

class WzMapView extends StatefulWidget {
  final int mapType; // 0=默认,1=灰白,2=浅蓝,3=深蓝
  final void Function(WzMapController controller) onMapCreated;
  final void Function(LatLng latLng) onMapClicked;

  WzMapView({
    this.mapType = 0,
    this.onMapCreated,
    this.onMapClicked,
    Key key, // 增加 key，方便外部使用 GlobalKey
  }) : super(key: key);

  @override
  WzMapViewState createState() => WzMapViewState();
}

class WzMapViewState extends State<WzMapView> {
  MethodChannel _channel;

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'wzmap_view',
        creationParams: {
          "mapType": widget.mapType,
        },
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    }
    return Text("地图仅支持 Android");
  }

  void _onPlatformViewCreated(int id) {
    _channel = MethodChannel('wzmap_view_$id');

    _channel.setMethodCallHandler((call) async {
      if (call.method == "onMapCreated") {
        final controller = WzMapController.fromChannel(_channel);
        widget.onMapCreated?.call(controller);
      } else  if (call.method == "onMapClick") {
        final lat = call.arguments["lat"];
        final lon = call.arguments["lon"];

        final latLng = LatLng(lat, lon);
        widget.onMapClicked(latLng);
      }
    });
  }

  /// 添加标记点
  Future<void> addMarker({
    @required double latitude,
    @required double longitude,
    String title,
  }) async {
    if (_channel == null) return;
    await _channel.invokeMethod("addMarker", {
      "lat": latitude,
      "lon": longitude,
      "title": title ?? "Marker",
    });
  }

  /// 添加折线
  Future<void> addPolyline(@required List<Map<String, double>> points) async {
    if (_channel == null) return;
    await _channel.invokeMethod("addPolyline", {
      "points": points,
    });
  }

  /// 设置地图类型
  Future<void> setMapType(@required int type) async {
    if (_channel == null) return;
    await _channel.invokeMethod("setMapType", {"type": type});
  }
}
