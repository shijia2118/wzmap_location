package com.example.wzmap_location.wzMap.view;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wzmap_location.R;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.example.wzmap_location.wzMap.Map;
import com.example.wzmap_location.wzMap.common.DistanceUtil;
import com.example.wzmap_location.wzMap.common.MapType;
import com.example.wzmap_location.wzMap.model.Circle;
import com.example.wzmap_location.wzMap.model.CircleOptions;
import com.example.wzmap_location.wzMap.model.Polygon;
import com.example.wzmap_location.wzMap.model.PolygonOptions;
import com.example.wzmap_location.wzMap.model.Polyline;
import com.example.wzmap_location.wzMap.model.PolylineOptions;
import com.example.wzmap_location.wzMap.util.LayerManager;
import com.example.wzmap_location.wzMap.util.MarkManager;
import com.example.wzmap_location.wzMap.model.Marker;
import com.example.wzmap_location.wzMap.model.MarkerOptions;

public class WZMap extends BaseView implements Map {

    private static final String TAG = "WZMap";
    private MapReadyListener listener;
    private OnMarkerClickListener onMarkerClickListener;
    private OnMarkerDragListener onMarkerDragListener;
    private MapLongClickListener mapLongClickListener;
    private CameraMoveListener moveListener;
    private CameraMoveCancelListener moveCancleListener;
    private CameraMoveStartedListener moveStartListener;
    private OnMoveListener onMoveListener;
    private OnScaleListener onScaleListener;
    private OnFlingListener onFlingListener;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MarkManager mMarkManager;
    private LayerManager mLayerManager;
    private MapType curMapType = MapType.DEFAULT;
    private Context context;
    private static final String SOURCE_ID = "source-id";
    private static final String SYMBOL_LAYER_ID = "symbol-layer-id";
    private static final String BLUE_ICON_ID = "blue-icon-id";
    private static final double mBLon = 116.39763602352961;
    private static final double mBlat = 39.9216597222605;
    private static final long DRAG_TIME = 1000;
    private long lastClickTime;

    private Bitmap locBitmap = null;
    private Style mapStyle = null;
    private boolean mapStyleNotLoaded = false;

    public WZMap(@NonNull Context context) {
        super(context);
        this.context = context;
        initContext();
        initData();
    }

