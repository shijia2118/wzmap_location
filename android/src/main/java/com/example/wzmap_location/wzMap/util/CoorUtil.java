package com.example.wzmap_location.wzMap.util;

import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;

import com.mapbox.mapboxsdk.style.expressions.Expression;

/**
 * @Author yihong
 * @CreateTime 2024 年 01 月 18 日
 */
public class CoorUtil {
    public static Expression getCircle(float accuracy) {
        return interpolate(exponential(1.0f), zoom(),
                stop(9f, getRadius(accuracy, 2, -7)),
                stop(10f,getRadius(accuracy, 2, -6)),
                stop(11f, getRadius(accuracy, 2, -5)),
                stop(12f, getRadius(accuracy, 2, -4)),
                stop(13f, getRadius(accuracy, 2, -3)),
                stop(14f, getRadius(accuracy, 2, -2)),
                stop(15f, getRadius(accuracy, 2, -1)),
                stop(16f, accuracy),
                stop(17f, getRadius(accuracy, 2, 1)),
                stop(18f, getRadius(accuracy, 2, 2)),
                stop(19f, getRadius(accuracy, 2, 3)),
                stop(20f, getRadius(accuracy, 2, 4)),
                stop(21f, getRadius(accuracy, 2, 5)),
                stop(22f, getRadius(accuracy, 2, 6)),
                stop(23f, getRadius(accuracy, 2, 7)),
                stop(24f, getRadius(accuracy, 2, 8))
        );
    }

    public static float getRadius(float accuracy, float buffer, int count) {
        return (float) (accuracy * Math.pow(buffer, count));
    }
}
