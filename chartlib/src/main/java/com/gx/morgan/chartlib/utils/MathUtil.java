package com.gx.morgan.chartlib.utils;

import java.util.List;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/3/11 20:35
 */
public class MathUtil {
    private MathUtil(){}
    public static double[] getCoordinate(double hypotenuse,double degree){
        double x=Math.cos(degree)*hypotenuse;
        double y=Math.sin(degree)*hypotenuse;
        return new double[]{x,y};
    }
    public static int max(List<Integer> datas){
        if (null == datas || datas.isEmpty()) {
            return -1;
        }
        int max = datas.get(0);
        for (int data : datas) {
            max = Math.max(max, data);
        }
        return max;
    }

    public static float max(float[] datas){
        if (null == datas || 0==datas.length) {
            return -1;
        }
        float max = datas[0];
        for (float data : datas) {
            max = Math.max(max, data);
        }
        return max;
    }
}
