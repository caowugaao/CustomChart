package com.gx.morgan.chartlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/3/11 9:25
 */
public abstract class CustomView  extends View{
    private static final String TAG = "CustomView";
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        Log.e(TAG, "onMeasure---minimumWidth = " + minimumWidth + "，minimumHeight="+minimumHeight);
        int width = measureWidth(minimumWidth, widthMeasureSpec);
        int height = measureHeight(minimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    protected int measureWidth(int defaultWidth, int widthMeasureSpec) {

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.e(TAG, "measureWidth ---speSize = " + specSize + "");


        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = contentWidth(widthMeasureSpec)+ getPaddingLeft() + getPaddingRight();
//                defaultWidth = numberBound.width()+ getPaddingLeft() + getPaddingRight();

                Log.e(TAG, "measureWidth---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e(TAG, "measureWidth---speMode = EXACTLY");
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e(TAG, "measureWidth---speMode = UNSPECIFIED");
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    protected  abstract int contentWidth(int widthMeasureSpec);
    protected abstract int contentHeight(int heightMeasureSpec);


    protected int measureHeight(int defaultHeight, int heightMeasureSpec) {

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "measureHeight---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = contentHeight(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
                Log.e(TAG, "measureHeight---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                Log.e(TAG, "measureHeight---speSize = EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                Log.e(TAG, "measureHeight---speSize = UNSPECIFIED");

                break;
        }
        return defaultHeight;

    }


}
