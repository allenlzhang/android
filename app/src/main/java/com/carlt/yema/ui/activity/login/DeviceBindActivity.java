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
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.activity.setting.CarModeListActivity;
import com.carlt.yema.ui.view.UUToast;

import java.util.HashMap;

public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;//返回按钮
    private LinearLayout title_bar;

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_vin_code;//vin码输入框

    private Intent intent;

    private String carTitle;//用户选择的车款

    private String deviceId;//设备ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBindData();
    }

    private void setBindData(){
        intent=getIntent();
        carTitle=intent.getStringExtra("cat_title");
        if (!TextUtils.isEmpty(carTitle)) {
            btn_select_car.setText(carTitle);
        }
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("vin"))) {
            car_vin_code.setText(intent.getStringExtra("vin"));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setBindData();
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
                deviceId=car_vin_code.getText().toString();
                if (isVinValid()) {
                    bindDevice();
                }
                break;
        }
    }

    private void bindDevice(){
        DefaultStringParser parser=new DefaultStringParser(callback);
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("vin",deviceId);
        parser.executePost(URLConfig.getM_DEVICE_BIND_CAR(),map);
    }

    BaseParser.ResultCallback callback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            LoginInfo.setDeviceActivate(true);
            LoginInfo.setDeviceidstring(bInfo.getValue().toString());
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            switch (bInfo.getFlag()) {
                case 1004:
                    UUToast.showUUToast(DeviceBindActivity.this,"设备号格式不正确");
                    break;
                case 1105:
                    UUToast.showUUToast(DeviceBindActivity.this,"该设备未出库,无法绑定或解绑");
                    break;
                case 2710:
                    UUToast.showUUToast(DeviceBindActivity.this,"您不能绑定其他经销商的设备");
                    break;
                case 2708:
                    UUToast.showUUToast(DeviceBindActivity.this,"您已经绑定设备,不能绑定多个设备");
                    break;
                case 2709:
                    UUToast.showUUToast(DeviceBindActivity.this,"该设备不能重复绑定");
                    break;
                case 2701:
                    UUToast.showUUToast(DeviceBindActivity.this,"该设备不支持您绑定的车型");
                    break;
                case 2702:
                    UUToast.showUUToast(DeviceBindActivity.this,"该设备不能重复绑定");
                    break;
                case 2703:
                    UUToast.showUUToast(DeviceBindActivity.this,"该设备不支持您绑定的车款");
                    break;
                case 1014:
                    UUToast.showUUToast(DeviceBindActivity.this,"操作失败,请确认信息后重试");
                    break;
            }
        }
    };
    private boolean isVinValid(){
        if (TextUtils.isEmpty(deviceId)) {
            UUToast.showUUToast(this," VIN码不能为空");
        } else if (deviceId.length()<17) {
            UUToast.showUUToast(this," 请输入正确的VIN码");
        } else if (btn_select_car.getText().equals(getResources().getString(R.string._car_select_type))) {
            UUToast.showUUToast(this," 爱车信息不能为空");
        }
        return true;
    }

}
