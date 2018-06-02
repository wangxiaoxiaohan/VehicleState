package com.spreadwin.vehiclestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.spreadwin.vehiclestate.fragment.BatteryMotorFragment;
import com.spreadwin.vehiclestate.fragment.DrivingParingFragment;
import com.spreadwin.vehiclestate.fragment.EnergyConsumptionFragment;
import com.spreadwin.vehiclestate.fragment.ParkChargingFragment;
import com.spreadwin.vehiclestate.fragment.VehicleStateFragment;
import com.spreadwin.vehiclestate.utils.HttpUtils;
import com.spreadwin.vehiclestate.view.FragmentSwitcher;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    public String TAG = "MainActivity";
    private FragmentSwitcher fragmentSwitcher;
    private RadioGroup radioGroup;
    private RadioButton rbBM;
    private FragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private HttpUtils httpUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main_new);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        initView();
        initData();
    }

    public void initView() {
        Log.i(TAG, "initView");
        fragmentSwitcher = (FragmentSwitcher) findViewById(R.id.switcher);
        radioGroup = (RadioGroup) findViewById(R.id.rg_type);
        rbBM = (RadioButton) findViewById(R.id.rb_ec);

        radioGroup.setOnCheckedChangeListener(this);
        adapter = new FragmentAdapter(getSupportFragmentManager());

        radioGroup.check(rbBM.getId());
    }


    public void initData() {
        Log.i(TAG, "initData");
//        fragments.add(BatteryMotorFragment.getInstance());
        fragments.add(VehicleStateFragment.getInstance());
        fragments.add(BatteryMotorFragment.getInstance());
        fragments.add(EnergyConsumptionFragment.getInstance());
        fragments.add(ParkChargingFragment.getInstance());


        fragmentSwitcher.setAdapter(adapter);
        fragmentSwitcher.setCurrentItem(0);

        httpUtils=new HttpUtils(getApplication());
       new Thread(runnable).start();
    }



    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            httpUtils.initHttp();
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_type:
                if (group.getChildAt(0).getId() == checkedId) {
                    fragmentSwitcher.setCurrentItem(0);
                } else if (group.getChildAt(1).getId() == checkedId) {
                    fragmentSwitcher.setCurrentItem(1);
                } else if (group.getChildAt(2).getId() == checkedId) {
                    fragmentSwitcher.setCurrentItem(2);
                } else if (group.getChildAt(3).getId() == checkedId) {
                    fragmentSwitcher.setCurrentItem(3);
                }

                break;
            default:
                break;
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {

            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
