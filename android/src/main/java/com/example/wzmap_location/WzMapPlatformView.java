package com.example.wzmap_location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.wzmap_location.wzMap.Map;
import com.example.wzmap_location.wzMap.common.MapType;
import com.example.wzmap_location.wzMap.model.Circle;
import com.example.wzmap_location.wzMap.model.CircleOptions;
import com.example.wzmap_location.wzMap.model.LatLng;
import com.example.wzmap_location.wzMap.model.Marker;
import com.example.wzmap_location.wzMap.model.MarkerOptions;
import com.example.wzmap_location.wzMap.model.Polygon;
import com.example.wzmap_location.wzMap.model.PolygonOptions;
import com.example.wzmap_location.wzMap.model.Polyline;
import com.example.wzmap_location.wzMap.model.PolylineOptions;
import com.example.wzmap_location.wzMap.util.WzManager;
import com.example.wzmap_location.wzMap.view.WZMap;
import com.wayz.location.MapsInitializer;

import java.util.HashMap;
import java.util.List;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class WzMapPlatformView implements PlatformView, MethodChannel.MethodCallHandler {

    private final WZMap wzMap;
    private final MethodChannel channel;
    private Marker lastMarker;
    private Polyline lastPolyline;
    private Polygon lastPolygon;
    private Circle lastCircle;

    public WzMapPlatformView(Context context, BinaryMessenger messenger, int viewId, final java.util.Map<String, Object> args) {
        WzManager.getInstance(context);
        MapsInitializer.updatePrivacyShow(context, true, false);
        MapsInitializer.updatePrivacyAgree(context, true);

        wzMap = new WZMap(context);
        wzMap.onCreate(null);

        wzMap.setMapReadyListener(new Map.MapReadyListener() {
            @Override
            public void onMapReady() {
                double lat = 23.167132;
                double lon = 113.456323;
                if (args != null) {
                    Object latObj = args.get("lat");
                    Object lonObj = args.get("lon");
                    if (latObj instanceof Number && lonObj instanceof Number) {
                        lat = ((Number) latObj).doubleValue();
                        lon = ((Number) lonObj).doubleValue();
                    }
                }
                wzMap.showLocationView(lon, lat, 10);

                channel.invokeMethod("onMapCreated", null);
            }
        });


        channel = new MethodChannel(messenger, "wzmap_view_" + viewId);
        channel.setMethodCallHandler(this);

        // 长按事件回调
        wzMap.addOnMapLongClickListener(new Map.MapLongClickListener() {
            @Override
            public void OnMapLongClickListener(@NonNull LatLng latLng) {
                java.util.Map<String, Object> args = new HashMap<>();
                args.put("lat", latLng.lat);
                args.put("lon", latLng.lon);
                channel.invokeMethod("onMapLongClick", args);
            }
        });

        // Marker 拖拽事件
        wzMap.setOnMarkerDragListener(new Map.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                java.util.Map<String, Object> args = new HashMap<>();
                args.put("id", marker.getId());
                channel.invokeMethod("onMarkerDragStart", args);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                java.util.Map<String, Object> args = new HashMap<>();
                args.put("id", marker.getId());
                args.put("lat", marker.getPosition().lat);
                args.put("lon", marker.getPosition().lon);
                channel.invokeMethod("onMarkerDrag", args);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                java.util.Map<String, Object> args = new HashMap<>();
                args.put("id", marker.getId());
                channel.invokeMethod("onMarkerDragEnd", args);
            }
        });

        // Marker 点击事件
        wzMap.setOnMarkerClickListener(new Map.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.isInfoWindowShown()) marker.showInfoWindow();
                else marker.hideInfoWindow();
                return false;
            }
        });

        wzMap.addOnMapLongClickListener(new Map.MapLongClickListener() {
            @Override
            public void OnMapLongClickListener(@NonNull LatLng latLng) {
                java.util.Map<String, Object> args = new HashMap<>();
                args.put("lat", latLng.lat);
                args.put("lon", latLng.lon);
                channel.invokeMethod("onMapClick", args);
            }
        });

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if ("addMarker".equals(call.method)) {
            double lat = call.argument("lat");
            double lon = call.argument("lon");
            String title = call.argument("title");

            Bitmap raw = BitmapFactory.decodeResource(
                    wzMap.getContext().getResources(),
                    R.mipmap.location
            );

            Bitmap scaled = Bitmap.createScaledBitmap(raw, 550, 550, false);


            lastMarker = wzMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(title != null ? title : "Marker")
                    .icon(scaled));

            result.success(lastMarker.getId());
        } else if ("removeMarker".equals(call.method)) {
            int markerId = call.argument("id");
            if (lastMarker != null && lastMarker.getId().equals(String.valueOf(markerId))) lastMarker.remove();
            result.success(null);
        } else if ("addPolyline".equals(call.method)) {
            List<java.util.Map<String, Double>> points = call.argument("points");
            if (points != null && points.size() > 1) {
                PolylineOptions options = new PolylineOptions();
                for (java.util.Map<String, Double> p : points) {
                    options.add(new LatLng(p.get("lat"), p.get("lon")));
                }
                lastPolyline = wzMap.addPolyline(options);
            }
            result.success(null);
        } else if ("removePolyline".equals(call.method)) {
            if (lastPolyline != null) lastPolyline.remove();
            result.success(null);
        } else if ("addPolygon".equals(call.method)) {
            List<java.util.Map<String, Double>> polygonPoints = call.argument("points");
            if (polygonPoints != null && polygonPoints.size() > 2) {
                PolygonOptions options = new PolygonOptions();
                for (java.util.Map<String, Double> p : polygonPoints) {
                    options.add(new LatLng(p.get("lat"), p.get("lon")));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    options.strokeWidth(4).strokeColor(0xff6200ee).fillColor(0xff03dac5);
                }
                lastPolygon = wzMap.addPolygon(options);
            }
            result.success(null);
        } else if ("removePolygon".equals(call.method)) {
            if (lastPolygon != null) lastPolygon.remove();
            result.success(null);
        } else if ("addCircle".equals(call.method)) {
            double centerLat = call.argument("lat");
            double centerLon = call.argument("lon");

            Object radiusObj = call.argument("radius");
            float radius = 0f;
            if (radiusObj instanceof Number) {
                radius = ((Number) radiusObj).floatValue();
            }

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(centerLat, centerLon))
                    .radius(radius)
                    .strokeWidth(5)
                    .fillColor(0x5503dac5)
                    .strokeColor(0xff03dac5)
                    .setDottedLine(true);
            lastCircle = wzMap.addCircle(circleOptions);
            result.success(null);
        } else if ("removeCircle".equals(call.method)) {
            if (lastCircle != null) lastCircle.remove();
            result.success(null);
        } else if ("setMapType".equals(call.method)) {
            int type = call.argument("type");
            if (type == 0) wzMap.setMapType(MapType.DEFAULT);
            else if (type == 1) wzMap.setMapType(MapType.GRAY_WHITE);
            else if (type == 2) wzMap.setMapType(MapType.LIGHT_BLUE);
            else if (type == 3) wzMap.setMapType(MapType.DARK_BLUE);
            result.success(null);
        } else if ("setCenterCoordinate".equals(call.method)) {
            double lat = call.argument("lat");
            double lon = call.argument("lon");
            wzMap.showLocationView(lon, lat, 16); // 设置中心点并调整缩放等级
            result.success(null);

            // 👉 新增：清除所有 Marker
        } else if ("clearMarkers".equals(call.method)) {
            if (lastMarker != null) {
                lastMarker.remove(); // 移除最后一个 Marker
                lastMarker = null;
            }

            result.success(null);

        } else {
            result.notImplemented();
        }
    }

    @NonNull
    @Override
    public View getView() {
        return wzMap;
    }

    @Override
    public void dispose() {
        wzMap.onDestroy();
    }
}
