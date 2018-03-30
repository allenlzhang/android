package com.carlt.yema.ui.activity.remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;

/**
 * 远程操作记录
 *
 */
public class RemoteLogActivity extends LoadingActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_log);
        initTitle("远程操作记录");
        initView();
        loadingDataUI();
        initData();
    }

    private void initView() {
        listView = $ViewByID(R.id.remotelog_list);
    }

    private void initData() {


    }


}
