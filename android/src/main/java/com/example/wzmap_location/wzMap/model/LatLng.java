package com.example.wzmap_location.wzMap.model;


import java.io.Serializable;

/**
 * 经纬度
 *
 * @author yihong.chen
 */
public class LatLng implements Serializable {
    public LatLng(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double lat;
    public double lon;
}
