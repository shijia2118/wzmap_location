package com.example.wzmap_location.wzMap.util;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.example.wzmap_location.wzMap.IMapMarkLayer;
import com.example.wzmap_location.R;
import com.example.wzmap_location.wzMap.common.BitmapUtil;
import com.example.wzmap_location.wzMap.common.SymbolGenerator;
import com.example.wzmap_location.wzMap.model.LatLng;
import com.example.wzmap_location.wzMap.model.Marker;
import com.example.wzmap_location.wzMap.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class MarkManager implements IMapMarkLayer {
    private MapboxMap mapboxMap;
    private Style mStyle;
    private Context context;
    private Layer mLayer = null;

    private List<Feature> featureList = new ArrayList();
    private FeatureCollection featureCollection = null;
    private GeoJsonSource mGeoSource = null;
    private HashMap imagesMap = new HashMap<String, Bitmap>();

    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String GEO_JSON_SOURCE_ID = "GEO_JSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID"; // windowInfo id
    private static final String PROPERTY_WINDOW_NAME = "PROPERTY_WINDOW_NAME";
    private static final String PROPERTY_WINDOW_CAPITAL = "property_window_capital";
    private static final String PROPERTY_SELECTED = "selected";
    private static final String TAG = "wzMap";


    public MarkManager(Context context, MapboxMap mapboxMap) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.mStyle = mapboxMap.getStyle();
        initData();
    }

    public void initData() {
        setupSource();
        setUpImage();
        setMarkerLayer();
        setUpInfoWindowLayer();
    }

    public void setMapboxMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mStyle = mapboxMap.getStyle();
    }

    /**
     * 将 GeoJson 源添加到地图
     */
    private void setupSource() {
        featureCollection = FeatureCollection.fromFeatures(featureList);
        mGeoSource = new GeoJsonSource(GEO_JSON_SOURCE_ID, featureCollection);
        mStyle.addSource(mGeoSource);
    }

    private void refreshSource() {
        featureCollection = FeatureCollection.fromFeatures(featureList); // 重新赋值featureCollection
        mGeoSource.setGeoJson(featureCollection);   // 更新source
    }

    /**
     * 将标记图像添加到地图中，以用作 SymbolLayer 图标
     */
    private void setUpImage() {
        mStyle.addImages(imagesMap);
    }

    /**
     * 设置带有 mark 图标的图层
     */
    private void setMarkerLayer() {
        mLayer = new SymbolLayer(MARKER_LAYER_ID, GEO_JSON_SOURCE_ID).withProperties(
                iconImage("{MARKER_IMAGE_ID}"),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -8f})
        );
        mStyle.addLayer(mLayer);
    }

    private void setUpInfoWindowLayer() {
        Layer windowLayer = new SymbolLayer(CALLOUT_LAYER_ID, GEO_JSON_SOURCE_ID)
                .withProperties(
                        /* show image with id title based on the value of the name feature property */
                        iconImage("{PROPERTY_WINDOW_NAME}"),
                        /* set anchor of icon to bottom-left */
                        iconAnchor(ICON_ANCHOR_BOTTOM),
                        /* all info window and marker image to appear at the same time*/
                        iconAllowOverlap(true),
                        /* offset the info window to be above the marker */
                        iconOffset(new Float[]{-2f, -28f})
                ).withFilter(eq((get(PROPERTY_SELECTED)), literal(true)));
        mStyle.addLayer(windowLayer);
    }


    private void setInfoWindow(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(context);
        BubbleLayout bubbleLayout = (BubbleLayout)
                inflater.inflate(R.layout.marker_view_bubble, null);
        MarkerOptions markerOptions = marker.getMarkOptions();
        String title = markerOptions.getTitle();
        String snippet = markerOptions.getSnippet();
        TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
        titleTextView.setText(title);
        TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
        descriptionTextView.setText(
                String.format(context.getString(R.string.capital), snippet));

        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        bubbleLayout.measure(measureSpec, measureSpec);

        float measuredWidth = bubbleLayout.getMeasuredWidth();

        bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

        Bitmap bitmap = SymbolGenerator.generate(bubbleLayout);
        imagesMap.put(title, bitmap);
    }

    public Marker addMark(MarkerOptions markerOptions) {
        String title = markerOptions.getTitle();
        LatLng position = markerOptions.getPosition();
        Point point = Point.fromLngLat(position.lon, position.lat);
        Feature feature = Feature.fromGeometry(point);
        String idName = UUID.randomUUID().toString();
        feature.addStringProperty(MARKER_IMAGE_ID, idName);
        feature.addStringProperty(PROPERTY_WINDOW_NAME, title);
        feature.addBooleanProperty(PROPERTY_SELECTED, false);
        featureList.add(feature);
        Bitmap markIcon = markerOptions.getBitmap();
        Marker curMarker = new Marker(this, markerOptions, idName);
        curMarker.setLatLng(position);
        imagesMap.put(idName, BitmapUtil.getScaleImg(markIcon, 100, 100));
        setInfoWindow(curMarker);
//        mStyle.addImages(imagesMap);
        refreshSource();
        return curMarker;
    }


    private void clearAllData() {
        imagesMap.clear();
        featureList.clear();
    }

    /**
     * 清除所有标记
     */
    public void removeAllLayers() {
        if (mStyle != null) {
            mGeoSource.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<Feature>()));
            mStyle.removeLayer(MARKER_LAYER_ID);
            mStyle.removeLayer(CALLOUT_LAYER_ID);
            mStyle.removeImage(MARKER_IMAGE_ID);
            mStyle.removeImage(PROPERTY_WINDOW_NAME);
            mStyle.removeSource(GEO_JSON_SOURCE_ID);
            clearAllData();
            mStyle = null;
        }

    }

    public Marker handleClickObjet(PointF screenPoint) {
        // 是否点击到了 Marker
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);
        if (!features.isEmpty()) {
            // 针对 marker 点击 infoWindow 改变状
            Feature feature = features.get(0);
            Geometry geometry = feature.geometry();
            String markId = feature.getStringProperty(MARKER_IMAGE_ID);
            Marker marker = new Marker(this, null, markId);
            if (geometry instanceof Point) {
                Point point = (Point) feature.geometry();
                marker.setLatLng(new LatLng(point.longitude(), point.latitude()));
                return marker;
            }
            return marker;
        }
        return null;
    }

    @Override
    public void showInfoWindow(String markerId) {
        if (markerId == null) {
            return;
        }
        setFeatureStatus(markerId, true);
    }

    @Override
    public void hideInfoWindow(String markerId) {
        if (markerId == null) {
            return;
        }
        setFeatureStatus(markerId, false);
    }

    @Override
    public boolean isInfoWindowShown(String markerId) {
        if (markerId == null) {
            return false;
        }
        Boolean isSel = false;
        for (Feature feature : featureList) {
            String propertyId = feature.getStringProperty(MARKER_IMAGE_ID);
            if (propertyId.equals(markerId)) { // 当前 Mark
                isSel = feature.getBooleanProperty(PROPERTY_SELECTED);
                break;
            }
        }
        return isSel;
    }

    @Override
    public void removeOverlay(String curMarkId) {
        if (curMarkId == null) {
            Log.i(TAG, "标注 ID 为空");
            return;
        }
        Iterator it = featureList.iterator();
        while (it.hasNext()) {
            Feature feature = (Feature) it.next();
            String pointId = feature.getStringProperty(MARKER_IMAGE_ID);
            if (curMarkId.equals(pointId)) {
                imagesMap.remove(pointId);
                it.remove();
                break;
            }
        }
        mStyle.removeImage(curMarkId);
        refreshSource();
    }

    private void setFeatureStatus(String markId, boolean isSel) {
        for (Feature feature : featureList) {
            String propertyId = feature.getStringProperty(MARKER_IMAGE_ID);
            Log.i(TAG, "propertyId : " + propertyId + "markId :" + markId);
            if (propertyId.equals(markId)) { // 当前 Mark
                feature.addBooleanProperty(PROPERTY_SELECTED, isSel);
                break;
            }
        }
        refreshSource();
    }

    public Marker getRectMark(RectF rect) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(rect, MARKER_LAYER_ID);
        if (features == null || features.size() == 0) {
            return null;
        }
        Feature feature = features.get(0);
        Geometry geometry = feature.geometry();
        if (geometry instanceof Point) {
            String markId = feature.getStringProperty(MARKER_IMAGE_ID);
            Marker marker = new Marker(this, null, markId);
            Point point = (Point) feature.geometry();
            marker.setLatLng(new LatLng(point.longitude(), point.latitude()));
            return marker;
        }
        return null;
    }

    public LatLng fromScreenPoint(PointF tapPoint) {
        if (mapboxMap != null) {
            Projection projection = mapboxMap.getProjection();
            com.mapbox.mapboxsdk.geometry.LatLng latLng = projection.fromScreenLocation(tapPoint);
            LatLng transLatLng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
            return transLatLng;
        }
        return null;
    }

    public void updateMark(Marker marker, LatLng newLat) {
        Iterator it = featureList.iterator();
        Feature updateFeature = null;
        while (it.hasNext()) {
            Feature feature = (Feature) it.next();
            String markId = marker.getId();
            String pointId = feature.getStringProperty(MARKER_IMAGE_ID);
            if (markId.equals(pointId)) {
                Point newPoint = Point.fromLngLat(newLat.lon, newLat.lat);
                ;
                updateFeature = Feature.fromGeometry(newPoint);
                marker.setLatLng(newLat);
                updateFeature.addStringProperty(MARKER_IMAGE_ID, feature.getStringProperty(MARKER_IMAGE_ID));
                updateFeature.addStringProperty(PROPERTY_WINDOW_NAME, feature.getStringProperty(PROPERTY_WINDOW_NAME));
                updateFeature.addBooleanProperty(PROPERTY_SELECTED, feature.getBooleanProperty(PROPERTY_SELECTED));
                it.remove();
                break;
            }
        }
        if (updateFeature != null) {
            featureList.add(updateFeature);
        }
        refreshSource();
    }
}