    public WZMap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initContext();
        initData();
    }

    private void initContext() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.map_view, null);
        super.addView(view);
        mapView = view.findViewById(R.id.mapView);
    }

    private void initData() {
        if (locBitmap == null) {
            locBitmap = getScaledBitmap(context, R.mipmap.location); // 30dp × 30dp
        }
    }

    private Bitmap getScaledBitmap(Context context, int resId) {
        // dp 转 px
        float density = context.getResources().getDisplayMetrics().density;
        int targetPxWidth = (int) (32 * density + 0.5f);
        int targetPxHeight = (int) (44 * density + 0.5f);

        // 先解码原始 Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);

        // 缩放到指定大小
        return Bitmap.createScaledBitmap(bitmap, targetPxWidth, targetPxHeight, true);
    }


    @Override
    public void onCreate(Bundle bundle) {
        mapView.onCreate(bundle);
    }

    @Override
    public void onStart() {
        mapView.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        if (mapStyle != null) {
            mapStyle.removeLayer(SYMBOL_LAYER_ID);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    @Override
    public void zoomIn() {
        if (mapboxMap != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.zoomIn(), 100);
        }
    }

    @Override
    public void zoomOut() {
        if (mapboxMap != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.zoomOut(), 100);
        }
    }

    @Override
    public void setMapType(MapType mapType) {
        curMapType = mapType;
        mapStyleNotLoaded = false;
        setStyle(curMapType);

    }

    @Override
    public void showLocationView(double lon, double lat, double accuracy) {
        if (mapStyleNotLoaded) {
            if (mapboxMap == null) {
                return;
            }
            if (lon == 0.0 || lat == 0.0) {
                return;
            }
            Style style = mapboxMap.getStyle();
            if (style == null) {
                return;
            }
            removeLayer();
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0), 100);

            GeoJsonSource geoJsonSource = new GeoJsonSource(SOURCE_ID, Feature.fromGeometry(Point.fromLngLat(lon, lat)));
            style.addSource(geoJsonSource);
            SymbolLayer symbolLayer = new SymbolLayer(SYMBOL_LAYER_ID, SOURCE_ID);
            symbolLayer.withProperties(
                    iconImage(BLUE_ICON_ID),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true),
                    // 如果为true，则即使该图标与其他先前绘制的符号发生冲突也将是可见的。（机翻，大概就这意思）
                    iconOffset(new Float[]{0f, -10f}));
            style.addImage(BLUE_ICON_ID, locBitmap);
            style.addLayer(symbolLayer);
        }
    }

    @Override
    public void setMapReadyListener(MapReadyListener listener) {
        this.listener = listener;
        if (mapView != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    initReadyMapBox(mapboxMap);
                }
            });
        }
    }

    @Override
    public void addOnMapLongClickListener(MapLongClickListener mapLongClickListener) {
        this.mapLongClickListener = mapLongClickListener;
    }


    @Override
    public void addOnCameraMoveListener(CameraMoveListener cameraMoveListener) {
        this.moveListener = cameraMoveListener;
    }

    @Override
    public void addOnCameraMoveCancelListener(Map.CameraMoveCancelListener cameraMoveCancelListener) {
        this.moveCancleListener = cameraMoveCancelListener;
    }

    @Override
    public void addOnCameraMoveStartedListener(Map.CameraMoveStartedListener cameraMoveStartedListener) {
        this.moveStartListener = cameraMoveStartedListener;
    }

    @Override
    public void onAddMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    @Override
    public void onAddScaleListener(OnScaleListener scaleListener) {
        this.onScaleListener = scaleListener;
    }

    @Override
    public void onAddFlingListener(OnFlingListener flingListener) {
        this.onFlingListener = flingListener;
    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    @Override
    public void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.onMarkerDragListener = onMarkerDragListener;
    }

    private void initReadyMapBox(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        UiSettings uiSettings = mapboxMap.getUiSettings();
        uiSettings.setLogoEnabled(false);
        uiSettings.setAttributionEnabled(false);
        uiSettings.setCompassEnabled(false);
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mBlat, mBLon), 12.0), 100);
        mapboxMap.setStyle(MapType.valueOf(MapType.DEFAULT.name()).toString(), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapStyle = style;
                mapStyleNotLoaded = true;
                if (mMarkManager == null) {
                    mMarkManager = new MarkManager(context, mapboxMap);
                }
                if (mLayerManager == null) {
                    mLayerManager = new LayerManager(context, mapboxMap);
                }
                if (listener != null) {
                    listener.onMapReady();
                }
            }
        });
