package com.example.wzmap_location.wzMap.model;

import android.text.TextUtils;


import com.example.wzmap_location.wzMap.IMapLayer;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 地图上的线条
 *
 * @author yihong.chen
 */
public class Polyline extends BaseOverlay implements Serializable {
    private PolylineOptions options;
    WeakReference<IMapLayer> iMapLayerWeakRef;

    /**
     * 构造函数
     *
     * @param iMapLayer       图层接口
     * @param polylineOptions 线条选项
     */
    public Polyline(IMapLayer iMapLayer, PolylineOptions polylineOptions) {
        super("");
        this.iMapLayerWeakRef = new WeakReference(iMapLayer);
        this.options = polylineOptions;
    }

    /**
     * @param iMapLayer       图层接口
     * @param polylineOptions 线条选项
     * @param overlayId       图层 ID
     */
    public Polyline(IMapLayer iMapLayer, PolylineOptions polylineOptions, String overlayId) {
        super(overlayId);
        this.iMapLayerWeakRef = new WeakReference(iMapLayer);
        this.options = polylineOptions;
        this.overlayId = overlayId;
    }

    /**
     * 从地图上删除当前线条
     */
    public void remove() {
        try {
            IMapLayer iMapLayer;
            if ((iMapLayer = (IMapLayer) this.iMapLayerWeakRef.get()) != null) {
                iMapLayer.removeOverlay(this.overlayId);
            }

            this.overlayId = null;
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 获取当前图层 ID
     */
    public String getId() {
        try {
            return this.overlayId;
        } catch (Throwable error) {
            error.printStackTrace();
            return null;
        }
    }

    /**
     * 设置线段的坐标点列表
     *
     * @param points 坐标点列表
     */
    public void setPoints(List<LatLng> points) {
        try {
            synchronized (this) {
                if (this.options != null) {
                    this.options.setPoints(points);
                    this.updateOption();
                }

            }
        } catch (Throwable var4) {
            var4.printStackTrace();
        }
    }

    /**
     * @return 获取线段的顶点坐标点列表
     */
    public List<LatLng> getPoints() {
        try {
            return this.options != null ? this.options.getPoints() : null;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return null;
        }
    }

    private void updateOption() {
        try {
            synchronized (this) {
                IMapLayer iMapLayer = (IMapLayer) this.iMapLayerWeakRef.get();
                if (!TextUtils.isEmpty(this.overlayId) && iMapLayer != null) {
                    iMapLayer.updateOption(this.overlayId, this.options);
                }

            }
        } catch (Throwable var4) {
        }
    }

    /**
     * 设置线段的宽度
     *
     * @param width 线条宽度
     */
    public void setWidth(float width) {
        try {
            if (this.options != null) {
                this.options.width(width);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 获取线条宽度
     */
    public float getWidth() {
        try {
            return this.options != null ? this.options.getWidth() : 0.0F;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * 设置线条颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        try {
            if (this.options != null) {
                this.options.color(color);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 返回线条颜色
     */
    public int getColor() {
        try {
            return this.options != null ? this.options.getColor() : 0;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0;
        }
    }

    /**
     * @param zIndex 设置线段的z轴值
     */
    public void setZIndex(float zIndex) {
        try {
            if (this.options != null) {
                this.options.zIndex(zIndex);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 获取线段的z轴值
     */
    public float getZIndex() {
        try {
            return this.options != null ? this.options.getzIndex() : 0.0F;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * 设置线段的可见属性
     *
     * @param visible 是否可见
     */
    public final void setVisible(boolean visible) {
        try {
            if (this.options != null) {
                this.options.visible(visible);
                this.updateOption();
            }

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * @return 获取线段是否可见
     */
    public final boolean isVisible() {
        try {
            return this.options != null ? this.options.isVisible() : false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

}
