package com.example.wzmap_location.wzMap.common;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {

    /**
     * 宽高比取最大值缩放图片.
     *
     * @param bitmap     加载的图片
     * @param widthSize  缩放之后的图片宽度,一般就是屏幕的宽度.
     * @param heightSize 缩放之后的图片高度,一般就是屏幕的高度.
     * @return 返回图片 bitmap
     */
    public static Bitmap getScaleImg(Bitmap bitmap, int widthSize, int heightSize) {
        int bmpW = bitmap.getWidth();
        int bmpH = bitmap.getHeight();
        float scaleW = ((float) widthSize) / bmpW;
        float scaleH = ((float) heightSize) / bmpH;
        //取宽高最大比例来缩放图片
        float max = Math.max(scaleW, scaleH);
        Matrix matrix = new Matrix();
        matrix.postScale(max, max);
        return Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, matrix, true);
    }
}
