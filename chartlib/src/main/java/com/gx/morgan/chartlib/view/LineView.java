package com.gx.morgan.chartlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.animation.LinearInterpolator;

import com.gx.morgan.chartlib.R;
import com.gx.morgan.chartlib.utils.AnimatorUtil;
import com.gx.morgan.chartlib.utils.CommonUtil;
import com.gx.morgan.chartlib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * description：折线图
 * <br>author：caowugao
 * <br>time：2018/4/15 14:12
 */
public class LineView extends BaseCoordinateView {

    private float pointRadius;//拐点半径，如果pointType是方形或者圆角方形，则方形长度等于2倍pointRadius，
    private int lineColor;//连线颜色
    private int pointType;//拐点类型，圆形、方形、圆角方形

    private float contentTextSize;//内容文字大小
    private int contentTextColor;//内容文字字体颜色

    private float roundDegree;//方形拐点角度，即pointType为圆角方形才会有作用
    private RectF pointRectF;
    private float lineWidth;//连线宽度
    private SparseIntArray latterAnimationHeightValueMap;
    private SparseArray<ValueAnimator> latterAnimatorMap;

    private ValueAnimator frontAnimator;//前一段动画
    private float animateCenterPosition;//前一段动画结尾的位置，后一段动画开始的位置,这个值只是contentData的y而已
    private float animateCenterPositionInView;//前一段动画结尾的位置，后一段动画开始的位置,这个值是在View中的位置
    private boolean frontAnimationRunning;//前一段动画是否已经运行
    private boolean frontAnimationJustEnd;//前一段动画是否刚结束
    private int frontAnimationHeightValue;//前一段动画高度变化的值
    private boolean latterAnimationStarted;//后一段动画是否开始
    private int maxContentDataY;//最大contentData.y
    private int minContentDataY;//最小contentData.y
    private float prePointX;
    private float prePointY;


    public static class PointType {
        public static final int CIRCLE = 1;
        public static final int RECT = 2;
        public static final int ROUNDRECT = 3;//圆角方形
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({PointType.CIRCLE, PointType.RECT, PointType.ROUNDRECT})
    public @interface LinePointType {
    }

