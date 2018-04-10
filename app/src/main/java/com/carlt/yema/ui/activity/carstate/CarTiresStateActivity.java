package com.carlt.yema.ui.activity.carstate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.SaftyMsgInfo;
import com.carlt.yema.data.remote.RemoteDirectPressureInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *胎压监测
 */
public class CarTiresStateActivity extends LoadingActivity {

    private TextView subHeadTxt;
    private ImageView tirePressureLay0;
    private ImageView tirePressureLay1;
    private ImageView tirePressureLay2;
    private ImageView tirePressureLay3;
    private TextView pa_tv0;
    private TextView pa_tv1;
    private TextView pa_tv2;
    private TextView pa_tv3;
    private TextView temp_tv0;
    private TextView temp_tv1;
    private TextView temp_tv2;
    private TextView temp_tv3;

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
        subHeadTxt = $ViewByID(R.id.layout_sub_head_txt);
        tirePressureLay0 = $ViewByID(R.id.activity_car_tire_pressure_lay0);
        tirePressureLay1 = $ViewByID(R.id.activity_car_tire_pressure_lay1);
        tirePressureLay2 = $ViewByID(R.id.activity_car_tire_pressure_lay2);
        tirePressureLay3 = $ViewByID(R.id.activity_car_tire_pressure_lay3);

        pa_tv0 = $ViewByID(R.id.l_t_pa_tv);
        pa_tv1 = $ViewByID(R.id.r_t_pa_tv);
        pa_tv2 = $ViewByID(R.id.l_b_pa_tv);
        pa_tv3 = $ViewByID(R.id.r_b_pa_tv);

        temp_tv0 = $ViewByID(R.id.l_t_temp_tv);
        temp_tv1 = $ViewByID(R.id.r_t_temp_tv);
        temp_tv2 = $ViewByID(R.id.l_b_temp_tv);
        temp_tv3 = $ViewByID(R.id.r_b_temp_tv);


    }

    private void initdata() {
        DefaultStringParser parser = new DefaultStringParser(mCallback);
        HashMap<String, String> param = new HashMap();
        param.put("move_device_name", LoginInfo.getDeviceidstring());
        parser.executePost(URLConfig.getM_REMOTE_DRIECTRRESSURE(), param);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        String value = (String) ((BaseResponseInfo) bInfo).getValue();
        Gson gson = new Gson();
        Type type = new TypeToken<List<RemoteDirectPressureInfo>>() {
        }.getType();
        List<RemoteDirectPressureInfo> remoteDirectPressureInfos = gson.fromJson(value, type);
        if (null == remoteDirectPressureInfos || remoteDirectPressureInfos.size() == 0) {
            loadNodataUI();
        } else {
            showData(remoteDirectPressureInfos);
        }
    }

    private void showData(List<RemoteDirectPressureInfo> remoteDirectPressureInfos) {
        subHeadTxt.setText("胎压胎温正常");
        // 胎压状态，1：正常；0：异常
        for (int i = 0; i < remoteDirectPressureInfos.size(); i++) {
            int pressure_status = remoteDirectPressureInfos.get(i).getPressure_status();
            String pa = remoteDirectPressureInfos.get(i).getPressure_value() + remoteDirectPressureInfos.get(i).getPressure_unit();
            String temp = remoteDirectPressureInfos.get(i).getTemperature_value() + remoteDirectPressureInfos.get(i).getTemperature_unit();
            //四个轮胎,赋值
            if (i == 0) {   //左前
                pa_tv0.setText(pa);
                temp_tv0.setText(temp);
            } else if (i == 1) {//右前
                pa_tv1.setText(pa);
                temp_tv1.setText(temp);
            } else if (i == 2) {//左后
                pa_tv2.setText(pa);
                temp_tv2.setText(temp);
            } else if (i == 3) { //右后
                pa_tv3.setText(pa);
                temp_tv3.setText(temp);
            }else{
                //么有五个轮胎
            }

            //检测是否正常
            if (pressure_status != 1) {
                subHeadTxt.setText("胎压异常!请及时查看!");
                //四个轮胎
                if (i == 0) {   //左前
                    tirePressureLay0.setBackgroundResource(R.drawable.tire_err);
                    pa_tv0.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv0.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 1) {//右前
                    tirePressureLay1.setBackgroundResource(R.drawable.tire_err);
                    pa_tv1.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv1.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 2) {//左后
                    tirePressureLay2.setBackgroundResource(R.drawable.tire_err);
                    pa_tv2.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv2.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 3) { //右后
                    tirePressureLay3.setBackgroundResource(R.drawable.tire_err);
                    pa_tv3.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv3.setTextColor(getResources().getColor(R.color.text_tire_err));
                }else{
                    //么有五个轮胎
                }
            }else{
                //四个轮胎
                if (i == 0) {   //左前
                    tirePressureLay0.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 1) {//右前
                    tirePressureLay1.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 2) {//左后
                    tirePressureLay2.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 3) { //右后
                    tirePressureLay3.setBackgroundResource(R.drawable.tire_nol);
                }else{
                    //么有五个轮胎
                }
            }
        }

    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initdata();
    }
}
