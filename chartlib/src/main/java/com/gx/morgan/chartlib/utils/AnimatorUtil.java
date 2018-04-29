package com.gx.morgan.chartlib.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.SparseArray;

import java.util.List;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/4/29 15:53
 */
public class AnimatorUtil {
    private AnimatorUtil(){}
    public static void cancelAnimator(Animator animator){
        if(null!=animator&&animator.isRunning()){
            animator.cancel();
        }
    }

    public static void cancelAnimator(List<Animator> animators){
        if(!CommonUtil.checkCollectionEmpty(animators)){
            for(Animator animator:animators){
                cancelAnimator(animator);
            }
        }
    }

    public static void cancelAnimator(SparseArray<ValueAnimator> animators){
        if(null==animators||0==animators.size()){
            return;
        }
        for (int i = 0,size = animators.size(); i < size; i++) {
            Animator animator = animators.valueAt(i);
            cancelAnimator(animator);
        }
    }
}
