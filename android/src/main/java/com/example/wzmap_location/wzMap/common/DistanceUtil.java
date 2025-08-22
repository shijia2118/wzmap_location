package com.example.wzmap_location.wzMap.common;

import android.content.Context;

public class DistanceUtil {

    /**
     * 两点之间距离
     *
     * @param context 应用上下文
     * @param x1      坐标1 X 轴
     * @param y1      坐标1 Y 轴
     * @param x2      坐标2 X 轴
     * @param y2      坐标2 Y 轴
     * @return  返回距离
     */
    public static float distance(Context context, float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(context, distanceInPx);
    }

    private static float pxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
