package com.spreadwin.vehiclestate.fragment;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.interfaces.iData.ICurveData;
import com.spreadwin.vehiclestate.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class VehicleStateFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private String TAG="VehicleStateFragment";
    public static VehicleStateFragment vehicleStateFragment;
    public  View mECView;
    private RadioGroup rgKm;
    private RadioButton rbkm;

    private CurveChart curveChart;

    private CurveData mCurveData ;
    private ArrayList<PointF> mPointArrayList1 ;


    private ArrayList<ICurveData> mDataList ;

    protected float[][] points = new float[][]{{1,10}, {2,77}, {3,31}, {4,58}, {5,49},{6,91}, {7,14}, {8,37}, {9,29}, {10,31}};


    public static VehicleStateFragment getInstance(){
        Log.i("VehicleStateFragment","getInstance");
        if (vehicleStateFragment==null){
            vehicleStateFragment=new VehicleStateFragment();
        }
        return vehicleStateFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView");
        mECView=inflater.inflate(R.layout.energy_consumption_view, container, false);
        return mECView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, null);
        Log.i(TAG,"onViewCreated");
       setInfo();
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }




    public  void setInfo() {
        Log.i(TAG,"setInfo");
        initData();
        initView();
    }



    private void initView(){
        Log.i(TAG,"initView");
        rgKm=(RadioGroup)mECView.findViewById(R.id.rg_km);
        rgKm.setOnCheckedChangeListener(this);
        rbkm=(RadioButton)mECView.findViewById(R.id.rb_km);
        curveChart = (CurveChart) mECView.findViewById(R.id.curvechart);
        curveChart.setDataList(mDataList);
        curveChart.setXAxisUnit("k");
        curveChart.setYAxisUnit("h");
        curveChart.setAxisTextSize(22);
        curveChart.setShowAverageValue(true);
        curveChart.setAverageValueColor(Color.BLUE);
        rgKm.check(rbkm.getId());
    }


    private void initData() {
        mDataList=new ArrayList<>();
        mPointArrayList1= new ArrayList<>();
        Log.i(TAG,"initData");
        for (int i = 0; i < 8; i++) {
            mPointArrayList1.add(new PointF(points[i][0], points[i][1]));
        }

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue_a);
        mCurveData=new CurveData();
        mCurveData.setDrawable(drawable);
        mCurveData.setValue(mPointArrayList1);
        mCurveData.setColor(Color.BLUE);
        mCurveData.setPaintWidth(pxTodp(3));
        mCurveData.setTextSize(pxTodp(10));
        mDataList.add(mCurveData);


    }


    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        Log.i(TAG,"pxTodp valueDP="+valueDP);
        return valueDP;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView");
        mECView=null;
        curveChart=null;
        mCurveData=null;
        mPointArrayList1.clear();
        mPointArrayList1=null;
        mDataList.clear();
        mDataList=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){
            case R.id.rg_km:

                break;
                default:
                    break;
        }
    }
}
