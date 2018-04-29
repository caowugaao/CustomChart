package com.gx.morgan.chartlib.utils;

import java.util.Collection;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/4/15 14:16
 */
public class CommonUtil {
    private CommonUtil(){}

    public static void checkNull(Object targetObj, String targetDesc) {
        if (null == targetObj) {
            throw new NullPointerException(targetDesc + " cannor be NULL");
        }
    }

    public static<T> boolean checkCollectionEmpty(Collection<T> collection){
      return null==collection?true:collection.isEmpty();
    }
}
