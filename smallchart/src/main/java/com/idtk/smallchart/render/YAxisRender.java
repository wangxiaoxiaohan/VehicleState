package com.idtk.smallchart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.idtk.smallchart.interfaces.iData.IXAxisData;
import com.idtk.smallchart.interfaces.iData.IYAxisData;

import java.text.NumberFormat;

/**
 * Created by Idtk on 2016/6/6.
 * Blog : http://www.idtkm.com
 * GitHub : https://github.com/Idtk
 * 描述 ; Y轴渲染类
 */
public class YAxisRender extends AxisRender {
    private Paint mPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint aVPaint = new Paint();//平均值
    private IYAxisData yAxisData;
    private IXAxisData xAxisData;
    private NumberFormat numberFormat;
    private PointF mPoint = new PointF();

    public YAxisRender(IYAxisData yAxisData, IXAxisData xAxisData) {
        super();
        this.yAxisData = yAxisData;
        this.xAxisData = xAxisData;
        mPaint.setAntiAlias(true);
        mPaint.setColor(yAxisData.getColor());
        mPaint.setTextSize(yAxisData.getTextSize());
        mPaint.setStrokeWidth(yAxisData.getPaintWidth());

        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GRAY);

        aVPaint.setStrokeWidth(yAxisData.getPaintWidth()+4);
        aVPaint.setAntiAlias(true);
        /**
         * 设置小数点位数
         */
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(yAxisData.getDecimalPlaces());
    }

    @Override
    public void drawGraph(Canvas canvas) {
        canvas.drawLine(0,0,0,yAxisData.getAxisLength(),mPaint);

        for (int i=0;(yAxisData.getInterval()*i+yAxisData.getMinimum())<=yAxisData.getMaximum();i++){
            canvas.save();
            canvas.scale(1,-1);
            /**
             * 坐标数值
             */
            float TextPathX = yAxisData.getAxisLength()/100;
            float TextPathY = (mPaint.descent()+mPaint.ascent())/2+(float) (yAxisData.getInterval()*i*yAxisData.getAxisScale());
            mPoint.x =-TextPathX;
            mPoint.y =-TextPathY;
            textCenter(new String[]{numberFormat.format(yAxisData.getInterval()*i+yAxisData.getMinimum())+yAxisData.getUnit()},
                    mPaint,canvas, mPoint, Paint.Align.RIGHT);
            if (i>0)
            canvas.drawLine(0,-TextPathY,xAxisData.getAxisLength(),-TextPathY,linePaint);
            canvas.restore();
            Log.i("YAxisRender","-TextPathY="+(-TextPathY)+",i="+i);
        }
        Log.i("YAxisRender","yAxisData.getAverageValue()="+yAxisData.getAverageValue());
        Log.i("YAxisRender","yAxisData.getAxisLength()="+yAxisData.getAxisLength());
//        yAxisData.setAverageValue(yAxisData.getAverageValue()+(mPaint.descent()+mPaint.ascent())/2);
//        Log.i("YAxisRender","yAxisData.getAverageValue()="+yAxisData.getAverageValue());
        /**
         * 平均值
         */
        if(yAxisData.isShowAverageValue()) {
            aVPaint.setColor(yAxisData.getAverageValueColor());
            canvas.drawLine(0, (float) (yAxisData.getAverageValue()*1.92), xAxisData.getAxisLength(), (float) (yAxisData.getAverageValue()*1.92), aVPaint);
        }
        /**
         * 箭头
         */
        canvas.drawLine(0,yAxisData.getAxisLength(),yAxisData.getAxisLength()*0.01f,yAxisData.getAxisLength()*0.99f,mPaint);
        canvas.drawLine(0,yAxisData.getAxisLength(),-yAxisData.getAxisLength()*0.01f,yAxisData.getAxisLength()*0.99f,mPaint);
        canvas.save();
        canvas.scale(1,-1);
        canvas.drawText(yAxisData.getUnit(),0,-yAxisData.getAxisLength()+(mPaint.descent()+mPaint.ascent())*2+20,mPaint);
        canvas.restore();
    }
}
