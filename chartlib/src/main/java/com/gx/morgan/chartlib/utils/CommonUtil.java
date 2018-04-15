package com.gx.morgan.chartlib.utils;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/4/15 14:16
 */
public class CommonUtil {
    private CommonUtil(){}

    public static void checkEmpty(Object targetObj, String targetDesc) {
        if (null == targetObj) {
            throw new NullPointerException(targetDesc + " cannor be NULL");
        }
    }
}
