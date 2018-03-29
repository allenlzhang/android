package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.car.SaftyMsgInfo;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;

import java.util.HashMap;

public class CarSaftyListActivity extends LoadingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_safty_list);
        initTitle("安防提醒");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataUI();
        initData();
    }

    private void initData() {
        DefaultParser<SaftyMsgInfo> parser = new DefaultParser<SaftyMsgInfo>(mCallback,SaftyMsgInfo.class);
        parser.executePost(URLConfig.getM_SECRETARY_MESSAGE_URL(),new HashMap());
    }
}
