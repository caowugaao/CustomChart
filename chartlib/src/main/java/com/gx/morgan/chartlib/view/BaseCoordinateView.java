package com.gx.morgan.chartlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.gx.morgan.chartlib.utils.MathUtil;
import com.gx.morgan.chartlib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * description：坐标基类view
 * <br>author：caowugao
 * <br>time：2018/4/15 14:35
 */
public abstract class BaseCoordinateView extends CustomView {

    protected int width;
    protected int height;
    protected Paint paint;
    protected int xTextSize;
    protected int yTextSize;
    protected int xTextColor;
    protected int yTextColor;

    protected int textCoordinatePadding;//数字与坐标轴的距离
    protected Rect textBound;
    protected List<Integer> xCoodinateDatas;//x轴上的数字
    protected List<Integer> yCoodinateDatas;//y轴上的数字
    protected int contentPadding;//内容与view边界的距离
    protected int xCoordinateBulgeDistance;//x轴凸起距离
    protected int xCoordinateBulgeDirection;//x轴凸起方向
    protected int coodinatateColor;//坐标轴颜色
    protected int arrowHeight;//箭头高度
    protected int arrowWidth;//箭头宽度
    protected List<ContentData> contentDatas;//内容x、y数据
    protected String xCoodinateUnitDesc;//x轴单位说明
    protected String yCoodinateUnitDesc;//y轴单位说明
    protected int unitDescTextSize;
    protected int unitDescTextColor;

    protected boolean animate = true;//是否需要动画
    protected boolean animateStarted = false;//动画是否已经开始

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

    public BaseCoordinateView(Context context) {
        super(context);
        init(context);
    }

    public BaseCoordinateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseCoordinateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseCoordinateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xTextSize = (int) ViewUtil.sp(context, 16);
        xTextColor = Color.BLACK;
        yTextSize = xTextSize;
        yTextColor = xTextColor;
        textCoordinatePadding = (int) ViewUtil.dp2px(context, 5);
        textBound = new Rect();
        contentPadding = (int) ViewUtil.dp2px(context, 5);
        xCoordinateBulgeDistance = (int) ViewUtil.dp2px(context, 5);
        xCoordinateBulgeDirection = XCoordinateBulgeDirection.DOWN;
        coodinatateColor = Color.BLACK;

        arrowHeight = (int) ViewUtil.dp2px(context, 5);
        arrowWidth = arrowHeight;

        unitDescTextSize = (int) ViewUtil.sp(context, 14);
        unitDescTextColor = yTextColor;


        xCoodinateUnitDesc = "";
        yCoodinateUnitDesc = "";
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
        initAnimator(animate, contentDatas);
        if (null == xCoodinateDatas || xCoodinateDatas.isEmpty() || null == yCoodinateDatas || yCoodinateDatas
                .isEmpty()) {
            return;
        }
        invalidate();
    }

    public abstract void initAnimator(boolean animate, List<ContentData> contentDatas);

    public boolean isAnimateStarted() {
        return animateStarted;
    }


    public void setFullData(List<Integer> xCoodinateDatas, List<Integer> yCoodinateDatas, List<ContentData>
            contentDatas) {


        this.xCoodinateDatas = xCoodinateDatas;
        this.yCoodinateDatas = yCoodinateDatas;
        this.contentDatas = contentDatas;

        checkEmpty(xCoodinateDatas, "xCoodinateDatas");
        checkEmpty(yCoodinateDatas, "yCoodinateDatas");
        checkEmpty(contentDatas, "contentDatas");
        initAnimator(animate, contentDatas);

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
                initAnimator(animate, contentDatas);
            }
        }
    }

    protected static float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    protected static float getTextHeight(Paint paint) {
        return (-paint.ascent() + paint.descent());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoodrinate(canvas);
    }

    private float getMaxTextWidth(List coodinateDatas, Paint paint) {
        float[] widths = new float[coodinateDatas.size()];
        for (int i = 0, length = coodinateDatas.size(); i < length; i++) {
            float textWidth = getTextWidth(String.valueOf(coodinateDatas.get(i)), paint);
            widths[i] = textWidth;
        }
        return MathUtil.max(widths);
    }

    protected void drawCoodrinate(Canvas canvas) {
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
        //画x轴单位说明
        if (!TextUtils.isEmpty(xCoodinateUnitDesc)) {
            paint.setTextSize(unitDescTextSize);
            paint.setColor(unitDescTextColor);
            paint.getTextBounds(xCoodinateUnitDesc, 0, xCoodinateUnitDesc.length(), textBound);
            textStartX = arrowUpX - textBound.width();
            textStartY = arrowUpY;
            canvas.drawText(xCoodinateUnitDesc, textStartX, textStartY, paint);
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

        onDrawSelfContent(canvas, coodinateXStartX, coodinateXStartY, coodinateXStopX, coodinateXStopY, coodinateYStartX, coodinateYStartY, coodinateYStopX, coodinateYStopY);
    }

    /**
     * 画自己
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
    protected abstract void onDrawSelfContent(Canvas canvas, int coodinateXStartX, int coodinateXStartY, int
            coodinateXStopX, int coodinateXStopY, int coodinateYStartX, int coodinateYStartY, int coodinateYStopX,
                                              int coodinateYStopY);
}


