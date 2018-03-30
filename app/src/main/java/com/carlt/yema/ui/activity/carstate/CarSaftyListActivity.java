package com.carlt.yema.ui.activity.carstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.SaftyMsgInfo;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.CarSaftyAdapter;
import com.carlt.yema.utils.ILog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarSaftyListActivity extends LoadingActivity {

    //提醒小标题
    private TextView safyHeadTV;

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_safty_list);
        initTitle("安防提醒");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        loadingDataUI();
        initData();
    }

    private void initView() {
        safyHeadTV = $ViewByID(R.id.layout_sub_head_txt);
        mListView = $ViewByID(R.id.activity_car_query_illegal_list);
    }

    private void initData() {
        DefaultStringParser parser = new DefaultStringParser(mCallback);
        parser.executePost(URLConfig.getM_SECRETARY_MESSAGE_URL(),new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            String value = ((BaseResponseInfo<String>) bInfo).getValue();
            Gson gson = new Gson();
            Type type = new TypeToken<List<SaftyMsgInfo>>() {}.getType();
            List<SaftyMsgInfo> saftyMsgInfoLists = gson.fromJson(value, type);
            if(null == saftyMsgInfoLists || saftyMsgInfoLists.size() ==0){
                loadNodataUI();
            }else{
                showData(saftyMsgInfoLists);
            }
        }catch (Exception e){
            loadonErrorUI(null);
        }


    }

    /**
     * 显示数据
     * @param saftyMsgInfoLists
     */
    private void showData(List<SaftyMsgInfo> saftyMsgInfoLists) {
        CarSaftyAdapter adapter = new CarSaftyAdapter(CarSaftyListActivity.this,saftyMsgInfoLists);
        mListView.setAdapter(adapter);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}
