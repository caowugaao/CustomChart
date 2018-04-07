package com.gx.morgan.chartlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.animation.DecelerateInterpolator;

import com.gx.morgan.chartlib.R;
import com.gx.morgan.chartlib.utils.MathUtil;
import com.gx.morgan.chartlib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * description：柱状图
 * <br>author：caowugao
 * <br>time：2018/3/16 17:56
 */
public class StatisticsBar extends CustomView {

    private int width;
    private int height;
    private Paint paint;

    private int xTextSize;
    private int yTextSize;
    private int xTextColor;
    private int yTextColor;
    private int statisticBarColor;//柱状颜色
    private int statisticBarWidth;//柱状宽度
    private int textCoordinatePadding;//数字与坐标轴的距离
    private Rect textBound;
    private List<Integer> xCoodinateDatas;//x轴上的数字
    private List<Integer> yCoodinateDatas;//y轴上的数字
    private int contentPadding;//内容与view边界的距离
    private int xCoordinateBulgeDistance;//x轴凸起距离
    private int xCoordinateBulgeDirection;//x轴凸起方向
    private int coodinatateColor;//坐标轴颜色
    private int arrowHeight;//箭头高度
    private int arrowWidth;//箭头宽度
    private List<ContentData> contentDatas;//柱状的x、y数据
    private float dp2;
    private String xCoodinateUnitDesc;//x轴单位说明
    private String yCoodinateUnitDesc;//y轴单位说明
    private int unitDescTextSize;
    private int unitDescTextColor;

    private boolean animate = true;//是否需要动画
    private SparseIntArray heightValueMap;
    private SparseArray<ValueAnimator> animatorMap;
    private boolean animateStarted = false;//动画是否已经开始

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


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xTextSize = (int) ViewUtil.sp(context, 16);
        xTextColor = Color.BLACK;
        yTextSize = xTextSize;
        yTextColor = xTextColor;
        statisticBarColor = Color.BLUE;
        statisticBarWidth = (int) ViewUtil.dp2px(context, 10);
        textCoordinatePadding = (int) ViewUtil.dp2px(context, 5);
        textBound = new Rect();
        contentPadding = (int) ViewUtil.dp2px(context, 5);
        xCoordinateBulgeDistance = (int) ViewUtil.dp2px(context, 5);
        xCoordinateBulgeDirection = XCoordinateBulgeDirection.DOWN;
        coodinatateColor = Color.BLACK;

        arrowHeight = (int) ViewUtil.dp2px(context, 5);
        arrowWidth = arrowHeight;

        dp2 = ViewUtil.dp2px(context, 2);
        unitDescTextSize = (int) ViewUtil.sp(context, 14);
        unitDescTextColor = yTextColor;


        xCoodinateUnitDesc = "";
        yCoodinateUnitDesc = "";


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
    protected int contentWidth(int widthMeasureSpec) {
        return (int) ViewUtil.dp2px(getContext(), 300);
    }

