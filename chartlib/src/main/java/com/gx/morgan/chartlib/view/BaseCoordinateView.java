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
    protected List<Integer> xCoordinateDatas;//x轴上的数字
    protected List<Integer> yCoordinateDatas;//y轴上的数字
    protected int contentPadding;//内容与view边界的距离
    protected int xCoordinateBulgeDistance;//x轴凸起距离
    protected int xCoordinateBulgeDirection;//x轴凸起方向
    protected int coordinatateColor;//坐标轴颜色
    protected int arrowHeight;//箭头高度
    protected int arrowWidth;//箭头宽度
    protected List<ContentData> contentDatas;//内容x、y数据
    protected String xCoordinateUnitDesc;//x轴单位说明
    protected String yCoordinateUnitDesc;//y轴单位说明
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
        coordinatateColor = Color.BLACK;

        arrowHeight = (int) ViewUtil.dp2px(context, 5);
        arrowWidth = arrowHeight;

        unitDescTextSize = (int) ViewUtil.sp(context, 14);
        unitDescTextColor = yTextColor;


        xCoordinateUnitDesc = "";
        yCoordinateUnitDesc = "";
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

    public void setxCoordinateDatas(List<Integer> xCoordinateDatas) {

        checkEmpty(xCoordinateDatas, "xCoordinateDatas");

        this.xCoordinateDatas = xCoordinateDatas;

        if (null == yCoordinateDatas || yCoordinateDatas.isEmpty() || null == contentDatas || contentDatas.isEmpty()) {
            return;
        }
        invalidate();
    }

    public void setyCoordinateDatas(List<Integer> yCoordinateDatas) {
        checkEmpty(yCoordinateDatas, "yCoordinateDatas");
        this.yCoordinateDatas = yCoordinateDatas;
        if (null == xCoordinateDatas || xCoordinateDatas.isEmpty() || null == contentDatas || contentDatas.isEmpty()) {

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

    public void setCoordinatateColor(int coordinatateColor) {
        if (this.coordinatateColor != coordinatateColor) {
            this.coordinatateColor = coordinatateColor;
            invalidate();
        }
    }

    public void setContentDatas(List<ContentData> contentDatas) {
        checkEmpty(contentDatas, "contentDatas");

        this.contentDatas = contentDatas;
        initAnimator(animate, contentDatas);
        if (null == xCoordinateDatas || xCoordinateDatas.isEmpty() || null == yCoordinateDatas || yCoordinateDatas
                .isEmpty()) {
            return;
        }
        invalidate();
    }

    public abstract void initAnimator(boolean animate, List<ContentData> contentDatas);

    public boolean isAnimateStarted() {
        return animateStarted;
    }


    public void setFullData(List<Integer> xCoordinateDatas, List<Integer> yCoordinateDatas, List<ContentData>
            contentDatas) {


        this.xCoordinateDatas = xCoordinateDatas;
        this.yCoordinateDatas = yCoordinateDatas;
        this.contentDatas = contentDatas;

        checkEmpty(xCoordinateDatas, "xCoordinateDatas");
        checkEmpty(yCoordinateDatas, "yCoordinateDatas");
        checkEmpty(contentDatas, "contentDatas");
        initAnimator(animate, contentDatas);

        invalidate();
    }

    private <T> void checkEmpty(List<T> datas, String target) {
        if (null == datas || datas.isEmpty()) {
            throw new NullPointerException(target + " cannor be NULL or empty");
        }
    }

    public void setxCoordinateUnitDesc(String xCoordinateUnitDesc) {
        if (!this.xCoordinateUnitDesc.equals(xCoordinateUnitDesc)) {
            this.xCoordinateUnitDesc = xCoordinateUnitDesc;
            invalidate();
        }
    }

    public void setyCoordinateUnitDesc(String yCoordinateUnitDesc) {
        if (!this.yCoordinateUnitDesc.equals(yCoordinateUnitDesc)) {
            this.yCoordinateUnitDesc = yCoordinateUnitDesc;
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
        drawCoordinate(canvas);
    }

    private float getMaxTextWidth(List coordinateDatas, Paint paint) {
        float[] widths = new float[coordinateDatas.size()];
        for (int i = 0, length = coordinateDatas.size(); i < length; i++) {
            float textWidth = getTextWidth(String.valueOf(coordinateDatas.get(i)), paint);
            widths[i] = textWidth;
        }
        return MathUtil.max(widths);
    }

    protected void drawCoordinate(Canvas canvas) {
        paint.reset();
        paint.setTextSize(yTextSize);
        float maxTextWidth = getMaxTextWidth(yCoordinateDatas, paint);

        paint.setTextSize(xTextSize);

        //画x轴
        paint.setColor(coordinatateColor);
        int xDataSize = xCoordinateDatas.size();
        float textHeight = getTextHeight(paint);

        int coordinateXStartX = (int) (getPaddingLeft() + contentPadding + maxTextWidth + textCoordinatePadding);
        int coordinateXStartY = (int) (height - getPaddingBottom() - contentPadding - textHeight -
                textCoordinatePadding);
        int coordinateXStopX = width - getPaddingRight() - contentPadding;
        int coordinateXStopY = coordinateXStartY;
        canvas.drawLine(coordinateXStartX, coordinateXStartY, coordinateXStopX, coordinateXStopY, paint);


        //画x轴数字
        int xCoordinateDistance = coordinateXStopX - coordinateXStartX;
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

        boolean isXCoordinateDataInBulge= isXCoordinateDataInBulge();//是否在x轴凸起部分
        for (int i = 0, size = xDataSize; i < size; i++) {
            //画x轴数字
            paint.setColor(xTextColor);
            String text = String.valueOf(xCoordinateDatas.get(i));
            paint.getTextBounds(text, 0, text.length(), textBound);
            textStartX = isXCoordinateDataInBulge?coordinateXStartX + i * xSpacing - textBound.width() / 2:coordinateXStartX + i * xSpacing + xSpacing / 2 - textBound.width() / 2;
            textStartY = height - paddingBottom - contentPadding;
            canvas.drawText(text, textStartX, textStartY, paint);

            //画x轴凸起部分
            paint.setColor(coordinatateColor);
            xCoodinateBuglyStartX = isXCoordinateDataInBulge?textStartX + textBound.width() / 2 + xSpacing:textStartX + textBound.width() / 2 + xSpacing / 2;
            xCoodinateBuglyStartY = coordinateXStartY;
            xCoodinateBuglyStopX = xCoodinateBuglyStartX;
            if (xCoordinateBulgeDirection == XCoordinateBulgeDirection.UP) {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY - xCoordinateBulgeDistance;
            } else if (xCoordinateBulgeDirection == XCoordinateBulgeDirection.DOWN) {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY + xCoordinateBulgeDistance;
            } else {
                xCoodinateBuglyStopY = xCoodinateBuglyStartY + xCoordinateBulgeDistance;
            }

            if (i == size - 1) {//最后一个就画箭头
                arrowUpX = coordinateXStopX - arrowWidth;
                arrowUpY = coordinateXStartY - arrowHeight;
                paint.setStrokeJoin(Paint.Join.MITER);//设置拐角为尖的
                canvas.drawLine(coordinateXStopX, coordinateXStopY, arrowUpX, arrowUpY, paint);

                int arrowDownX = arrowUpX;
                int arrowDownY = coordinateXStartY + arrowHeight;
                canvas.drawLine(coordinateXStopX, coordinateXStopY, arrowDownX, arrowDownY, paint);


            } else {
                canvas.drawLine(xCoodinateBuglyStartX, xCoodinateBuglyStartY, xCoodinateBuglyStopX,
                        xCoodinateBuglyStopY, paint);
            }
        }
        //画x轴单位说明
        if (!TextUtils.isEmpty(xCoordinateUnitDesc)) {
            paint.setTextSize(unitDescTextSize);
            paint.setColor(unitDescTextColor);
            paint.getTextBounds(xCoordinateUnitDesc, 0, xCoordinateUnitDesc.length(), textBound);
            textStartX = arrowUpX - textBound.width();
            textStartY = arrowUpY;
            canvas.drawText(xCoordinateUnitDesc, textStartX, textStartY, paint);
        }


        int paddingTop = getPaddingTop();

        //画y轴
        int coordinateYStartX = coordinateXStartX;
        int coordinateYStartY = coordinateXStartY;
        int coordinateYStopX = coordinateYStartX;
        int coordinateYStopY = paddingTop + contentPadding;
        canvas.drawLine(coordinateYStartX, coordinateYStartY, coordinateYStopX, coordinateYStopY, paint);

        //画y轴箭头
        int arrowLeftX = coordinateYStopX - arrowHeight;
        int arrowLeftY = coordinateYStopY + arrowWidth;
        int arrowRightX = coordinateYStopX + arrowHeight;
        int arrowRightY = arrowLeftY;
        canvas.drawLine(coordinateYStopX, coordinateYStopY, arrowLeftX, arrowLeftY, paint);
        canvas.drawLine(coordinateYStopX, coordinateYStopY, arrowRightX, arrowRightY, paint);

        //画y轴单位说明
        if (!TextUtils.isEmpty(yCoordinateUnitDesc)) {
            paint.setColor(unitDescTextColor);
            paint.setTextSize(unitDescTextSize);
            paint.getTextBounds(yCoordinateUnitDesc, 0, yCoordinateUnitDesc.length(), textBound);
            textStartX = coordinateXStartX + textCoordinatePadding;
            textStartY = arrowLeftY + textBound.height();
            canvas.drawText(yCoordinateUnitDesc, textStartX, textStartY, paint);
        }


        int paddingLeft = getPaddingLeft();
        int yDataSize = yCoordinateDatas.size();
        int yCoordinateDistance = coordinateYStartY - coordinateYStopY;
        int ySpacing = yCoordinateDistance / yDataSize;

        int contentLineStartX = 0;
        int contentLineStartY = 0;
        int contentLineStopX = 0;
        int contentLineStopY = 0;

        paint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0, size = yDataSize; i < size; i++) {

            //画y轴数字
            paint.setTextSize(yTextSize);
            paint.setColor(yTextColor);
            String text = String.valueOf(yCoordinateDatas.get(i));
            paint.getTextBounds(text, 0, text.length(), textBound);
            textStartX = (int) (paddingLeft + contentPadding + maxTextWidth);
            textStartY = coordinateYStartY - i * ySpacing + textBound.height() / 2;
            canvas.drawText(text, textStartX, textStartY, paint);


            //画内容横线
            paint.setColor(coordinatateColor);
            contentLineStartX = coordinateXStartX;
            contentLineStartY = coordinateYStartY - i * ySpacing;
            contentLineStopX = coordinateXStopX;
            contentLineStopY = contentLineStartY;

            canvas.drawLine(contentLineStartX, contentLineStartY, contentLineStopX, contentLineStopY, paint);
        }

        onDrawSelfContent(canvas, coordinateXStartX, coordinateXStartY, coordinateXStopX, coordinateXStopY, coordinateYStartX, coordinateYStartY, coordinateYStopX, coordinateYStopY);
    }

    /**
     * x轴数据是否在x轴凸起部分
     * @return boolean
     */
    protected abstract boolean isXCoordinateDataInBulge();


    /**
     * 画自己
     * @param canvas
     * @param coordinateXStartX x轴开始横坐标
     * @param coordinateXStartY x轴开始纵坐标
     * @param coordinateXStopX x轴结束横坐标
     * @param coordinateXStopY x轴结束纵坐标
     * @param coordinateYStartX y轴开始横坐标
     * @param coordinateYStartY y轴开始纵坐标
     * @param coordinateYStopX y轴结束横坐标
     * @param coordinateYStopY y轴结束纵坐标
     */
    protected abstract void onDrawSelfContent(Canvas canvas, int coordinateXStartX, int coordinateXStartY, int
            coordinateXStopX, int coordinateXStopY, int coordinateYStartX, int coordinateYStartY, int coordinateYStopX,
                                              int coordinateYStopY);
}


