package com.carlt.yema.ui.activity.remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.SaftyMsgInfo;
import com.carlt.yema.data.remote.RemoteLogInfo;
import com.carlt.yema.data.remote.RemoteLogListInfo;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.RemoteLogAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 远程操作记录
 *
 */
public class RemoteLogActivity extends LoadingActivity {

    //每一页得个数
    private static final String COUNT = "200";
    private ListView listView;
    private RemoteLogAdapter remoteLogAdapter;

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
        DefaultParser defaultParser = new DefaultParser(mCallback, RemoteLogListInfo.class);
        HashMap map = new HashMap();
        map.put("limit",COUNT);
        map.put("offset",offset+"");
        defaultParser.executePost(URLConfig.getM_CAR_REMOTE_LOG_OPERATION(),map);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData(0);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            RemoteLogListInfo value =  (RemoteLogListInfo)((BaseResponseInfo)bInfo).getValue();
            ArrayList<RemoteLogInfo> logInfos = value.getList();
            if(null == logInfos || logInfos.size() ==0){
                loadNodataUI();
            }else{
                showData(logInfos);
            }
        }catch (Exception e){
            loadonErrorUI(null);
        }
    }

    private void showData(ArrayList<RemoteLogInfo> logInfos) {
        if(remoteLogAdapter == null){
            remoteLogAdapter = new RemoteLogAdapter(this,logInfos);
            listView.setAdapter(remoteLogAdapter);
        }else{
            remoteLogAdapter.setmList(logInfos);
            remoteLogAdapter.notifyDataSetChanged();
        }

    }
}
