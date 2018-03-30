package com.carlt.yema.ui.activity.remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;

import java.net.URL;
import java.util.HashMap;

/**
 * 远程操作记录
 *
 */
public class RemoteLogActivity extends LoadingActivity {

    //每一页得个数
    private static final String COUNT = "200";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_log);
        initTitle("远程操作记录");
        initView();
        loadingDataUI();
        initData(0);
    }

    private void initView() {
        listView = $ViewByID(R.id.remotelog_list);
    }

    private void initData(int offset) {
        DefaultStringParser defaultStringParser = new DefaultStringParser(mCallback);
        HashMap map = new HashMap();
        map.put("limit",COUNT);
        map.put("offset",offset+"");
        defaultStringParser.executePost(URLConfig.getM_CAR_REMOTE_LOG_OPERATION(),map);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData(0);
    }
}
