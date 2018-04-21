package com.gx.morgan.chartlib.view;

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

import com.gx.morgan.chartlib.R;
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

    public static class PointType {
        public static final int CIRCLE = 1;
        public static final int RECT = 2;
        public static final int ROUNDRECT = 3;//圆角方形
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({PointType.CIRCLE, PointType.RECT,PointType.ROUNDRECT})
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
        pointRadius =  ViewUtil.dp2px(context, 5);
        contentTextSize=  ViewUtil.sp(context,12);
        contentTextColor =Color.RED;
        lineColor = Color.BLUE;
        pointType = PointType.ROUNDRECT;
        roundDegree =  ViewUtil.dp2px(context, 2);
        lineWidth =  ViewUtil.dp2px(context, 1);
        pointRectF = new RectF();

        initAttr(context,attrs);
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
        animate = ViewUtil.optBoolean(typedArray, R.styleable.LineView_animate, true);



        pointRadius=ViewUtil.optPixelSize(typedArray,R.styleable.LineView_pointRadius,pointRadius);
        lineColor=ViewUtil.optColor(typedArray,R.styleable.LineView_lineColor,lineColor);
        pointType=ViewUtil.optInt(typedArray,R.styleable.LineView_pointType,pointType);
        contentTextSize=ViewUtil.optPixelSize(typedArray,R.styleable.LineView_contentTextSize,contentTextSize);
        contentTextColor =ViewUtil.optColor(typedArray,R.styleable.LineView_contentTextColor, contentTextColor);
        roundDegree=ViewUtil.optPixelSize(typedArray,R.styleable.LineView_roundDegree,roundDegree);
        lineWidth=ViewUtil.optPixelSize(typedArray,R.styleable.LineView_lineWidth,lineWidth);


        if(null!=typedArray){
            typedArray.recycle();
        }
    }

    public void setPointRadius(float pointRadius){
        if(this.pointRadius!=pointRadius){
            this.pointRadius=pointRadius;
            invalidate();
        }
    }
    public void setLineColor(@ColorInt int lineColor){
        if(this.lineColor!=lineColor){
            this.lineColor=lineColor;
            invalidate();
        }
    }

    public void setPointType(@LinePointType int pointType){
        if(this.pointType!=pointType){
            this.pointType=pointType;
            invalidate();
        }
    }

    public void setContentTextSize(float contentTextSize){
            if(this.contentTextSize!=contentTextSize){
                this.contentTextSize=contentTextSize;
                invalidate();
            }
    }

    public void setContentTextColor(@ColorInt int contentTextColor){
        if(this.contentTextColor != contentTextColor){
            this.contentTextColor = contentTextColor;
            invalidate();
        }
    }

    public void setLineWidth(float lineWidth){
        if(this.lineWidth!=lineWidth){
            this.lineWidth=lineWidth;
            invalidate();
        }
    }

    public void setRoundDegree(float roundDegree){
        if(this.roundDegree!=roundDegree){
            this.roundDegree=roundDegree;
            invalidate();
        }
    }

    @Override
    public void initAnimator(boolean animate, List<ContentData> contentDatas) {


    }

    @Override
    protected boolean isXCoordinateDataInBulge() {
        return true;
    }

    @Override
    protected void onDrawSelfContent(Canvas canvas, float coordinateXStartX, float coordinateXStartY, float coordinateXStopX,
                                     float coordinateXStopY, float coordinateYStartX, float coordinateYStartY, float
                                             coordinateYStopX, float coordinateYStopY) {


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

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        float startX = 0;
        float startY = 0;
        float stopX = 0;
        float stopY = 0;
        float pointX = 0;
        float pointY = 0;
        float prePointX = 0;
        float prePointY = 0;
        float oldStrokeWidth = paint.getStrokeWidth();


        for (int i = 0, size = contentDatas.size(); i < size; i++) {
            ContentData contentData = contentDatas.get(i);
            pointX = (float) (coordinateXStartX + xCoordinateDistance * (contentData.x - firstXCoordinateData) * 1.0 /
                    xCoordinateDataRangeLength);
            pointY = coordinateYStartY - (float) (yCoordinateDistance * (contentData.y - firstYCoordinateData) * 1.0 /
                    yCoodinateDataRangLength);



            if (0 == i) {
                prePointX = pointX;
                prePointY = pointY;
            } else {
                paint.setStrokeWidth(oldStrokeWidth);
                paint.setColor(lineColor);
                startX = prePointX;
                startY = prePointY;
                stopX = pointX;
                stopY = pointY;
                canvas.drawLine(startX, startY, stopX, stopY, paint);

                prePointX = pointX;
                prePointY = pointY;
            }

            drawInflectionPoint(canvas, pointX, pointY);//画拐点
            drawConentText(canvas, pointX, pointY, contentData);
        }
    }

    private void drawConentText(Canvas canvas, float pointX, float pointY, ContentData contentData) {
        paint.setTextSize(contentTextSize);
        paint.setColor(contentTextColor);
        String text = String.valueOf(contentData.y);
        paint.getTextBounds(text, 0, text.length(), textBound);

        float x = pointX +textBound.width() / 2;
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
