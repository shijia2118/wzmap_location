package com.example.wzmap_location.wzMap.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * 创建圆的选项类
 *
 * @author yihong.chen
 */
public class CircleOptions extends BaseOptions implements Serializable {

    private float radius = 10f;
    private int fillColor = Color.TRANSPARENT;
    private int strokeColor = Color.BLACK;
    private float strokeWidth = 1;
    private LatLng centerPoint;
    private boolean isVisible = true;
    private float zIndex = 0.0F;
    private boolean isDottedLine = false;

    /**
     * 设置圆心经纬度坐标
     *
     * @param latLng 坐标
     * @return 返回圆的选项类
     */
    public CircleOptions center(LatLng latLng) {
        this.centerPoint = latLng;
        return this;
    }

    /**
     * 设置圆的半径，单位米
     *
     * @param radius 圆半径
     * @return 返回圆的选项类
     */
    public CircleOptions radius(float radius) {
        this.radius = radius;
        return this;
    }

    /**
     * 设置圆的边框宽度，单位像素
     *
     * @param width 边框宽
     * @return 返回圆的选项类
     */
    public final CircleOptions strokeWidth(float width) {
        this.strokeWidth = width;
        return this;
    }

    /**
     * 设置圆的边框颜色，ARGB格式。
     *
     * @param strokeColor 边框颜色
     * @return 返回圆的选项类
     */
    public final CircleOptions strokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    /**
     * 设置圆的填充颜色
     *
     * @param fillColor 填充颜色
     * @return 返回圆的选项类
     */
    public final CircleOptions fillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * 设置圆的可见属性
     *
     * @param isVisible 是否可见
     * @return 返回圆的选项类
     */
    public final CircleOptions visible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    /**
     * 设置圆的边框虚线形状
     *
     * @param dottedLine 是否虚线
     * @return 返回圆的选项类
     */
    public final CircleOptions setDottedLine(boolean dottedLine) {
        this.isDottedLine = dottedLine;
        return this;
    }

    /**
     * @return 获取圆心经纬度坐标
     */
    public final LatLng getCenter() {
        return this.centerPoint;
    }

    /**
     * @return 获取圆的半径，单位米
     */
    public final float getRadius() {
        return this.radius;
    }

    /**
     * @return 获取圆的边框宽度
     */
    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    /**
     * @return 获取圆的边框颜色
     */
    public final int getStrokeColor() {
        return this.strokeColor;
    }

    /**
     * 获取圆的填充颜色
     *
     * @return 返回填充颜色
     */
    public final int getFillColor() {
        return this.fillColor;
    }

    /**
     * @return 获取圆的Z轴的值
     */
    public float getzIndex() {
        return zIndex;
    }

    /**
     * @param zIndex 设置圆的Z轴数值，默认为0
     */
    public void setzIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * @return 边框是否a虚线
     */
    public boolean isDottedLine() {
        return isDottedLine;
    }

    /**
     * @return 获取圆是否可见
     */
    public boolean isVisible() {
        return isVisible;
    }
}
