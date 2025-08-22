package com.example.wzmap_location.wzMap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 创建线条的选项类
 *
 * @author yihong.chen
 */
public class PolylineOptions extends BaseOptions implements Serializable {
    public static final int DOTTEDLINE_TYPE_SQUARE = 0;
    public static final int DOTTEDLINE_TYPE_CIRCLE = 1;

    private final List<LatLng> points;
    private float width = 10.0F;
    private int color = -16777216;
    private float zIndex = 0.0F;
    private boolean isVisible = true;
    private boolean isDottedLine = false;

    /**
     * 构造函数
     */
    public PolylineOptions() {
        this.points = new ArrayList();
    }

    /**
     * 追加一个顶点到线段的坐标集合
     *
     * @param latLng 单个坐标
     * @return PolylineOptions对象
     */
    public final PolylineOptions add(LatLng latLng) {
        if (latLng != null) {
            try {
                this.points.add(latLng);
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }

        return this;
    }

    /**
     * 追加一批顶点到线段的坐标集合
     *
     * @param latLngs 要添加的顶点集合
     * @return 追加一批顶点到线段终点的 PolylineOptions对象
     */
    public final PolylineOptions add(LatLng... latLngs) {
        if (latLngs != null) {
            try {
                this.points.addAll(Arrays.asList(latLngs));
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }

        return this;
    }

    /**
     * 追加一批顶点到线段的坐标集合
     *
     * @param latLngs 要添加的顶点集合
     * @return 追加一批顶点到线段终点的 PolylineOptions对象
     */
    public final PolylineOptions addAll(List<LatLng> latLngs) {
        if (latLngs != null) {
            try {
                this.points.addAll(latLngs);
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }

        return this;
    }

    /**
     * @return 获取线段的宽度
     */
    public final float getWidth() {
        return this.width;
    }

    /**
     * 设置线段的宽度，默认为10
     *
     * @param width 线宽
     * @return 设置线段新宽度的PolylineOptions对象
     */
    public final PolylineOptions width(float width) {
        this.width = width;
        return this;
    }

    /**
     * @return 获取线段的颜色
     */
    public final int getColor() {
        return this.color;
    }

    /**
     * 设置线段的颜色
     *
     * @param color 颜色值
     * @return 设置线段颜色的 PolylineOptions 对象
     */
    public final PolylineOptions color(int color) {
        this.color = color;
        return this;
    }

    /**
     * @return 获取线段的Z轴值
     */
    public final float getzIndex() {
        return this.zIndex;
    }

    /**
     * @param zIndex 设置线段Z轴的值
     * @return 设置线段Z轴 PolylineOptions 对象
     */
    public final PolylineOptions zIndex(float zIndex) {
        if (this.zIndex != zIndex) {
            //
        }
        this.zIndex = zIndex;
        return this;
    }

    /**
     * @return 是否可见
     */
    public final boolean isVisible() {
        return this.isVisible;
    }

    /**
     * 设置线段的可见性
     *
     * @param visible 一个表示线段是否可见的布尔值，true表示可见，false表示不可见
     * @return 设置新可见属性的PolylineOptions对象
     */
    public final PolylineOptions visible(boolean visible) {
        isVisible = visible;
        return this;
    }

    /**
     * 设置是否画虚线，默认为false，画实线
     *
     * @param isDottedLine true，画虚线；false，画实线。
     * @return 设置线段是否为虚线的PolylineOptions对象
     */
    public final PolylineOptions setDottedLine(boolean isDottedLine) {
        this.isDottedLine = isDottedLine;
        return this;
    }

    /**
     * @return 获取线段是否为虚线
     */
    public final boolean isDottedLine() {
        return this.isDottedLine;
    }

    /**
     * 设置线段的点坐标集合,如果以前已经存在点,则会清空以前的点
     *
     * @param points 要设置的顶点集合
     */
    public final void setPoints(List<LatLng> points) {
        if (points != null && this.points != points) {
            try {
                this.points.clear();
                this.points.addAll(points);
                return;
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }
    }

    /**
     * @return 获取线段的点坐标列表
     */
    public final List<LatLng> getPoints() {
        return this.points;
    }


}