//        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
//            @Override
//            public boolean onMapClick(@NonNull LatLng latLng) {
//                PointF pointF = mapboxMap.getProjection().toScreenLocation(latLng);
//                Marker selMark = mMarkManager.handleClickObjet(pointF);
//                if (selMark != null && onMarkerClickListener != null) {
//                    onMarkerClickListener.onMarkerClick(selMark);
//                    return true;
//                }
//                return false;
//            }
//        });

        mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public boolean onMapLongClick(@NonNull LatLng latLng) {
                if (mapLongClickListener != null) {
                    mapLongClickListener.OnMapLongClickListener(new com.example.wzmap_location.wzMap.model.LatLng(latLng.getLatitude(), latLng.getLongitude()));
                }
                return false;
            }
        });

        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (moveListener != null) {
                    moveListener.onCameraMove();
                }
            }
        });
        mapboxMap.addOnCameraMoveCancelListener(new MapboxMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                if (moveCancleListener != null) {
                    moveCancleListener.onCameraMoveCanceled();
                }
            }
        });
        mapboxMap.addOnCameraMoveStartedListener(new MapboxMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (moveStartListener != null) {
                    moveStartListener.onCameraMoveStarted(i);
                }
            }
        });
        mapboxMap.addOnScaleListener(new MapboxMap.OnScaleListener() {
            @Override
            public void onScaleBegin(@NonNull StandardScaleGestureDetector standardScaleGestureDetector) {
                if (onScaleListener != null) {
                    onScaleListener.onScaleBegin(standardScaleGestureDetector);
                }
            }

            @Override
            public void onScale(@NonNull StandardScaleGestureDetector standardScaleGestureDetector) {
                if (onScaleListener != null) {
                    onScaleListener.onScale(standardScaleGestureDetector);
                }
            }

            @Override
            public void onScaleEnd(@NonNull StandardScaleGestureDetector standardScaleGestureDetector) {
                if (onScaleListener != null) {
                    onScaleListener.onScaleEnd(standardScaleGestureDetector);
                }
            }
        });
        mapboxMap.addOnFlingListener(new MapboxMap.OnFlingListener() {
            @Override
            public void onFling() {
                if (onFlingListener != null) {
                    onFlingListener.onFling();
                }
            }
        });
        mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
            @Override
            public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
                if (onMoveListener != null) {
                    onMoveListener.onMoveBegin(moveGestureDetector);
                }
            }

            @Override
            public void onMove(@NonNull MoveGestureDetector moveGestureDetector) {
                if (onMoveListener != null) {
                    onMoveListener.onMove(moveGestureDetector);
                }
            }

            @Override
            public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {
                if (onMoveListener != null) {
                    onMoveListener.onMoveEnd(moveGestureDetector);
                }
            }
        });
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF tapPoint = new PointF(motionEvent.getX(), motionEvent.getY());

                // marker图标的长宽
                float averageIconWidth = 40.0f;
                float averageIconHeight = 40.0f;
                float left = tapPoint.x - averageIconWidth / 2;
                float top = tapPoint.y - averageIconHeight / 2;
                float right = tapPoint.x + averageIconWidth / 2;
                float bottom = tapPoint.y + averageIconHeight / 2;
                // 基于触摸位置生成一个矩形
                RectF tapRect = new RectF(left, top, right, bottom);
                //判断是否有marker在这个矩形内
                return dragMark(motionEvent, tapRect);
            }
        });

    }

    private Marker curMoveMark;
    private boolean isDraging = false;
    private static final int MAX_CLICK_DISTANCE = 15;
    private float pressedX;
    private float pressedY;

    private Boolean dragMark(MotionEvent event, RectF rectF) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //获取地图上所有marker
                curMoveMark = mMarkManager.getRectMark(rectF);
                lastClickTime = System.currentTimeMillis();
                isDraging = false;
                pressedX = event.getX();
                pressedY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (curMoveMark != null) {
                    long curTimeStamp = System.currentTimeMillis();
                    if (curTimeStamp - lastClickTime >= DRAG_TIME || DistanceUtil.distance(this.context, pressedX, pressedY, event.getX(), event.getY()) >= MAX_CLICK_DISTANCE) { // 拖动
                        if (curMoveMark != null && this.onMarkerDragListener != null) {
                            this.onMarkerDragListener.onMarkerDragStart(curMoveMark);
                        }
                        PointF tapPoint = new PointF(event.getX(), event.getY());
                        // 将像素点转换为坐标
                        com.example.wzmap_location.wzMap.model.LatLng curPoint = mMarkManager.fromScreenPoint(tapPoint);
                        if (curPoint != null && this.onMarkerDragListener != null) {
                            mMarkManager.updateMark(curMoveMark, curPoint);
                            this.onMarkerDragListener.onMarkerDrag(curMoveMark);
                        }
                        isDraging = true;
                        return true;
                    }
                }
                break;
            }
            // 如果保存需要移动的marker容器不为空，消费触摸事件
            case MotionEvent.ACTION_UP: {
                if (isDraging) { // 拖动
                    if (curMoveMark != null && this.onMarkerDragListener != null) {
                        this.onMarkerDragListener.onMarkerDragEnd(curMoveMark);
                        return true;
                    }
                } else {
                    PointF tapPoint = new PointF(event.getX(), event.getY());
                    Marker selMark = mMarkManager.handleClickObjet(tapPoint);
                    if (selMark != null && onMarkerClickListener != null) {
                        onMarkerClickListener.onMarkerClick(selMark);
                        return true;
                    }
                }
                curMoveMark = null;
            }
            case MotionEvent.ACTION_CANCEL: {

                break;
            }
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 切换地图样式风格
     *
     * @param mapType
     */
    private void setStyle(MapType mapType) {
        removeLayer();
        if (mMarkManager != null) {
            mMarkManager.removeAllLayers();
        }
        if (mLayerManager != null) {
            mLayerManager.removeAll();
        }
        String style = MapType.valueOf(mapType.name()).toString();
        mapboxMap.setStyle(style, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapStyle = style;
                mapStyleNotLoaded = true;
                mMarkManager.setMapboxMap(mapboxMap);
                mMarkManager.initData();
                mLayerManager.initMapBox(mapboxMap);
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mBlat, mBLon), 12.0), 100);
            }
        });
    }

    /**
     * 清除所有标记
     */
    public void removeLayer() {
        mapStyle.removeImage(BLUE_ICON_ID);
        mapStyle.removeLayer(SYMBOL_LAYER_ID);
        mapStyle.removeSource(SOURCE_ID);
    }

    /**
     * 添加标注
     *
     * @param markerOptions mark 选项类
     * @return 返回添加mark
     */
    public Marker addMarker(MarkerOptions markerOptions) {
        if (markerOptions == null) {
            return null;
        }
        if (mMarkManager == null) {
            return null;
        }
        return mMarkManager.addMark(markerOptions);
    }

    /**
     * 添加线条
     *
     * @param polylineOptions 线条选项类
     * @return 返回添加线条
     */
    public final Polyline addPolyline(PolylineOptions polylineOptions) {
        if (polylineOptions == null) {
            return null;
        }
        if (mLayerManager == null) {
            return null;
        }
        try {
            return this.mLayerManager.addPolyline(polylineOptions);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 移除图层
     *
     * @param layerId 图层 ID
     */
    public final void removeLayer(String layerId) {
        if (mLayerManager == null || TextUtils.isEmpty(layerId)) {
            return;
        }
        try {
            this.mLayerManager.removeOverlay(layerId);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 添加圆
     *
     * @param circleOptions 圆形选项类
     * @return 返回添加圆
     */
    public final Circle addCircle(CircleOptions circleOptions) {
        if (circleOptions == null) {
            return null;
        }
        if (mLayerManager == null) {
            return null;
        }
        try {
            return this.mLayerManager.addCircle(circleOptions);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 添加多边形
     * @param polygonOptions 多边形选项类
     * @return 返回添加多边形对象
     */
    public final Polygon addPolygon(PolygonOptions polygonOptions) {
        if (polygonOptions == null) {
            return null;
        }
        if (mLayerManager == null) {
            return null;
        }
        try {
            return this.mLayerManager.addPolygon(polygonOptions);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 获取地图中心位置
     *
     * @return 经纬度
     */
    public com.example.wzmap_location.wzMap.model.LatLng getCenterLocation(){
        if (mapboxMap == null) {
            return null;
        }
        CameraPosition cameraPosition = mapboxMap.getCameraPosition();
        if (cameraPosition == null || cameraPosition.target == null) {
            return null;
        }
        return new com.example.wzmap_location.wzMap.model.LatLng(cameraPosition.target.getLatitude(), cameraPosition.target.getLongitude());
    }
}
