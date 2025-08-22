package com.example.wzmap_location.wzMap;

import androidx.annotation.NonNull;

import com.example.wzmap_location.wzMap.common.MapType;
import com.example.wzmap_location.wzMap.model.LatLng;
import com.example.wzmap_location.wzMap.model.Marker;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;

public interface Map {

    void setMapReadyListener(MapReadyListener listener);

    void showLocationView(double lnt, double lat, double accuracy);

    /**
     * 缩小
     */
    void zoomIn();

    /**
     * 放大
     */
    void zoomOut();


    /**
     * 切换地图样式
     *
     * @param mode 地图模式，四种样式   DEFAULT,LIGHT_BLUE、GRAY_WHITE、DARK_BLUE
     */
    void setMapType(MapType mode);

    /**
     * 添加长按地图事件
     *
     * @param mapLongClickListener 长按地图接口
     */
    void addOnMapLongClickListener(MapLongClickListener mapLongClickListener);

    /**
     * 添加可视区域移动接口
     *
     * @param cameraMoveListener 可视区域移动接口
     */
    void addOnCameraMoveListener(CameraMoveListener cameraMoveListener);

    /**
     * 添加可视区域移动取消接口
     *
     * @param cameraMoveCancelListener 可视区域取消接口
     */
    void addOnCameraMoveCancelListener(CameraMoveCancelListener cameraMoveCancelListener);

    /**
     * 添加可视区域移动开始接口
     *
     * @param cameraMoveStartedListener 可视区域移动开始接口
     */
    void addOnCameraMoveStartedListener(CameraMoveStartedListener cameraMoveStartedListener);

    /**
     * 添加地图移动接口
     *
     * @param onMoveListener 移动接口
     */
    void onAddMoveListener(OnMoveListener onMoveListener);

    /**
     * 添加地图缩放接口
     *
     * @param scaleListener 缩放接口
     */
    void onAddScaleListener(OnScaleListener scaleListener);

    /**
     * 添加地图飞
     *
     * @param flingListener 飞接口
     */
    void onAddFlingListener(OnFlingListener flingListener);

    /**
     * 添加 mark 点击事件接口
     *
     * @param onMarkerClickListener 点击接口
     */
    void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener);

    /**
     * 添加 mark 拖动事件接口
     *
     * @param onMarkerDragListener 拖动接口
     */
    void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener);

    /**
     * 地图准备回调
     */
    interface MapReadyListener {
        /**
         * 地图加载完成
         *
         */
        void onMapReady();
    }

    /**
     * MARK 点击事接口
     */
    interface OnMarkerClickListener{
        /**
         * mark 点击回调
         *
         * @param marker 当前 mark
         *
         * @return 是否点击
         */
        boolean onMarkerClick(Marker marker);
    }

    /**
     * MARK 拖动接口
     */
    interface OnMarkerDragListener{

        /**
         * mark 开始拖动
         *
         * @param marker 当前 mark
         */
        void onMarkerDragStart(Marker marker);

        /**
         * mark 拖动中
         *
         * @param marker 当前 mark
         */
        void onMarkerDrag(Marker marker);

        /**
         * mark 拖动结束
         *
         * @param marker 当前 mark
         */
        void onMarkerDragEnd(Marker marker);
    }

    /**
     * 地图长按事件接口
     */
    interface MapLongClickListener {

        /**
         * 长按地图事件
         *
         * @param latLng 经纬度
         */
        void OnMapLongClickListener(@NonNull LatLng latLng);
    }

    /**
     * 当可视范围移动回调的接口
     */
    interface CameraMoveListener {

        /**
         * 可视范围移动
         */
        void onCameraMove();
    }

    /**
     * 当可视范围取消回调的接口
     */
    interface CameraMoveCancelListener {

        /**
         * 可视范围移动取消
         */
        void onCameraMoveCanceled();
    }

    /**
     * 当可视范围开始移动回调的接口
     */
    interface CameraMoveStartedListener {

        /**
         * 当可视范围开始移动
         *
         * @param index 移动
         */
        void onCameraMoveStarted(int index);
    }

    /**
     * 缩放接口
     */
    interface OnScaleListener {
        void onScaleBegin(@NonNull StandardScaleGestureDetector var1);

        void onScale(@NonNull StandardScaleGestureDetector var1);

        void onScaleEnd(@NonNull StandardScaleGestureDetector var1);
    }

    /**
     * 移动接口
     */
    interface OnMoveListener {
        void onMoveBegin(@NonNull MoveGestureDetector var1);

        void onMove(@NonNull MoveGestureDetector var1);

        void onMoveEnd(@NonNull MoveGestureDetector var1);
    }

   interface OnFlingListener {
        void onFling();
    }
}
