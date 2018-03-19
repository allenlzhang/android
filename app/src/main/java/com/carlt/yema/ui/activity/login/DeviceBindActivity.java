package com.carlt.yema.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;//返回按钮

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_pin_code;//pin码输入框
    private EditText car_vin_code;//vin码输入框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        initComponent();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("绑定设备");
        btn_select_car = findViewById(R.id.btn_select_car);
        btn_select_car.setOnClickListener(this);
        bind_commit = findViewById(R.id.bind_commit);
        bind_commit.setOnClickListener(this);
        car_pin_code = findViewById(R.id.car_pin_code);
        car_vin_code = findViewById(R.id.car_vin_code);
    }

    @Override
    public void onClick(View view) {

    }
}
