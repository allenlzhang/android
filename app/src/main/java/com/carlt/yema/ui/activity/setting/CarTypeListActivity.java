package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.car.CarTypeInfoListParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.CarTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class CarTypeListActivity extends LoadingActivity {

    private ListView car_type_list;//车款列表
    private CarTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_list);
        loadingDataUI();;
        initComponent();
        initData();
    }

    private void initData() {
        CarTypeInfoListParser parser=new CarTypeInfoListParser(mCallback);
        HashMap<String,String> params=new HashMap<>();
        parser.executePost(URLConfig.getM_CAR_TYPE_LIST(),params);
    }

    private void initComponent(){
        initTitle("车款");
        car_type_list=$ViewByID(R.id.car_type_list);
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        try {
            BaseResponseInfo<ArrayList<CarModeInfo>> baseResponseInfo= (BaseResponseInfo<ArrayList<CarModeInfo>>) bInfo;
            ArrayList<CarModeInfo> carModeInfos= (ArrayList<CarModeInfo>) baseResponseInfo.getValue();
            if (carModeInfos != null && carModeInfos.size() > 0) {
                adapter = new CarTypeAdapter(this,carModeInfos);
                car_type_list.setAdapter(adapter);
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
