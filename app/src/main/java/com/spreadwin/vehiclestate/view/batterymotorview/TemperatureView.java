package com.spreadwin.vehiclestate.view.batterymotorview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.idtk.smallchart.chart.CombineChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.interfaces.iData.IBarLineCurveData;
import com.spreadwin.vehiclestate.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class TemperatureView extends RelativeLayout{

    private String TAG="TempView";
    public  View mECView;
    public Activity mContext;

    private CombineChart combineChart;

    private CurveData mCurveData = new CurveData();
    private ArrayList<PointF> mPointArrayList1 = new ArrayList<>();

    private CurveData mLineData = new CurveData();
    private ArrayList<PointF> mPointArrayList2 = new ArrayList<>();

    private CurveData mBarData = new CurveData();
    private ArrayList<PointF> mPointArrayList3 = new ArrayList<>();

    private ArrayList<IBarLineCurveData> mDataList = new ArrayList<>();

    protected float[][] points = new float[][]{{1,0}, {2,47}, {3,11}, {4,38}, {5,9},{6,91}, {7,14}, {8,37}, {9,29}, {10,31}};

    public TemperatureView(Context context) {
        super(context);
        Log.i(TAG,"EnergyConsumptionView");
        mECView = LayoutInflater.from(context).inflate(R.layout.temp_view, this);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public View getView(Activity activity){
        Log.i(TAG,"getView");
        this.mContext = activity;
        if (mECView == null) {
            mECView = LayoutInflater.from(mContext).inflate(R.layout.temp_view, this);
        }

        return mECView;
    }

    public  void setInfo() {
        Log.i(TAG,"setInfo");
        initData();
        initView();
    }



    private void initView(){
        Log.i(TAG,"initView");
        combineChart = (CombineChart)mECView.findViewById(R.id.combineChart);
        combineChart.isAnimated = false;
        combineChart.setDataList(mDataList);
    }


    private void initData() {
        Log.i(TAG,"initData");
        for (int i = 0; i < 8; i++) {
            mPointArrayList3.add(new PointF(points[i][0], points[i][1]));
            mPointArrayList2.add(new PointF(points[i][0], points[i][1]-5));
            mPointArrayList1.add(new PointF(points[i][0], points[i][1]+10));
        }

        mBarData.setValue(mPointArrayList3);
        mBarData.setColor(Color.CYAN);
        mBarData.setPaintWidth(pxTodp(5));
        mBarData.setTextSize(pxTodp(10));
        mDataList.add(mBarData);

        mLineData.setValue(mPointArrayList2);
        mLineData.setColor(Color.MAGENTA);
        mLineData.setPaintWidth(pxTodp(3));
        mLineData.setTextSize(pxTodp(10));
        mDataList.add(mLineData);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
        mCurveData.setDrawable(drawable);
        mCurveData.setValue(mPointArrayList1);
        mCurveData.setColor(Color.YELLOW);
        mCurveData.setPaintWidth(pxTodp(3));
        mCurveData.setTextSize(pxTodp(10));
        mDataList.add(mCurveData);

    }


    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        Log.i(TAG,"pxTodp valueDP="+valueDP);
        return valueDP;
    }


    public void releaseInfo(){
        mCurveData=null;
        combineChart=null;
    }

}
