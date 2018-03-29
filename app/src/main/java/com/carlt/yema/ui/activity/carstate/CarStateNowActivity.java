package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;

public class CarStateNowActivity extends LoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_state_now);
        initTitle("实时车况");
        initView();
        initData();
    }


    private void initView() {
    }


    private void initData() {

    }
}
