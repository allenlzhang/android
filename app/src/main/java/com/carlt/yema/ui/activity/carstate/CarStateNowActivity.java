package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarNowStatusInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.utils.ILog;

import java.util.HashMap;

/**
 * 实时车况
 *
 */
public class CarStateNowActivity extends LoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_state_now);
        initTitle("实时车况");
        initView();
        loadingDataUI();
        initData();
    }

    //初始化view
    private void initView() {

    }

    private void initData() {
        DefaultParser<CarNowStatusInfo> parser = new DefaultParser<>(mCallback,CarNowStatusInfo.class);
        parser.executePost(URLConfig.getM_REMOTE_STATUS(),new HashMap());
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}
