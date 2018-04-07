package com.gx.morgan.chartlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.animation.DecelerateInterpolator;

import com.gx.morgan.chartlib.utils.MathUtil;
import com.gx.morgan.chartlib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description：条形图
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
    private boolean animateStarted=false;//动画是否已经开始

    public static class XCoordinateBulgeDirection {
        private XCoordinateBulgeDirection() {
        }

        public static final int UP = 1;
        public static final int DOWN = 2;
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


        xCoodinateUnitDesc = "月份";
        yCoodinateUnitDesc = "台";


        int xDataSize = 8;
        xCoodinateDatas = new ArrayList<>(xDataSize);
        for (int i = 0; i < xDataSize; i++) {
            xCoodinateDatas.add(i + 1);
        }


        int yDataSize = 7;
        yCoodinateDatas = new ArrayList<>(yDataSize);
        for (int i = 0; i < yDataSize; i++) {
            yCoodinateDatas.add(8 * i);
        }


        final int contentDataSize = 8;
        contentDatas = new ArrayList<>(contentDataSize);
        int[] datas = new int[]{24, 48, 40, 8, 0, 16, 32, 48};
        for (int i = 0; i < contentDataSize; i++) {
            ContentData contentData = new ContentData(i + 1, datas[i]);
            contentDatas.add(contentData);
        }


        if(animate){

            animatorMap = new SparseArray<>();
            heightValueMap = new SparseIntArray();

            for (int i = 0; i <contentDataSize ; i++) {
                ValueAnimator animator = ValueAnimator.ofInt();
                animator.setDuration(500);
                animator.setInterpolator(new DecelerateInterpolator());
                final int finalI = i;
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int baeHeightValue = (int) valueAnimator.getAnimatedValue();
                        heightValueMap.put(finalI,baeHeightValue);
                        invalidate();
                    }
                });
                final int finalI1 = i;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        if(finalI1 ==contentDataSize-1){
                            animateStarted=false;
                        }
                    }
                });
                animatorMap.put(i,animator);
            }
        }
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

    public void setBarWidth(int barWidth) {
//        param.setBarWidth(barWidth);
        invalidate();
    }

//    public void setDatas(List<AbstractCoordinate.Data> datas) {
////        param.setDatas(datas);
//        invalidate();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.GRAY);
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

        if(animateStarted){//动画是否已经开始

            for (int i = 0, size = contentDatas.size(); i < size; i++) {

                //画柱状
                ContentData contentData = contentDatas.get(i);
//                statisicBarHeight = (int) ((contentData.y * 1.0 / yCoodinateMaxData) * yCoodinateDistance);
//                statisicBarHeight = 0 == statisicBarHeight ? (int) dp2 : statisicBarHeight;//柱状高度为0就附上2dp

                statisicBarHeight = heightValueMap.get(i);
                drawSingleBar(canvas, coodinateXStartX, coodinateXStartY, xCoordinateDistance, xSpacing,
                        xCoodinateMaxData, statisicBarHeight, contentData);

            }
        }
        else {//动画还没开始
            animateStarted=true;
            for (int i = 0, size = contentDatas.size(); i < size; i++) {

                //画柱状
                ContentData contentData = contentDatas.get(i);
                statisicBarHeight = (int) ((contentData.y * 1.0 / yCoodinateMaxData) * yCoodinateDistance);
                statisicBarHeight = 0 == statisicBarHeight ? (int) dp2 : statisicBarHeight;//柱状高度为0就附上2dp

                ValueAnimator animator = animatorMap.get(i);
                if(null!=animator){
                    if(animator.isRunning()){
                        animator.cancel();
                    }
                    animator.setIntValues(0,statisicBarHeight);
                }
            }


            for (int i = 0, size = contentDatas.size(); i < size; i++) {
                ValueAnimator animator = animatorMap.get(i);
                if(null!=animator){
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

//    private void drawSingleBar(final Canvas canvas, final int coodinateXStartX, final int coodinateXStartY, final
// int xCoordinateDistance,
//                               final int xSpacing, final int xCoodinateMaxData, final int statisicBarHeight, final
// ContentData contentData) {
//
//        if(!animate){//没有动画
//            paint.setStyle(Paint.Style.FILL);
//            paint.setColor(statisticBarColor);
//            int textStartX;
//            int textStartY;
//            int left= (int) (coodinateXStartX+(contentData.x*1.0/xCoodinateMaxData)
// *xCoordinateDistance-xSpacing/2-statisticBarWidth/2);
//            int top=coodinateXStartY-statisicBarHeight;
//            int right=left+statisticBarWidth;
//            int bottom=coodinateXStartY;
//            canvas.drawRect( left,  top,  right,  bottom,paint);
//
//
//            //画柱状图顶部的数字
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setTextSize(yTextSize);
//            paint.setColor(yTextColor);
//            String text=String.valueOf(contentData.y);
//            paint.getTextBounds(text,0,text.length(),textBound);
//            int textWidth=textBound.width();
//            textStartX=left+statisticBarWidth/2-textWidth/2;
//            textStartY=top-textCoordinatePadding;
//            canvas.drawText(text,textStartX,textStartY,paint);
//        }
//        else {//动画
//            ValueAnimator animator=ValueAnimator.ofInt(0,statisicBarHeight/6,statisicBarHeight/3,statisicBarHeight);
//            animator.setDuration(500);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    int value = (int) valueAnimator.getAnimatedValue();
//                    drawSingleBarWithAnimate( canvas,  coodinateXStartX,  coodinateXStartY,  xCoordinateDistance,
//                     xSpacing,  xCoodinateMaxData,  value,  contentData);
//                }
//            });
//            animator.start();
//        }
//    }

//    private void drawSingleBarWithAnimate(Canvas canvas, int coodinateXStartX, int coodinateXStartY, int
// xCoordinateDistance,
//                                          int xSpacing, int xCoodinateMaxData, int barHeightAnimateValue,
// ContentData contentData) {
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(statisticBarColor);
//        int textStartX;
//        int textStartY;
//        int left= (int) (coodinateXStartX+(contentData.x*1.0/xCoodinateMaxData)
// *xCoordinateDistance-xSpacing/2-statisticBarWidth/2);
//        int top=coodinateXStartY-barHeightAnimateValue;
//        int right=left+statisticBarWidth;
//        int bottom=coodinateXStartY;
//        canvas.drawRect( left,  top,  right,  bottom,paint);
//
//
//        //画柱状图顶部的数字
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setTextSize(yTextSize);
//        paint.setColor(yTextColor);
//        String text=String.valueOf(contentData.y);
//        paint.getTextBounds(text,0,text.length(),textBound);
//        int textWidth=textBound.width();
//        textStartX=left+statisticBarWidth/2-textWidth/2;
//        textStartY=top-textCoordinatePadding;
//        canvas.drawText(text,textStartX,textStartY,paint);
//    }

    private float getMaxTextWidth(List coodinateDatas, Paint paint) {
        float[] widths = new float[coodinateDatas.size()];
        for (int i = 0, length = coodinateDatas.size(); i < length; i++) {
            float textWidth = getTextWidth(String.valueOf(coodinateDatas.get(i)), paint);
            widths[i] = textWidth;
        }
        return MathUtil.max(widths);
    }

    public static float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    public static float getTextHeight(Paint paint) {
        return (-paint.ascent() + paint.descent());
    }
}


