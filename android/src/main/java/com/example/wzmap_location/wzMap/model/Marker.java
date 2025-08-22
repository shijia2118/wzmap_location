package com.example.wzmap_location.wzMap.model;

import android.text.TextUtils;

import com.example.wzmap_location.wzMap.IMapMarkLayer;
import com.example.wzmap_location.wzMap.util.MarkManager;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Marker 是在地图上的一个点绘制图标
 *
 * @author yihong.chen
 */
public class Marker extends BaseOverlay implements Serializable {

    private LatLng latLng;
    private MarkerOptions options;
    WeakReference<IMapMarkLayer> iMapLayerWeakRef;

    /**
     * 构造函数，定义地图 Marker 覆盖物 Marker 是在地图上的一个点绘制图标
     *
     * @param markManager   管理类
     * @param markerOptions 选项类
     */
    public Marker(MarkManager markManager, MarkerOptions markerOptions) {
        super("");
        iMapLayerWeakRef = new WeakReference<IMapMarkLayer>(markManager);
        this.options = markerOptions;
    }

    /**
     * 构造函数，定义地图 Marker 覆盖物 Marker 是在地图上的一个点绘制图标
     *
     * @param markManager   管理类
     * @param markerOptions 选项类
     * @param idName        mark id
     */
    public Marker(MarkManager markManager, MarkerOptions markerOptions, String idName) {
        super(idName);
        iMapLayerWeakRef = new WeakReference<IMapMarkLayer>(markManager);
        this.options = markerOptions;
    }

    /**
     * 设置 Marker 覆盖物的位置坐标
     *
     * @param latLng 坐标
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * 每个 marker 的唯一标识，用来区分不同的 Marker
     *
     * @return 返回 Marker 的 Id，
     */
    public String getId() {
        return this.overlayId;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.options = markerOptions;
    }

    public MarkerOptions getMarkOptions() {
        return options;
    }

    /**
     * @return 获取 Marker 覆盖物的位置坐标
     */
    public final LatLng getPosition() {
        return latLng;
    }

    /**
     * 显示 Marker 覆盖物的信息窗口
     */
    public final void showInfoWindow() {
        try {
            IMapMarkLayer mapLayer = this.iMapLayerWeakRef.get();
            if (!TextUtils.isEmpty(overlayId) && mapLayer != null) {
                mapLayer.showInfoWindow(overlayId);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 隐藏 Mark 覆盖物的信息窗口
     */
    public final void hideInfoWindow() {
        try {
            IMapMarkLayer mapLayer = this.iMapLayerWeakRef.get();
            if (!TextUtils.isEmpty(overlayId) && mapLayer != null) {
                mapLayer.hideInfoWindow(overlayId);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 删除当前 mark
     */
    public final void remove() {
        try {
            if (this.isInfoWindowShown()) {
                this.hideInfoWindow();
            }
            try {
                IMapMarkLayer mapLayer = this.iMapLayerWeakRef.get();
                if (!TextUtils.isEmpty(overlayId) && mapLayer != null) {
                    mapLayer.removeOverlay(overlayId);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 返回 Marker覆盖物的信息窗口是否显示，true: 显示，false: 不显示。
     */
    public final boolean isInfoWindowShown() {
        try {
            IMapMarkLayer mapLayer = this.iMapLayerWeakRef.get();
            return mapLayer.isInfoWindowShown(overlayId);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }
}
