package com.example.wzmap_location.wzMap.util;

import java.util.List;

/**
 * @Author yihong
 * @CreateTime 2024 年 01 月 25 日
 */
public class CoCalculateUtil {

    private static final double LON_RANGE = 1.31;
    private static final double LON_DELTA = 1.21;

    private static final double LAT_RANGE = 1.51;
    private static final double LAT_DELTA = 1.41;

    public static double[] cal(List<Double> lnglat) {
        return new double[]{lnglat.get(0) / LON_RANGE - LON_DELTA, lnglat.get(1)/ LAT_RANGE - LAT_DELTA};
    }

}
