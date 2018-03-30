package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.model.LoginInfo;

/**
 * 定位寻车
 */
public class FindCarActivity extends LoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);
        initTitle("");
    }
}
