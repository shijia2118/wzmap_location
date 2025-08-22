package com.example.wzmap_location.wzMap.model;

import android.text.TextUtils;


import com.example.wzmap_location.wzMap.IMapLayer;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 定义在地图上绘制多边形覆盖物。一个多边形可以凸面体，也可是凹面体
 *
 * @author yihong.chen
 */
public class Polygon extends BaseOverlay {

    WeakReference<IMapLayer> iMapLayerWeakRef;
    private PolygonOptions options;

    /**
     * 构造函数
     *
     * @param iMapLayer 图层接口
     * @param options   多边形选项
     */
    public Polygon(IMapLayer iMapLayer, PolygonOptions options) {
        super("");
        this.iMapLayerWeakRef = new WeakReference<>(iMapLayer);
        this.options = options;
    }

    /**
     * 构造函数
     *
     * @param iMapLayer 图层接口
     * @param options   多边形选项
     * @param id        图层 ID
     */
    public Polygon(IMapLayer iMapLayer, PolygonOptions options, String id) {
        super(id);
        this.iMapLayerWeakRef = new WeakReference<>(iMapLayer);
        this.options = options;
    }

    /**
     * @return 图屋 ID
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
     * 设置多边形的顶点
     *
     * @param points 坐标数组
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
     * @return 获取多边形的顶点列表
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
     * 设置多边形的边框宽度
     *
     * @param width 边框宽度
     */
    public void strokeWidth(float width) {
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
     * @return 返回多边形的宽度
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
     * 设置多边形的填充颜色
     *
     * @param color 填充颜色
     */
    public void setFillColor(int color) {
        try {
            if (this.options != null) {
                this.options.fillColor(color);
                this.updateOption();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * @return 返回填充颜色
     */
    public int getFillColor() {
        try {
            return this.options != null ? this.options.getFillColor() : 0;
        } catch (Throwable var1) {
            var1.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置多边形的 Z轴数值
     *
     * @param zIndex Z 轴数值
     */
    public void setZIndex(float zIndex) {
        try {
            if (this.options != null) {
                this.options.setZIndex(zIndex);
                this.updateOption();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * 获取多边形的 Z轴值
     *
     * @return 多边形的 Z 轴数值
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
     * 设置多边形的可见属性
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
     * @return 获取多边形是否可见
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
