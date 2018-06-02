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
import android.widget.RelativeLayout;

import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.interfaces.iData.ICurveData;
import com.spreadwin.vehiclestate.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class HealthDegreeView extends RelativeLayout{

    private String TAG="HealthDegreeView";
    public  View mECView;
    public Activity mContext;



    public HealthDegreeView(Context context) {
        super(context);
        Log.i(TAG,"EnergyConsumptionView");
        mECView = LayoutInflater.from(context).inflate(R.layout.health_degree_view, this);
    }



    public View getView(Activity activity){
        Log.i(TAG,"getView");
        this.mContext = activity;
        if (mECView == null) {
            mECView = LayoutInflater.from(mContext).inflate(R.layout.health_degree_view, this);
        }

        return mECView;
    }

    public  void setInfo() {
        Log.i(TAG,"setInfo");
        initData();
        initView();
    }

    private void initData(){
        Log.i(TAG,"initData");
    }


    private void initView(){
        Log.i(TAG,"initView");

    }



}
