package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.protocolparser.car.CarTypeInfoListParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.activity.login.DeviceBindActivity;
import com.carlt.yema.ui.adapter.CarTypeAdapter;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;

import java.util.ArrayList;
import java.util.HashMap;


public class CarTypeListActivity extends LoadingActivity {

    private ListView car_type_list;//车款列表
    private CarTypeAdapter adapter;
    private Intent intent;
    private String optionid;//车型ID
    private static String brandid = "21";//车系ID
    private String carId;//车款ID
    private String carTitle;
    private String vinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_list);
        intent = getIntent();
        vinCode=intent.getStringExtra("vin");
        optionid = intent.getStringExtra("optionid");
        loadingDataUI();
        initComponent();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void initData() {
        CarTypeInfoListParser parser = new CarTypeInfoListParser(mCallback);
        HashMap<String, String> params = new HashMap<>();
        params.put("optionid", optionid);
        parser.executePost(URLConfig.getM_CAR_TYPE_LIST(), params);
    }

    private void initComponent() {
        initTitle("车款");
        car_type_list = $ViewByID(R.id.car_type_list);
        car_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarModeInfo modeInfo = (CarModeInfo) adapterView.getItemAtPosition(i);
                carId = modeInfo.getId();
                carTitle = modeInfo.getTitle();
                if (intent != null && intent.getBooleanExtra("switch", false)) {
                    switchCarType();
                } else {
                    addCarType();
                }
            }
        });
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        try {
            BaseResponseInfo<ArrayList<CarModeInfo>> baseResponseInfo = (BaseResponseInfo<ArrayList<CarModeInfo>>) bInfo;
            ArrayList<CarModeInfo> carModeInfos = (ArrayList<CarModeInfo>) baseResponseInfo.getValue();
            if (carModeInfos != null && carModeInfos.size() > 0) {
                adapter = new CarTypeAdapter(this, carModeInfos);
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

    private void addCarType() {
        PopBoxCreat.createDialogNotitle(CarTypeListActivity.this, null, "您选择的车型是" + carTitle, "取消", "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                DefaultStringParser parser = new DefaultStringParser(addResult);
                HashMap<String, String> params = new HashMap<>();
                params.put("brandid", brandid);
                params.put("optionid", optionid);
                params.put("carid", carId);
                parser.executePost(URLConfig.getM_CAR_ADD_CAR(), params);

            }
        });

    }

    BaseParser.ResultCallback addResult = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            LoginInfo.setCarname(carTitle);
            Intent intent = new Intent(CarTypeListActivity.this, DeviceBindActivity.class);
            intent.putExtra("cat_title", carTitle);
            if (!TextUtils.isEmpty(vinCode)) {
                intent.putExtra("vin", vinCode);
            }
            CarTypeListActivity.this.startActivity(intent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarTypeListActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarTypeListActivity.this, " 车型绑定失败");
            }
        }
    };

    private void switchCarType() {
        PopBoxCreat.createDialogNotitle(CarTypeListActivity.this, null, "您选择的车型是" + carTitle, "取消", "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                DefaultStringParser parser = new DefaultStringParser(switchResult);
                HashMap<String, String> params = new HashMap<>();
                params.put("brandid", brandid);
                params.put("optionid", optionid);
                params.put("carid", carId);
                parser.executePost(URLConfig.getM_SWITCHCAR_URL(), params);
            }
        });


    }

    BaseParser.ResultCallback switchResult = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            LoginInfo.setCarname(carTitle);
            UUToast.showUUToast(CarTypeListActivity.this, " 车型修改成功");
            ActivityControl.finishAllCarSelectActivity();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarTypeListActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarTypeListActivity.this, " 车型修改失败");
            }
        }
    };

}
