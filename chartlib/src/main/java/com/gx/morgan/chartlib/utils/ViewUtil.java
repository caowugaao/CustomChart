package com.gx.morgan.chartlib.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/3/11 11:02
 */
public class ViewUtil {
    private ViewUtil() {
    }

    public static int optInt(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getInt(index, def);
    }

    public static float optPixelSize(TypedArray typedArray, int index, float def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getDimension(index, def);
    }

    public static int optPixelSize(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getDimensionPixelOffset(index, def);
    }

    public static int optColor(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getColor(index, def);
    }

    public static boolean optBoolean(TypedArray typedArray, int index, boolean def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getBoolean(index, def);
    }

    public static String optString(TypedArray typedArray, int index, String def){
        if (typedArray == null) {
            return def;
        }
        return typedArray.getString(index);
    }

    public static float dp2px(Context context,float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static float sp(Context context,float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources().getDisplayMetrics());
    }


}
