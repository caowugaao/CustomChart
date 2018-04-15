package com.gx.morgan.chartlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.gx.morgan.chartlib.utils.ViewUtil;

import java.util.List;

/**
 * description：折线图
 * <br>author：caowugao
 * <br>time：2018/4/15 14:12
 */
public class LineView extends BaseCoordinateView {


    private int pointRadius;
    private int lineColor;
    private boolean isPointSolid = false;

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
        pointRadius = (int) ViewUtil.dp2px(context, 5);
        lineColor = Color.BLUE;
        isPointSolid = false;
    }


    @Override
    public void initAnimator(boolean animate, List<ContentData> contentDatas) {


    }

    @Override
    protected boolean isXCoordinateDataInBulge() {
        return true;
    }

    @Override
    protected void onDrawSelfContent(Canvas canvas, int coordinateXStartX, int coordinateXStartY, int coordinateXStopX,
                                     int coordinateXStopY, int coordinateYStartX, int coordinateYStartY, int
                                             coordinateYStopX, int coordinateYStopY) {


        int xCoordinateDistance = Math.abs(coordinateXStartX - coordinateXStopX);
        int yCoordinateDistance = Math.abs(coordinateYStartY - coordinateYStopY);

        int xDataSize = xCoordinateDatas.size();
        int xCoordinateDataRangeLength = xCoordinateDatas.get(xDataSize - 1) - xCoordinateDatas.get(0);//x轴数据区间长度

        int yDataSize = yCoordinateDatas.size();
        int yCoodinateDataRangLength = yCoordinateDatas.get(yDataSize - 1) + yCoordinateDatas.get(yDataSize - 1) -
                yCoordinateDatas.get(yDataSize - 2) - yCoordinateDatas.get(0);//y轴数据区间长度

        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);


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
            pointX = (float) (coordinateXStartX + xCoordinateDistance * contentData.x * 1.0 /
                    xCoordinateDataRangeLength);
            pointY = (float) (coordinateYStopY + yCoordinateDistance * contentData.y * 1.0 / yCoodinateDataRangLength);
            paint.setStrokeWidth(2 * pointRadius);
            canvas.drawPoint(pointX, pointY, paint);
            if (0 == i) {
                prePointX = pointX;
                prePointY = pointY;
            } else {
                paint.setStrokeWidth(oldStrokeWidth);
                startX = prePointX;
                startY = prePointY;
                stopX = pointX;
                stopY = pointY;
                canvas.drawLine(startX, startY, stopX, stopY, paint);

                prePointX = pointX;
                prePointY = pointY;
            }
        }
    }
}
