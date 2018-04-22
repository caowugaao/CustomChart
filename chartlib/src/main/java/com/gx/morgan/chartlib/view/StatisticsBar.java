package com.gx.morgan.chartlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.animation.DecelerateInterpolator;

import com.gx.morgan.chartlib.R;
import com.gx.morgan.chartlib.utils.ViewUtil;

import java.util.List;

/**
 * description：柱状图
 * <br>author：caowugao
 * <br>time：2018/3/16 17:56
 */
public class StatisticsBar extends BaseCoordinateView {

    private int statisticBarColor;//柱状颜色
    private int statisticBarWidth;//柱状宽度
    private float dp2;

    private SparseIntArray heightValueMap;
    private SparseArray<ValueAnimator> animatorMap;


    public StatisticsBar(Context context) {
        super(context);
        init(context, null);
    }

    public StatisticsBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StatisticsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StatisticsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        statisticBarColor = Color.BLUE;
        statisticBarWidth = (int) ViewUtil.dp2px(context, 10);
        dp2 = ViewUtil.dp2px(context, 2);

        initAttr(context, attrs);
    }

    private void initAnimator(final int contentDataSize) {
        if (animate) {

            if (null == animatorMap) {
                animatorMap = new SparseArray<>();
            }

            if (null == heightValueMap) {
                heightValueMap = new SparseIntArray();
            }
            for (int i = 0; i < contentDataSize; i++) {
                ValueAnimator animator = ValueAnimator.ofInt();
                animator.setDuration(500);
                animator.setInterpolator(new DecelerateInterpolator());
                final int finalI = i;
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int baeHeightValue = (int) valueAnimator.getAnimatedValue();
                        heightValueMap.put(finalI, baeHeightValue);
                        invalidate();
                    }
                });
                final int finalI1 = i;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        if (finalI1 == contentDataSize - 1) {
                            animateStarted = false;
                        }
                    }
                });
                animatorMap.put(i, animator);
            }
        }
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatisticsBar);
        }
        xTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_xTextSize, xTextSize);
        yTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_yTextSize, yTextSize);
        xTextColor = ViewUtil.optColor(typedArray, R.styleable.StatisticsBar_xTextColor, xTextColor);
        yTextColor = ViewUtil.optColor(typedArray, R.styleable.StatisticsBar_yTextColor, yTextColor);
        statisticBarColor = ViewUtil.optColor(typedArray, R.styleable.StatisticsBar_statisticBarColor,
                statisticBarColor);
        statisticBarWidth = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_statisticBarWidth,
                statisticBarWidth);
        textCoordinatePadding = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_textCoordinatePadding,
                textCoordinatePadding);
        contentPadding = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_contentPadding, contentPadding);
        xCoordinateBulgeDistance = ViewUtil.optPixelSize(typedArray, R.styleable
                .StatisticsBar_xCoordinateBulgeDistance, xCoordinateBulgeDistance);
        xCoordinateBulgeDirection = ViewUtil.optInt(typedArray, R.styleable.StatisticsBar_xCoordinateBulgeDirection,
                XCoordinateBulgeDirection.DOWN);
        coodinatateColor = ViewUtil.optColor(typedArray, R.styleable.StatisticsBar_coodinatateColor, coodinatateColor);
        xCoodinateUnitDesc = ViewUtil.optString(typedArray, R.styleable.StatisticsBar_xCoodinateUnitDesc,
                xCoodinateUnitDesc);
        yCoodinateUnitDesc = ViewUtil.optString(typedArray, R.styleable.StatisticsBar_yCoodinateUnitDesc,
                yCoodinateUnitDesc);
        unitDescTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.StatisticsBar_unitDescTextSize,
                unitDescTextSize);
        unitDescTextColor = ViewUtil.optColor(typedArray, R.styleable.StatisticsBar_unitDescTextColor,
                unitDescTextColor);
        animate = ViewUtil.optBoolean(typedArray, R.styleable.StatisticsBar_animate, true);

    }


    @Override
    protected void onDetachedFromWindow() {
        heightValueMap.clear();
        animatorMap.clear();
        super.onDetachedFromWindow();

    }

    public void setStatisticBarColor(int statisticBarColor) {
        if (this.statisticBarColor != statisticBarColor) {
            this.statisticBarColor = statisticBarColor;
            invalidate();
        }
    }

    public void setStatisticBarWidth(int statisticBarWidth) {
        if (this.statisticBarWidth != statisticBarWidth) {
            this.statisticBarWidth = statisticBarWidth;
            invalidate();
        }
    }



    @Override
    public void initAnimator(boolean animate, List<BaseCoordinateView.ContentData> contentDatas) {
        if (animate) {

            if (null == animatorMap) {
                animatorMap = new SparseArray<>();
            }

            if (null == heightValueMap) {
                heightValueMap = new SparseIntArray();
            }

            final int contentDataSize=contentDatas.size();
            for (int i = 0; i < contentDataSize; i++) {
                ValueAnimator animator = ValueAnimator.ofInt();
                animator.setDuration(500);
                animator.setInterpolator(new DecelerateInterpolator());
                final int finalI = i;
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int baeHeightValue = (int) valueAnimator.getAnimatedValue();
                        heightValueMap.put(finalI, baeHeightValue);
                        invalidate();
                    }
                });
                final int finalI1 = i;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        if (finalI1 == contentDataSize - 1) {
                            animateStarted = false;
                        }
                    }
                });
                animatorMap.put(i, animator);
            }
        }
    }



    private void drawSingleBar(Canvas canvas, int coodinateXStartX, int coodinateXStartY, int xCoordinateDistance,
                               int xSpacing, int xCoodinateMaxData, int barHeight, ContentData contentData) {
        int textStartX;
        int textStartY;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(statisticBarColor);
        int left = (int) (coodinateXStartX + (contentData.x * 1.0 / xCoodinateMaxData) * xCoordinateDistance -
                xSpacing / 2 - statisticBarWidth / 2);
        int top = coodinateXStartY - barHeight;
        int right = left + statisticBarWidth;
        int bottom = coodinateXStartY;
        canvas.drawRect(left, top, right, bottom, paint);


        //画柱状图顶部的数字
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(yTextSize);
        paint.setColor(yTextColor);
        String text = String.valueOf(contentData.y);
        paint.getTextBounds(text, 0, text.length(), textBound);
        int textWidth = textBound.width();
        textStartX = left + statisticBarWidth / 2 - textWidth / 2;
        textStartY = top - textCoordinatePadding;
        canvas.drawText(text, textStartX, textStartY, paint);
    }
    /**
     * @param canvas
     * @param coodinateXStartX x轴开始横坐标
     * @param coodinateXStartY x轴开始纵坐标
     * @param coodinateXStopX x轴结束横坐标
     * @param coodinateXStopY x轴结束纵坐标
     * @param coodinateYStartX y轴开始横坐标
     * @param coodinateYStartY y轴开始纵坐标
     * @param coodinateYStopX y轴结束横坐标
     * @param coodinateYStopY y轴结束纵坐标
     */
    @Override
    protected void onDrawSelfContent(Canvas canvas, int coodinateXStartX, int coodinateXStartY, int coodinateXStopX,
                                     int coodinateXStopY, int coodinateYStartX, int coodinateYStartY, int
                                           coodinateYStopX, int coodinateYStopY) {



        int xDataSize=xCoodinateDatas.size();
        int yDataSize=yCoodinateDatas.size();

        int yCoodinateDistance=Math.abs(coodinateYStopY-coodinateYStartY);//y轴长度
        int xCoordinateDistance=Math.abs(coodinateXStopX-coodinateXStartX);//x轴长度
        int xSpacing=xCoordinateDistance/xDataSize;

        //画柱状图
        int xCoodinateMaxData = xCoodinateDatas.get(xDataSize - 1);//x轴最大数字
        int yCoodinateMaxData = yCoodinateDatas.get(yDataSize - 1) + yCoodinateDatas.get(yDataSize - 1) -
                yCoodinateDatas.get(yDataSize - 2);//y轴最大数字
        int statisicBarHeight = 0;

        paint.setTextAlign(Paint.Align.LEFT);

        if (animateStarted) {//动画是否已经开始

            for (int i = 0, size = contentDatas.size(); i < size; i++) {

                //画柱状
                ContentData contentData = contentDatas.get(i);
                statisicBarHeight = heightValueMap.get(i);
                drawSingleBar(canvas, coodinateXStartX, coodinateXStartY, xCoordinateDistance, xSpacing,
                        xCoodinateMaxData, statisicBarHeight, contentData);

            }
        } else {//动画还没开始
            animateStarted = true;
            for (int i = 0, size = contentDatas.size(); i < size; i++) {

                //画柱状
                ContentData contentData = contentDatas.get(i);
                statisicBarHeight = (int) ((contentData.y * 1.0 / yCoodinateMaxData) * yCoodinateDistance);
                statisicBarHeight = 0 == statisicBarHeight ? (int) dp2 : statisicBarHeight;//柱状高度为0就附上2dp

                ValueAnimator animator = animatorMap.get(i);
                if (null != animator) {
                    if (animator.isRunning()) {
                        animator.cancel();
                    }
                    animator.setIntValues(0, statisicBarHeight);
                }
            }


            for (int i = 0, size = contentDatas.size(); i < size; i++) {
                ValueAnimator animator = animatorMap.get(i);
                if (null != animator) {
                    animator.start();
                }
            }
        }

    }

}


