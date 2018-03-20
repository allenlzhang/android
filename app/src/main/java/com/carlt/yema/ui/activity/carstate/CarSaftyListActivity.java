package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class CarSaftyListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_safty_list);

        initTitle("安防提醒");
    }
}
