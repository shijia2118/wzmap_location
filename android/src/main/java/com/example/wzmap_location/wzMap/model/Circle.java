package com.example.wzmap_location.wzMap.model;

import android.text.TextUtils;


import com.example.wzmap_location.wzMap.IMapLayer;

import java.lang.ref.WeakReference;

/**
 * 地图上的一个圆形标注
 *
 * @author yihong.chen
 */
public class Circle extends BaseOverlay {

    WeakReference<IMapLayer> iMapLayerWeakRef;
    private CircleOptions options;

    /**
     * 定义了在地图上绘制圆的类
     *
     * @param iMapLayer layer 图层
     * @param options   circle 选项
     */
    public Circle(IMapLayer iMapLayer, CircleOptions options) {
        super("");
        this.iMapLayerWeakRef = new WeakReference(iMapLayer);
        this.options = options;
    }

    /**
     * 定义了在地图上绘制圆的类
     *
     * @param iMapLayer layer 图层
     * @param options   circle 选项
     * @param id        layerID
     */
    public Circle(IMapLayer iMapLayer, CircleOptions options, String id) {
        super(id);
        this.iMapLayerWeakRef = new WeakReference<>(iMapLayer);
        this.options = options;
    }

    /**
     * 图层移除
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
     * @return 返回圆的id
     */
    public String getId() {
        try {
            return this.overlayId;
        } catch (Throwable error) {
            error.printStackTrace();
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
     * 设置圆的边框宽度，单位像素
     *
     * @param width 边框宽度
     */
    public void setStrokeWidth(float width) {
        try {
            if (this.options != null) {
                this.options.strokeWidth(width);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 获取圆的边框宽度
     */
    public float getStrokeWidth() {
        try {
            return this.options != null ? this.options.getStrokeWidth() : 0.0F;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * 设置圆的边框颜色
     *
     * @param color 颜色
     */
    public void setStrokeColor(int color) {
        try {
            if (this.options != null) {
                this.options.strokeColor(color);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * 获取圆的边框颜色
     *
     * @return 返回边框圆的边框颜色
     */
    public int getStrokeColor() {
        try {
            return this.options != null ? this.options.getStrokeColor() : 0;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置圆的 Z轴数值
     *
     * @param zIndex Z 轴数值
     */
    public void setZIndex(float zIndex) {
        try {
            if (this.options != null) {
                this.options.setzIndex(zIndex);
                this.updateOption();
            }

        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * 获取圆的 Z 轴数值
     *
     * @return 返回 Z 轴数值
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
     * 设置圆的可见属性
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
     * @return 是否可见
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
