package com.carlt.yema.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.ui.activity.setting.CarModeListActivity;

public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;//返回按钮
    private LinearLayout title_bar;

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_vin_code;//vin码输入框

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        intent=getIntent();
        initComponent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
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
        car_vin_code = findViewById(R.id.car_vin_code);
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("vin"))) {
            car_vin_code.setText(intent.getStringExtra("vin"));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_select_car:
                // 选择车系
                Intent intent=new Intent(this, CarModeListActivity.class);
                startActivity(intent );
                break;
            case R.id.bind_commit:
                break;
        }
    }


}
