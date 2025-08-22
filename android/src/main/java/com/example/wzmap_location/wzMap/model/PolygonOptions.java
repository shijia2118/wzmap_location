package com.example.wzmap_location.wzMap.model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * 创建多边形的选项类
 *
 * @author yihong.chen
 */
public class PolygonOptions extends BaseOptions implements Serializable {
    private final List<LatLng> points;
    private float strokeWidth = 10.0F;
    private int fillColor = Color.TRANSPARENT;
    private int strokeColor = Color.BLACK;
    private float zIndex = 0.0F;
    private boolean isDottedLine = false;
    private boolean isVisible = true;

    /**
     * 构造函数
     */
    public PolygonOptions() {
        this.points = new ArrayList();
    }

    /**
     * 添加一个多边形边框的顶点
     *
     * @param latLng 单个坐标
     * @return PolygonOptions 对象
     */
    public final PolygonOptions add(LatLng latLng) {
        try {
            this.points.add(latLng);
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

        return this;
    }

    /**
     * 添加多个多边形边框的顶点
     *
     * @param latLngs 多个顶点坐标
     * @return 加入多个新的多边形边框顶点的 PolygonOptions 对象
     */
    public final PolygonOptions add(LatLng... latLngs) {
        try {
            if (latLngs != null) {
                this.points.addAll(Arrays.asList(latLngs));
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

        return this;
    }

    /**
     * 添加多个多边形边框的顶点
     *
     * @param latLngs 顶点列表
     * @return 加入多个新的多边形边框顶点的 PolygonOptions 对象
     */
    public final PolygonOptions addAll(Iterable<LatLng> latLngs) {
        try {
            if (latLngs != null) {
                Iterator iterator = latLngs.iterator();

                while (iterator.hasNext()) {
                    LatLng latLng = (LatLng) iterator.next();
                    this.points.add(latLng);
                }
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

        return this;
    }

    /**
     * 设置边形边框的顶点
     *
     * @param latLngs 顶点列表
     *
     */
    public final void setPoints(List<LatLng> latLngs) {
        try {
            this.points.clear();
            if (latLngs != null) {
                this.points.addAll(latLngs);
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    /**
     * 设置虚线
     *
     * @param dottedLine 是否虚线
     * @return 设置线段是否为虚线的 PolygonOptions 对象
     */
    public final PolygonOptions setDottedLine(boolean dottedLine) {
        this.isDottedLine = dottedLine;
        return this;
    }

    /**
     * @return 返回多边形顶点列表
     */
    public final List<LatLng> getPoints() {
        return this.points;
    }

    /**
     * 设置边框宽度
     *
     * @param width 边框宽度
     * @return 设置线宽 PolylineOptions 对象
     */
    public final PolygonOptions strokeWidth(float width) {
        this.strokeWidth = width;
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 边框颜色
     * @return 设置线颜色 PolylineOptions 对象
     */
    public final PolygonOptions strokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    /**
     * 设置区域填充颜色
     *
     * @param fillColor 颜色值
     * @return 设置区域颜色 PolygonOptions 对象
     */
    public final PolygonOptions fillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * 设置多边形区域是否可见
     *
     * @param isVisible 是否可见，true 可见，false 不可见
     * @return 设置是否可见 PolygonOptions 对象
     */
    public final PolygonOptions visible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    /**
     * @return 获取多边形的边框宽度
     */
    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    /**
     * @return 获取多边形的边框颜色
     */
    public final int getStrokeColor() {
        return this.strokeColor;
    }

    /**
     * @return 获取多边形的填充颜色
     */
    public final int getFillColor() {
        return this.fillColor;
    }

    /**
     * @return 获取多边形的Z轴数值
     */
    public float getzIndex() {
        return zIndex;
    }

    /**
     * @param zIndex 设置多边形的Z轴数值
     */
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * @return 是否虚线
     */
    public boolean isDottedLine() {
        return isDottedLine;
    }

    /**
     * @return 多边形是否可见
     */
    public boolean isVisible() {
        return isVisible;
    }
}
