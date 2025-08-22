package com.example.wzmap_location.wzMap.model;


import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Marker 的选项类
 *
 * @author yihong.chen
 */
public class MarkerOptions implements Serializable {
    private LatLng position;
    private String title;
    private String snippet;
    private Bitmap bitmap;
    private boolean isDraggable;

    /**
     * 设置Marker覆盖物的位置坐标
     *
     * @param position 当前MarkerOptions对象的经纬度
     * @return MarkerOptions对象
     */
    public MarkerOptions position(LatLng position) {
        this.position = position;
        return this;
    }

    /**
     * 设置 Marker覆盖物的标题
     *
     * @param title Marker 的标题
     * @return 一个设置标题的MarkerOptions对象
     */
    public MarkerOptions title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置 Marker覆盖物的文字描述
     *
     * @param snippet Marker上的文字描述
     * @return 一个设置文字描述的MarkerOptions对象
     */
    public MarkerOptions snippet(String snippet) {
        this.snippet = snippet;
        return this;
    }

    /**
     * 设置 Marker覆盖物的图标
     *
     * @param bitmap 图标的 bitmap 对象
     * @return MarkerOptions对象
     */
    public final MarkerOptions icon(Bitmap bitmap) {
        try {
            this.bitmap = bitmap;
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return this;
    }

    /**
     * 设置Marker覆盖物是否可拖拽
     *
     * @param isDraggable 一个布尔值，表示Marker是否可拖拽，true表示可拖拽，false表示不可拖拽
     * @return 设Marker是否可拖拽的MarkerOptions对象
     */
    public final MarkerOptions draggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
        return this;
    }

    /**
     * 获取Marker覆盖物的坐标位置
     *
     * @return 当前 MarkerOptions 对象所设置的经纬度对象
     */
    public LatLng getPosition() {
        return position;
    }

    /**
     * 获取Marker覆盖物的标题
     *
     * @return Marker的标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取Marker覆盖物的文字片段
     *
     * @return 当前MarkerOptions 对象所设置的文字片段
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * 设置 Marker覆盖物的文字描述
     *
     * @param snippet 文字描术内容
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     * @return 返回当前 mark 的图标
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * 设置图标
     *
     * @param bitmap 图标 bitmap 对象
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * @return 返回是否可拖动
     */
    public boolean isDraggable() {
        return isDraggable;
    }

    /**
     * 设置是否可拖动
     *
     * @param draggable 是否可拖动， true 可拖动，false 不可拖动
     */
    public void setDraggable(boolean draggable) {
        isDraggable = draggable;
    }
}
