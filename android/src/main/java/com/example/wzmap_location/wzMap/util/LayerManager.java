package com.example.wzmap_location.wzMap.util;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;

import android.content.Context;
import android.text.TextUtils;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.example.wzmap_location.wzMap.IMapLayer;
import com.example.wzmap_location.wzMap.model.BaseOptions;
import com.example.wzmap_location.wzMap.model.Circle;
import com.example.wzmap_location.wzMap.model.CircleOptions;
import com.example.wzmap_location.wzMap.model.LatLng;
import com.example.wzmap_location.wzMap.model.Polygon;
import com.example.wzmap_location.wzMap.model.PolygonOptions;
import com.example.wzmap_location.wzMap.model.Polyline;
import com.example.wzmap_location.wzMap.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LayerManager implements IMapLayer {

    private MapboxMap mapboxMap;
    private Style mStyle;
    private Context context;
    private LineLayer mLineLayer = null;


    private static final String POLYGON_LINE_ID = "POLYGON_LINE_ID";

    public LayerManager(Context context, MapboxMap mapboxMap) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.mStyle = mapboxMap.getStyle();
    }

    public void initMapBox(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mStyle = mapboxMap.getStyle();
    }

    public Polyline addPolyline(PolylineOptions polylineOptions) {
        List<LatLng> latLngList = polylineOptions.getPoints();
        if (latLngList == null) {
            return null;
        }
        String layerId = UUID.randomUUID().toString();
        List<Point> pointList = transToPoints(latLngList);
        Feature feature = Feature.fromGeometry(LineString.fromLngLats(pointList));
        Layer layer = updatePolyLineLayer(layerId, feature, polylineOptions);
        if(layer == null){
            return null;
        }
        Polyline polyline = new Polyline(this, polylineOptions, layerId);
        return polyline;
    }

    private final Layer updatePolyLineLayer(String layerId, Feature feature, PolylineOptions polylineOptions) {
        int lineColorHex = polylineOptions.getColor();
        float width = polylineOptions.getWidth();
        boolean isDotted = polylineOptions.isDottedLine();
        float zIndex = polylineOptions.getzIndex();
        Layer curLayer = mStyle.getLayer(layerId);
        boolean isVisible = polylineOptions.isVisible();
        if (curLayer == null || mStyle.getSource(layerId) == null) {
            curLayer = new LineLayer(layerId, layerId).withProperties(
                    PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.7f),
                    PropertyFactory.lineWidth(width),
                    PropertyFactory.lineColor(ColorUtils.colorToRgbaString(lineColorHex)));
            if (isDotted) {
                curLayer.setProperties(
                        PropertyFactory.lineDasharray(new Float[]{1f, 2f})
                );
            }
            mStyle.addLayer(curLayer);
            GeoJsonSource geoJsonSource = new GeoJsonSource(layerId, feature);
            mStyle.addSource(geoJsonSource);
        } else {
            curLayer.setProperties(
                    PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.7f),
                    PropertyFactory.lineWidth(width),
                    PropertyFactory.lineColor(ColorUtils.colorToRgbaString(lineColorHex)));
            if (isDotted) {
                curLayer.setProperties(
                        PropertyFactory.lineDasharray(new Float[]{1f, 2f})
                );
            }
            curLayer.setProperties(isVisible ? PropertyFactory.visibility(Property.VISIBLE)
                    : PropertyFactory.visibility(Property.NONE));
        }
        return curLayer;
    }

    public Polygon addPolygon(PolygonOptions polygonOptions) {
        List<LatLng> latLngList = polygonOptions.getPoints();
        if (latLngList == null) {
            return null;
        }
        String layerId = UUID.randomUUID().toString();
        List<Point> pointList = transToPoints(latLngList);
        List<Point> afterList = PolylineUtils.simplify(pointList, 0.001);
        List<List<Point>> pointLists = new ArrayList<>();
        pointLists.add(afterList);
        Feature feature = Feature.fromGeometry(com.mapbox.geojson.Polygon.fromLngLats(pointLists));
        updatePolygonLayer(layerId, feature, polygonOptions);
        Polygon circle = new Polygon(this, polygonOptions, layerId);
        return circle;
    }

    private final void updatePolygonLayer(String layerId, Feature feature, PolygonOptions polygonOptions) {
        int fillColor = polygonOptions.getFillColor();
        int strokeColor = polygonOptions.getStrokeColor();
        float strokeWidth = polygonOptions.getStrokeWidth();
        boolean isDotted = polygonOptions.isDottedLine();
        boolean isVisible = polygonOptions.isVisible();
        Layer curLayer = mStyle.getLayer(layerId);
        Layer polyLineLayer = mStyle.getLayer(POLYGON_LINE_ID + layerId);
        if (curLayer == null || polyLineLayer == null || mStyle.getSource(layerId) == null) {
            curLayer = new FillLayer(layerId, layerId);
            curLayer.setProperties(
                    PropertyFactory.fillColor(ColorUtils.colorToRgbaString(fillColor)),
                    PropertyFactory.fillAntialias(true),
                    PropertyFactory.fillOpacity(1f));
            LineLayer lineLayer = new LineLayer(POLYGON_LINE_ID + layerId, layerId);
            lineLayer.setProperties(PropertyFactory.lineWidth(strokeWidth),
                    PropertyFactory.lineColor(ColorUtils.colorToRgbaString(strokeColor)));
            if (isDotted) {
                lineLayer.setProperties(
                        PropertyFactory.lineDasharray(new Float[]{1f, 2f})
                );
            }
            mStyle.addLayer(curLayer);
            mStyle.addLayerAbove(lineLayer, layerId);
            Source source = new GeoJsonSource(layerId, feature);
            mStyle.addSource(source);
        } else {
            curLayer.setProperties(
                    PropertyFactory.fillColor(ColorUtils.colorToRgbaString(fillColor)),
                    PropertyFactory.fillAntialias(true),
                    PropertyFactory.fillOpacity(1f),
                    isVisible ? PropertyFactory.visibility(Property.VISIBLE)
                            : PropertyFactory.visibility(Property.NONE));
            polyLineLayer.setProperties(PropertyFactory.lineWidth(strokeWidth),
                    PropertyFactory.lineColor(ColorUtils.colorToRgbaString(strokeColor)),
                    isVisible ? PropertyFactory.visibility(Property.VISIBLE)
                            : PropertyFactory.visibility(Property.NONE));
        }

    }

    public Circle addCircle(CircleOptions circleOptions) {
        LatLng centerLatLng = circleOptions.getCenter();
        if (centerLatLng == null) {
            return null;
        }
        String layerId = UUID.randomUUID().toString();
        Point point = Point.fromLngLat(centerLatLng.lon, centerLatLng.lat);
        Feature feature = Feature.fromGeometry(point);

        updateCircleLayer(layerId, feature, circleOptions);
        Circle circle = new Circle(this, circleOptions, layerId);
        return circle;
    }

    private final void updateCircleLayer(String layerId, Feature feature, CircleOptions circleOptions) {
        int fillColor = circleOptions.getFillColor();
        int strokeColor = circleOptions.getStrokeColor();
        float strokeWidth = circleOptions.getStrokeWidth();
        float radius = circleOptions.getRadius();
        boolean isVisible = circleOptions.isVisible();
        Layer curLayer = mStyle.getLayer(layerId);
        if (curLayer == null || mStyle.getSource(layerId) == null) {
            curLayer = new CircleLayer(layerId, layerId);
            curLayer.setMaxZoom(22);
            curLayer.setMinZoom(13);
            curLayer.setProperties(
                    PropertyFactory.circleOpacity(0.6f),
                    PropertyFactory.circleColor(ColorUtils.colorToRgbaString(fillColor)),
                    circleRadius(
                            CoorUtil.getCircle(radius)
                    ),
                    PropertyFactory.circleStrokeColor(ColorUtils.colorToRgbaString(strokeColor)),
                    PropertyFactory.circleStrokeWidth(strokeWidth)
            );
            mStyle.addLayer(curLayer);
            Source source = new GeoJsonSource(layerId, feature);
            mStyle.addSource(source);
        } else {
            curLayer.setProperties(PropertyFactory.circleOpacity(0.6f),
                    PropertyFactory.circleColor(ColorUtils.colorToRgbaString(fillColor)),
                    PropertyFactory.circleRadius(radius),
                    PropertyFactory.circleStrokeColor(ColorUtils.colorToRgbaString(strokeColor)),
                    PropertyFactory.circleStrokeWidth(strokeWidth));
            curLayer.setProperties(isVisible ? PropertyFactory.visibility(Property.VISIBLE)
                    : PropertyFactory.visibility(Property.NONE));
        }


    }

    @Override
    public void removeOverlay(String layerId) {
        if (TextUtils.isEmpty(layerId)) {
            return;
        }
        try {
            if (mStyle != null) {
                if (mStyle.getLayer(layerId) != null) {
                    mStyle.removeLayer(layerId);
                }
                if (mStyle.getLayer(POLYGON_LINE_ID + layerId) != null) {
                    mStyle.removeLayer(POLYGON_LINE_ID + layerId);
                }
                if (mStyle.getSource(layerId) != null) {
                    mStyle.removeSource(layerId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOption(String layerId, BaseOptions baseOptions) {
        if (TextUtils.isEmpty(layerId)) {
            return;
        }
        if (baseOptions instanceof CircleOptions) { // 圆形
            CircleOptions polylineOptions = (CircleOptions) baseOptions;
            updateCircleLayer(layerId, null, polylineOptions);
        } else if (baseOptions instanceof PolylineOptions) { // 线条
            PolylineOptions polylineOptions = (PolylineOptions) baseOptions;
            updatePolyLineLayer(layerId, null, polylineOptions);
        } else if (baseOptions instanceof PolygonOptions) { // 多边形
            PolygonOptions polygonOptions = (PolygonOptions) baseOptions;
            updatePolygonLayer(layerId, null, polygonOptions);
        }
    }

    @Override
    public void removeAll() {
        if(mStyle == null){
            return;
        }
        List<Source> sourceList = mStyle.getSources();
        for (Source source : sourceList) {
            mStyle.removeSource(source);
        }
        List<Layer> mAllLayers = mStyle.getLayers();
        for (Layer layer : mAllLayers) {
            mStyle.removeLayer(layer);
        }
        mStyle = null;
    }

    private List<Point> transToPoints(List<LatLng> latLngList) {
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : latLngList) {
            Point point = Point.fromLngLat(latLng.lon, latLng.lat);
            points.add(point);
        }
        return points;
    }
}