    public LineView(Context context) {
        super(context);
        init(context, null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        pointRadius = ViewUtil.dp2px(context, 5);
        contentTextSize = ViewUtil.sp(context, 12);
        contentTextColor = Color.RED;
        lineColor = Color.BLUE;
        pointType = PointType.ROUNDRECT;
        roundDegree = ViewUtil.dp2px(context, 2);
        lineWidth = ViewUtil.dp2px(context, 1);
        pointRectF = new RectF();

        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineView);
        }
        xTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_xTextSize, xTextSize);
        yTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_yTextSize, yTextSize);
        xTextColor = ViewUtil.optColor(typedArray, R.styleable.LineView_xTextColor, xTextColor);
        yTextColor = ViewUtil.optColor(typedArray, R.styleable.LineView_yTextColor, yTextColor);
        textCoordinatePadding = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_textCoordinatePadding,
                textCoordinatePadding);
        contentPadding = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_contentPadding, contentPadding);
        xCoordinateBulgeDistance = ViewUtil.optPixelSize(typedArray, R.styleable
                .LineView_xCoordinateBulgeDistance, xCoordinateBulgeDistance);
        xCoordinateBulgeDirection = ViewUtil.optInt(typedArray, R.styleable.LineView_xCoordinateBulgeDirection,
                XCoordinateBulgeDirection.DOWN);
        coordinatateColor = ViewUtil.optColor(typedArray, R.styleable.LineView_coordinatateColor, coordinatateColor);
        xCoordinateUnitDesc = ViewUtil.optString(typedArray, R.styleable.LineView_xCoordinateUnitDesc,
                xCoordinateUnitDesc);
        yCoordinateUnitDesc = ViewUtil.optString(typedArray, R.styleable.LineView_yCoordinateUnitDesc,
                yCoordinateUnitDesc);
        unitDescTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_unitDescTextSize,
                unitDescTextSize);
        unitDescTextColor = ViewUtil.optColor(typedArray, R.styleable.LineView_unitDescTextColor,
                unitDescTextColor);
        needAnimated = ViewUtil.optBoolean(typedArray, R.styleable.LineView_needAnimated, true);


        pointRadius = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_pointRadius, pointRadius);
        lineColor = ViewUtil.optColor(typedArray, R.styleable.LineView_lineColor, lineColor);
        pointType = ViewUtil.optInt(typedArray, R.styleable.LineView_pointType, pointType);
        contentTextSize = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_contentTextSize, contentTextSize);
        contentTextColor = ViewUtil.optColor(typedArray, R.styleable.LineView_contentTextColor, contentTextColor);
        roundDegree = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_roundDegree, roundDegree);
        lineWidth = ViewUtil.optPixelSize(typedArray, R.styleable.LineView_lineWidth, lineWidth);


        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    public void setPointRadius(float pointRadius) {
        if (this.pointRadius != pointRadius) {
            this.pointRadius = pointRadius;
            invalidate();
        }
    }

    public void setLineColor(@ColorInt int lineColor) {
        if (this.lineColor != lineColor) {
            this.lineColor = lineColor;
            invalidate();
        }
    }

    public void setPointType(@LinePointType int pointType) {
        if (this.pointType != pointType) {
            this.pointType = pointType;
            invalidate();
        }
    }

    public void setContentTextSize(float contentTextSize) {
        if (this.contentTextSize != contentTextSize) {
            this.contentTextSize = contentTextSize;
            invalidate();
        }
    }

    public void setContentTextColor(@ColorInt int contentTextColor) {
        if (this.contentTextColor != contentTextColor) {
            this.contentTextColor = contentTextColor;
            invalidate();
        }
    }

    public void setLineWidth(float lineWidth) {
        if (this.lineWidth != lineWidth) {
            this.lineWidth = lineWidth;
            invalidate();
        }
    }

    public void setRoundDegree(float roundDegree) {
        if (this.roundDegree != roundDegree) {
            this.roundDegree = roundDegree;
            invalidate();
        }
    }

    @Override
    public void initAnimator(boolean needAnimated, List<ContentData> contentDatas) {
        if (needAnimated) {
            if (CommonUtil.checkCollectionEmpty(contentDatas)) {
                return;
            }
            calculateAnimatePosition(contentDatas);
            if (animateCenterPosition <= 0) {
                return;
            }
            AnimatorUtil.cancelAnimator(frontAnimator);
            if (null == frontAnimator) {
                frontAnimator = ValueAnimator.ofInt();
            }
            frontAnimationJustEnd = false;
            frontAnimator.setDuration(300);
            frontAnimator.setInterpolator(new LinearInterpolator());
            frontAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    frontAnimationHeightValue = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            frontAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    frontAnimationRunning = false;
                    frontAnimationHeightValue = 0;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    frontAnimationRunning = false;
                    frontAnimationJustEnd = true;
                    frontAnimationHeightValue = 0;
                }
            });


            AnimatorUtil.cancelAnimator(latterAnimatorMap);
            if (null == latterAnimationHeightValueMap) {
                latterAnimationHeightValueMap = new SparseIntArray();
            }
            if (null == latterAnimatorMap) {
                latterAnimatorMap = new SparseArray<>();
            }
            latterAnimationHeightValueMap.clear();
            latterAnimatorMap.clear();

            final int contentSize = contentDatas.size();
            for (int i = 0; i < contentSize; i++) {
                ValueAnimator animator = ValueAnimator.ofInt();
                animator.setDuration(500);
                animator.setInterpolator(new LinearInterpolator());
                final int finalI = i;
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int heightValue = (int) animation.getAnimatedValue();
                        latterAnimationHeightValueMap.put(finalI, heightValue);
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (finalI == contentSize - 1) {
                            animateRunning = false;
                            frontAnimationJustEnd = false;
                            latterAnimationStarted = true;
                        }
                    }
                });
                latterAnimatorMap.put(finalI, animator);
            }
        }

    }


    @Override
    protected void onDetachedFromWindow() {

        AnimatorUtil.cancelAnimator(frontAnimator);
        AnimatorUtil.cancelAnimator(latterAnimatorMap);

        if (null != latterAnimationHeightValueMap) {
            latterAnimationHeightValueMap.clear();
        }

        if (null != latterAnimatorMap) {
            latterAnimatorMap.clear();
        }


        super.onDetachedFromWindow();
    }

    private void calculateAnimatePosition(List<ContentData> contentDatas) {
        int max = contentDatas.get(0).y;
        int min = contentDatas.get(0).y;
        for (int i = 1, size = contentDatas.size(); i < size; i++) {
            ContentData contentData = contentDatas.get(i);
            int y = contentData.y;
            if (max < y) {
                max = y;
            }

            if (min > y) {
                min = y;
            }
        }

        animateCenterPosition = min + (max - min) * 1.0f / 2;
        maxContentDataY = max;
        minContentDataY = min;
    }

    @Override
    protected boolean isXCoordinateDataInBulge() {
        return true;
    }

    @Override
    protected void onDrawSelfContent(Canvas canvas, float coordinateXStartX, float coordinateXStartY
            , float coordinateXStopX, float coordinateXStopY, float coordinateYStartX
            , float coordinateYStartY, float coordinateYStopX, float coordinateYStopY) {

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        float oldStrokeWidth = paint.getStrokeWidth();

        if(!needAnimated){
            drawNoAnimation(canvas, coordinateXStartX, coordinateXStopX, coordinateYStartY, coordinateYStopY, oldStrokeWidth);
        }
        else {
            drawHasAnimtion(canvas, coordinateXStartX, coordinateXStopX, coordinateYStartY, coordinateYStopY, oldStrokeWidth);
        }
    }

    private void drawHasAnimtion(Canvas canvas, float coordinateXStartX, float coordinateXStopX, float
            coordinateYStartY, float coordinateYStopY, float oldStrokeWidth) {
        if (!frontAnimationRunning) {//前一段动画不在运行中

            if (frontAnimationJustEnd) {//前一段动画刚结束，开启后一段动画
                frontAnimationJustEnd = false;
                startLatterAnimator();
                return;
            }

            if (!latterAnimationStarted) {//第一次或者重置content数据的时候，就初始化动画
                initAnimator(coordinateYStartY,coordinateYStopY);
            } else {//前一段动画停止，后一段动画运行中，就通过后一段动画绘制
                drawByLatterAnimator( canvas,  coordinateXStartX,  coordinateXStopX, coordinateYStartY,  coordinateYStopY,  oldStrokeWidth);
            }
        } else {//前一段动画运行中
            if (!latterAnimationStarted) {//还没开始后一段动画
                drawByFrontAnimator(canvas,  coordinateXStartX,  coordinateXStopX, coordinateYStartY,  coordinateYStopY,   oldStrokeWidth);

            }
        }
    }

    private void drawNoAnimation(Canvas canvas, float coordinateXStartX, float coordinateXStopX, float
            coordinateYStartY, float coordinateYStopY, float oldStrokeWidth) {
        for (int i = 0,size=contentDatas.size(); i <size ; i++) {
            drawSingleContentDataByHeightValue(-1, i,oldStrokeWidth, canvas,coordinateXStartX,coordinateXStopX,coordinateYStartY,coordinateYStopY);
        }
    }

    /**
     * 通过前一段动画绘制
     * @param canvas
     * @param coordinateXStartX
     * @param coordinateXStopX
     * @param coordinateYStartY
     * @param coordinateYStopY
     * @param oldStrokeWidth
     */
    private void drawByFrontAnimator(Canvas canvas, float coordinateXStartX, float coordinateXStopX, float
            coordinateYStartY, float coordinateYStopY, float oldStrokeWidth) {

        for (int i = 0, size = contentDatas.size(); i < size; i++) {
            drawSingleContentDataByHeightValue( frontAnimationHeightValue, i, oldStrokeWidth,  canvas,  coordinateXStartX,coordinateXStopX,coordinateYStartY,coordinateYStopY);
        }
    }

    /**
     * 通过后一段动画绘制
     * @param canvas
     * @param coordinateXStartX
     * @param coordinateXStopX
     * @param coordinateYStartY
     * @param coordinateYStopY
     * @param oldStrokeWidth
     */
    private void drawByLatterAnimator(Canvas canvas, float coordinateXStartX, float coordinateXStopX, float
            coordinateYStartY, float coordinateYStopY, float oldStrokeWidth) {

        for (int i = 0, size = contentDatas.size(); i < size; i++) {
            int latterAnimationHeightValue = latterAnimationHeightValueMap.get(i);
            drawSingleContentDataByHeightValue( latterAnimationHeightValue,  i
            ,  oldStrokeWidth,  canvas,  coordinateXStartX
            ,  coordinateXStopX,  coordinateYStartY
            ,  coordinateYStopY);
        }
    }

    private void initAnimator(float coordinateYStartY,float coordinateYStopY) {

        float yCoordinateDistance = Math.abs(coordinateYStartY - coordinateYStopY);//y轴长度
        int yDataSize = yCoordinateDatas.size();
        int firstYCoordinateData = yCoordinateDatas.get(0);
        int yCoodinateDataRangLength = yCoordinateDatas.get(yDataSize - 1) + yCoordinateDatas.get(yDataSize - 1) -
                yCoordinateDatas.get(yDataSize - 2) - firstYCoordinateData;//y轴数据区间长度


        animateRunning = true;

        animateCenterPositionInView = coordinateYStartY - (float) (yCoordinateDistance *
                (animateCenterPosition - firstYCoordinateData) * 1.0 /
                yCoodinateDataRangLength);
        float frontAnimateStartValue = coordinateYStartY - (float) (yCoordinateDistance * (minContentDataY -
                firstYCoordinateData) * 1.0 /
                yCoodinateDataRangLength);


        //初始化前一段动画并启动前一段动画
        frontAnimationRunning = true;
        AnimatorUtil.cancelAnimator(frontAnimator);
        if (null != frontAnimator) {
            frontAnimator.setIntValues((int) frontAnimateStartValue, (int) animateCenterPositionInView);
            frontAnimator.start();
        }

        initLatterAnimator(coordinateYStartY, yCoordinateDistance, firstYCoordinateData,
                yCoodinateDataRangLength);//初始化后一段动画
    }

    private void drawSingleContentDataByHeightValue(float heightValue, int contentDatasIndex
            , float oldStrokeWidth, Canvas canvas, float coordinateXStartX
            , float coordinateXStopX, float coordinateYStartY
            , float coordinateYStopY) {

        float xCoordinateDistance = Math.abs(coordinateXStartX - coordinateXStopX);//x轴长度
        float yCoordinateDistance = Math.abs(coordinateYStartY - coordinateYStopY);//y轴长度


        int firstXCoordinateData = xCoordinateDatas.get(0);
        int xDataSize = xCoordinateDatas.size();
        int xCoordinateDataRangeLength = xCoordinateDatas.get(xDataSize - 1) + xCoordinateDatas.get(xDataSize - 1) -
                xCoordinateDatas.get(xDataSize - 2) - firstXCoordinateData;//x轴数据区间长度

        int yDataSize = yCoordinateDatas.size();
        int firstYCoordinateData = yCoordinateDatas.get(0);
        int yCoodinateDataRangLength = yCoordinateDatas.get(yDataSize - 1) + yCoordinateDatas.get(yDataSize - 1) -
                yCoordinateDatas.get(yDataSize - 2) - firstYCoordinateData;//y轴数据区间长度


        ContentData contentData = contentDatas.get(contentDatasIndex);
       float pointX = (float) (coordinateXStartX + xCoordinateDistance * (contentData.x - firstXCoordinateData) * 1.0 /
                xCoordinateDataRangeLength);
        float pointY=0;
        if (heightValue <= 0) {
            pointY = coordinateYStartY - (float) (yCoordinateDistance * (contentData.y - firstYCoordinateData) * 1.0 /
                    yCoodinateDataRangLength);
        } else {
            pointY = heightValue;
        }

        if (0 == contentDatasIndex) {
            prePointX = pointX;
            prePointY = pointY;
        } else {
            paint.setStrokeWidth(oldStrokeWidth);
            paint.setColor(lineColor);
            float startX = prePointX;
            float startY = prePointY;
            float stopX = pointX;
            float stopY = pointY;
            canvas.drawLine(startX, startY, stopX, stopY, paint);

            prePointX = pointX;
            prePointY = pointY;
        }

        drawInflectionPoint(canvas, pointX, pointY);//画拐点
        drawConentText(canvas, pointX, pointY, contentData);
    }


    private void startLatterAnimator() {
        if (null != latterAnimatorMap) {
            for (int i = 0, size = latterAnimatorMap.size(); i < size; i++) {
                latterAnimatorMap.get(i).start();
            }
            latterAnimationStarted = true;
        }
    }

    /**
     * 初始化后一段动画
     *
     * @param coordinateYStartY
     * @param yCoordinateDistance
     * @param firstYCoordinateData
     * @param yCoodinateDataRangLength
     */
    private void initLatterAnimator(float coordinateYStartY, float yCoordinateDistance, int firstYCoordinateData, int
            yCoodinateDataRangLength) {
        for (int i = 0, size = contentDatas.size(),endLatterValue; i < size; i++) {
            ContentData contentData = contentDatas.get(i);
            endLatterValue = (int) (coordinateYStartY - (float) (yCoordinateDistance * (contentData.y - firstYCoordinateData) * 1.0 /
                                yCoodinateDataRangLength));
            ValueAnimator animator = latterAnimatorMap.get(i);
            if (null != animator) {
                if (animator.isRunning()) {
                    animator.cancel();
                }
                animator.setIntValues((int) animateCenterPositionInView, endLatterValue);
            }
        }
    }

    private void drawConentText(Canvas canvas, float pointX, float pointY, ContentData contentData) {
        paint.setTextSize(contentTextSize);
        paint.setColor(contentTextColor);
        String text = String.valueOf(contentData.y);
        paint.getTextBounds(text, 0, text.length(), textBound);

        float x = pointX + textBound.width() / 2;
        float y = pointY - pointRadius - textCoordinatePadding;
        canvas.drawText(text, x, y, paint);
    }

    /**
     * 画拐点
     *
     * @param canvas
     * @param pointX
     * @param pointY
     */
    private void drawInflectionPoint(Canvas canvas, float pointX, float pointY) {
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        switch (pointType) {
            case PointType.CIRCLE:
                float cx = pointX;
                float cy = pointY;
                canvas.drawCircle(cx, cy, pointRadius, paint);

                break;
            case PointType.RECT:
                float left = pointX - pointRadius;
                float top = pointY - pointRadius;
                float right = pointX + pointRadius;
                float bottom = pointY + pointRadius;
                canvas.drawRect(left, top, right, bottom, paint);
                break;
            case PointType.ROUNDRECT://圆角方形
                float rx = roundDegree;
                float ry = roundDegree;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    left = pointX - pointRadius;
                    top = pointY - pointRadius;
                    right = pointX + pointRadius;
                    bottom = pointY + pointRadius;
                    canvas.drawRoundRect(left, top, right, bottom, rx, ry, paint);
                } else {
                    pointRectF.left = pointX - pointRadius;
                    pointRectF.top = pointY - pointRadius;
                    pointRectF.right = pointX + pointRadius;
                    pointRectF.bottom = pointY + pointRadius;
                    canvas.drawRoundRect(pointRectF, rx, ry, paint);
                }
                break;
        }

    }
}
