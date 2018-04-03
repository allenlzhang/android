package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.car.CarModeInfoListParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.CarModeAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class CarModeListActivity extends LoadingActivity {

    private ListView car_mode_list;//车型列表
    private CarModeAdapter adapter;//车型列表适配器
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_mode_list);
        loadingDataUI();
        initComponent();
        initData();
        intent=getIntent();
    }

    /**
     * 初始化UI
     *
     * */
    private void initComponent(){
        initTitle("车型");
        car_mode_list=$ViewByID(R.id.car_mode_list);
        car_mode_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarModeInfo mCarModeInfo= (CarModeInfo) adapterView.getItemAtPosition(i);
                Intent intentType=new Intent(CarModeListActivity.this,CarTypeListActivity.class);
                intentType.putExtra("optionid",mCarModeInfo.getId());
                if (intent!=null) {
                    intentType.putExtra("switch",intent.getBooleanExtra("switch",false));//标记从车辆管理界面跳转
                }
                CarModeListActivity.this.startActivity(intentType);
                finish();
            }
        });
    }

    /*
    * 获取车型列表
    *
    * */
    private void initData(){
        CarModeInfoListParser parser=new CarModeInfoListParser(mCallback);
        HashMap<String,String> params=new HashMap<>();
        params.put("brandid","21");
        parser.executePost(URLConfig.getM_CAR_MODE_LIST(),params);
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            BaseResponseInfo<ArrayList<CarModeInfo>> baseResponseInfo= (BaseResponseInfo<ArrayList<CarModeInfo>>) bInfo;
            ArrayList<CarModeInfo> carModeInfos= (ArrayList<CarModeInfo>) baseResponseInfo.getValue();
            if (carModeInfos != null && carModeInfos.size() > 0) {
                adapter = new CarModeAdapter(this,carModeInfos);
                car_mode_list.setAdapter(adapter);
            } else {
                loadNodataUI();
            }
        } catch (Exception e) {
            loadonErrorUI(null);
        }
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}
