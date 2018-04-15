package com.gx.morgan.chartlib.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.gx.morgan.chartlib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description：折线图
 * <br>author：caowugao
 * <br>time：2018/4/15 14:12
 */
public class LineView extends CustomView {


    public static class XCoordinateBulgeDirection {
        private XCoordinateBulgeDirection() {
        }

        public static final int UP = 1;
        public static final int DOWN = 2;
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({XCoordinateBulgeDirection.UP, XCoordinateBulgeDirection.DOWN})
    public @interface XCoordinateBulgeDirectionType {
    }

    public static class ContentData {
        public ContentData(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
    }
    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected int contentWidth(int widthMeasureSpec) {
        return (int) ViewUtil.dp2px(getContext(), 300);
    }

    @Override
    protected int contentHeight(int heightMeasureSpec) {
        return (int) ViewUtil.dp2px(getContext(), 350);
    }

    private static float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    private static float getTextHeight(Paint paint) {
        return (-paint.ascent() + paint.descent());
    }
}
