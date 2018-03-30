package com.carlt.yema.ui.activity.carstate;

import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 胎压检测
 */
public class CarTiresStateActivity extends LoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_tires_state);
        initTitle("胎压监测");
        initView();
        loadingDataUI();
        initdata();
    }

    private void initView() {
        
    }

    private void initdata() {
        DefaultStringParser parser = new DefaultStringParser(mCallback);
        HashMap<String,String> param = new HashMap();
        param.put("move_device_name", LoginInfo.getDeviceidstring());
        parser.executePost(URLConfig.getM_REMOTE_DRIECTRRESSURE(),param);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initdata();
    }
}