    @Override
    protected int contentHeight(int heightMeasureSpec) {
        return (int) ViewUtil.dp2px(getContext(), 350);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDetachedFromWindow() {
        contentDatas.clear();
        heightValueMap.clear();
        animatorMap.clear();
        super.onDetachedFromWindow();

    }

    public void setxTextSize(int xTextSize) {
        if (this.xTextSize != xTextSize) {
            this.xTextSize = xTextSize;
            invalidate();
        }
    }

    public void setyTextSize(int yTextSize) {
        if (this.yTextSize != yTextSize) {
            this.yTextSize = yTextSize;
            invalidate();
        }
    }

    public void setxTextColor(int xTextColor) {
        if (this.xTextColor != xTextColor) {
            this.xTextColor = xTextColor;
            invalidate();
        }
    }

    public void setyTextColor(int yTextColor) {
        if (this.yTextColor != yTextColor) {
            this.yTextColor = yTextColor;
            invalidate();
        }
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

    public void setTextCoordinatePadding(int textCoordinatePadding) {
        if (this.textCoordinatePadding != textCoordinatePadding) {
            this.textCoordinatePadding = textCoordinatePadding;
            invalidate();
        }
    }

    public void setxCoodinateDatas(List<Integer> xCoodinateDatas) {

        checkEmpty(xCoodinateDatas, "xCoodinateDatas");

        this.xCoodinateDatas = xCoodinateDatas;

        if (null == yCoodinateDatas || yCoodinateDatas.isEmpty() || null == contentDatas || contentDatas.isEmpty()) {
            return;
        }
        invalidate();
    }

    public void setyCoodinateDatas(List<Integer> yCoodinateDatas) {
        checkEmpty(yCoodinateDatas, "yCoodinateDatas");
        this.yCoodinateDatas = yCoodinateDatas;
        if (null == xCoodinateDatas || xCoodinateDatas.isEmpty() || null == contentDatas || contentDatas.isEmpty()) {

            return;
        }
        invalidate();
    }

    public void setContentPadding(int contentPadding) {
        if (this.contentPadding != contentPadding) {
            this.contentPadding = contentPadding;
            invalidate();
        }
    }

    public void setxCoordinateBulgeDistance(int xCoordinateBulgeDistance) {
        if (this.xCoordinateBulgeDistance != xCoordinateBulgeDistance) {
            this.xCoordinateBulgeDistance = xCoordinateBulgeDistance;
            invalidate();
        }
    }

    public void setxCoordinateBulgeDirection(@XCoordinateBulgeDirectionType int xCoordinateBulgeDirection) {
        if (this.xCoordinateBulgeDirection != xCoordinateBulgeDirection) {
            this.xCoordinateBulgeDirection = xCoordinateBulgeDirection;
            invalidate();
        }
    }

    public void setCoodinatateColor(int coodinatateColor) {
        if (this.coodinatateColor != coodinatateColor) {
            this.coodinatateColor = coodinatateColor;
            invalidate();
        }
    }

    public void setContentDatas(List<ContentData> contentDatas) {
        checkEmpty(contentDatas, "contentDatas");

        this.contentDatas = contentDatas;
        initAnimator(contentDatas.size());
        if (null == xCoodinateDatas || xCoodinateDatas.isEmpty() || null == yCoodinateDatas || yCoodinateDatas
                .isEmpty()) {
            return;
        }
        invalidate();
    }

    public void setFullData(List<Integer> xCoodinateDatas, List<Integer> yCoodinateDatas, List<ContentData>
            contentDatas) {


        this.xCoodinateDatas = xCoodinateDatas;
        this.yCoodinateDatas = yCoodinateDatas;
        this.contentDatas = contentDatas;

        checkEmpty(xCoodinateDatas, "xCoodinateDatas");
        checkEmpty(yCoodinateDatas, "yCoodinateDatas");
        checkEmpty(contentDatas, "contentDatas");
        initAnimator(contentDatas.size());

        invalidate();
    }

    private <T> void checkEmpty(List<T> datas, String target) {
        if (null == datas || datas.isEmpty()) {
            throw new NullPointerException(target + " cannor be NULL or empty");
        }
    }

    public void setxCoodinateUnitDesc(String xCoodinateUnitDesc) {
        if (!this.xCoodinateUnitDesc.equals(xCoodinateUnitDesc)) {
            this.xCoodinateUnitDesc = xCoodinateUnitDesc;
            invalidate();
        }
    }

    public void setyCoodinateUnitDesc(String yCoodinateUnitDesc) {
        if (!this.yCoodinateUnitDesc.equals(yCoodinateUnitDesc)) {
            this.yCoodinateUnitDesc = yCoodinateUnitDesc;
            invalidate();
        }
    }

    public void setUnitDescTextSize(int unitDescTextSize) {
        if (this.unitDescTextSize != unitDescTextSize) {
            this.unitDescTextSize = unitDescTextSize;
            invalidate();
        }
    }

    public void setUnitDescTextColor(int unitDescTextColor) {
        if (this.unitDescTextColor != unitDescTextColor) {
            this.unitDescTextColor = unitDescTextColor;
            invalidate();
        }
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
        if (animate) {
            if (null != contentDatas && !contentDatas.isEmpty()) {
                initAnimator(contentDatas.size());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStatisticBar(canvas);
    }

    private void drawStatisticBar(Canvas canvas) {

        paint.reset();
        paint.setTextSize(yTextSize);
        float maxTextWidth = getMaxTextWidth(yCoodinateDatas, paint);

        paint.setTextSize(xTextSize);

        //画x轴
        paint.setColor(coodinatateColor);
        int xDataSize = xCoodinateDatas.size();
        float textHeight = getTextHeight(paint);

        int coodinateXStartX = (int) (getPaddingLeft() + contentPadding + maxTextWidth + textCoordinatePadding);
        int coodinateXStartY = (int) (height - getPaddingBottom() - contentPadding - textHeight -
                textCoordinatePadding);
        int coodinateXStopX = width - getPaddingRight() - contentPadding;
        int coodinateXStopY = coodinateXStartY;
        canvas.drawLine(coodinateXStartX, coodinateXStartY, coodinateXStopX, coodinateXStopY, paint);


        //画x轴数字
        int xCoordinateDistance = coodinateXStopX - coodinateXStartX;
        int xSpacing = xCoordinateDistance / xDataSize;


        int textStartX = 0;
        int textStartY = 0;
        int paddingBottom = getPaddingBottom();

        int xCoodinateBuglyStartX = 0;
        int xCoodinateBuglyStartY = 0;
        int xCoodinateBuglyStopX = 0;
        int xCoodinateBuglyStopY = 0;


        int arrowUpX = 0;//x轴三角形x坐标
        int arrowUpY = 0;//x轴三角形y坐标
        for (int i = 0, size = xDataSize; i < size; i++) {
            //画x轴数字
            paint.setColor(xTextColor);
            String text = String.valueOf(xCoodinateDatas.get(i));
            paint.getTextBounds(text, 0, text.length(), textBound);
            textStartX = coodinateXStartX + i * xSpacing + xSpacing / 2 - textBound.width() / 2;
            textStartY = height - paddingBottom - contentPadding;
            canvas.drawText(text, textStartX, textStartY, paint);

            //画x轴凸起部分
            paint.setColor(coodinatateColor);
            xCoodinateBuglyStartX = textStartX + textBound.width() / 2 + xSpacing / 2;
            xCoodinateBuglyStartY = coodinateXStartY;
            xCoodinateBuglyStopX = xCoodinateBuglyStartX;
            if (xCoordinateBulgeDirection == XCoordinateBulgeDirection.UP) {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY - xCoordinateBulgeDistance;
            } else if (xCoordinateBulgeDirection == XCoordinateBulgeDirection.DOWN) {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY + xCoordinateBulgeDistance;
            } else {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY + xCoordinateBulgeDistance;
            }

            if (i == size - 1) {//最后一个就画箭头
                arrowUpX = coodinateXStopX - arrowWidth;
                arrowUpY = coodinateXStartY - arrowHeight;
                paint.setStrokeJoin(Paint.Join.MITER);//设置拐角为尖的
                canvas.drawLine(coodinateXStopX, coodinateXStopY, arrowUpX, arrowUpY, paint);

                int arrowDownX = arrowUpX;
                int arrowDownY = coodinateXStartY + arrowHeight;
                canvas.drawLine(coodinateXStopX, coodinateXStopY, arrowDownX, arrowDownY, paint);


            } else {
                canvas.drawLine(xCoodinateBuglyStartX, xCoodinateBuglyStartY, xCoodinateBuglyStopX,
                        xCoodinateBuglyStopY, paint);
            }
        }


        int paddingTop = getPaddingTop();

        //画y轴
        int coodinateYStartX = coodinateXStartX;
        int coodinateYStartY = coodinateXStartY;
        int coodinateYStopX = coodinateYStartX;
        int coodinateYStopY = paddingTop + contentPadding;
        canvas.drawLine(coodinateYStartX, coodinateYStartY, coodinateYStopX, coodinateYStopY, paint);

        //画y轴箭头
        int arrowLeftX = coodinateYStopX - arrowHeight;
        int arrowLeftY = coodinateYStopY + arrowWidth;
        int arrowRightX = coodinateYStopX + arrowHeight;
        int arrowRightY = arrowLeftY;
        canvas.drawLine(coodinateYStopX, coodinateYStopY, arrowLeftX, arrowLeftY, paint);
        canvas.drawLine(coodinateYStopX, coodinateYStopY, arrowRightX, arrowRightY, paint);

        //画y轴单位说明
        if (!TextUtils.isEmpty(yCoodinateUnitDesc)) {
            paint.setColor(unitDescTextColor);
            paint.setTextSize(unitDescTextSize);
            paint.getTextBounds(yCoodinateUnitDesc, 0, yCoodinateUnitDesc.length(), textBound);
            textStartX = coodinateXStartX + textCoordinatePadding;
            textStartY = arrowLeftY + textBound.height();
            canvas.drawText(yCoodinateUnitDesc, textStartX, textStartY, paint);
        }


        int paddingLeft = getPaddingLeft();
        int yDataSize = yCoodinateDatas.size();
        int yCoodinateDistance = coodinateYStartY - coodinateYStopY;
        int ySpacing = yCoodinateDistance / yDataSize;

        int contentLineStartX = 0;
        int contentLineStartY = 0;
        int contentLineStopX = 0;
        int contentLineStopY = 0;

        paint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0, size = yDataSize; i < size; i++) {

            //画y轴数字
            paint.setTextSize(yTextSize);
            paint.setColor(yTextColor);
            String text = String.valueOf(yCoodinateDatas.get(i));
            paint.getTextBounds(text, 0, text.length(), textBound);
            textStartX = (int) (paddingLeft + contentPadding + maxTextWidth);
            textStartY = coodinateYStartY - i * ySpacing + textBound.height() / 2;
            canvas.drawText(text, textStartX, textStartY, paint);


            //画内容横线
            paint.setColor(coodinatateColor);
            contentLineStartX = coodinateXStartX;
            contentLineStartY = coodinateYStartY - i * ySpacing;
            contentLineStopX = coodinateXStopX;
            contentLineStopY = contentLineStartY;

            canvas.drawLine(contentLineStartX, contentLineStartY, contentLineStopX, contentLineStopY, paint);
        }


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

        //画x轴单位说明
        if (!TextUtils.isEmpty(xCoodinateUnitDesc)) {
            paint.setTextSize(unitDescTextSize);
            paint.setColor(unitDescTextColor);
            paint.getTextBounds(xCoodinateUnitDesc, 0, xCoodinateUnitDesc.length(), textBound);
            textStartX = arrowUpX - textBound.width();
            textStartY = arrowUpY;
            canvas.drawText(xCoodinateUnitDesc, textStartX, textStartY, paint);
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

    private float getMaxTextWidth(List coodinateDatas, Paint paint) {
        float[] widths = new float[coodinateDatas.size()];
        for (int i = 0, length = coodinateDatas.size(); i < length; i++) {
            float textWidth = getTextWidth(String.valueOf(coodinateDatas.get(i)), paint);
            widths[i] = textWidth;
        }
        return MathUtil.max(widths);
    }

    private static float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    private static float getTextHeight(Paint paint) {
        return (-paint.ascent() + paint.descent());
    }
}


