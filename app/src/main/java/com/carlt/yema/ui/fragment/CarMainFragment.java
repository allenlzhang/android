package com.carlt.yema.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseFragment;
import com.carlt.yema.R;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarIndexInfo;
import com.carlt.yema.data.remote.RemoteMainInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.CarOperationConfigParser;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.activity.carstate.CarSaftyListActivity;
import com.carlt.yema.ui.activity.carstate.CarStateNowActivity;
import com.carlt.yema.ui.activity.carstate.CarTiresStateActivity;
import com.carlt.yema.ui.activity.carstate.MainTestingActivity;
import com.carlt.yema.utils.ILog;

import java.util.HashMap;

/**
 * Created by liu on 2018/3/16.
 */

public class CarMainFragment extends BaseFragment implements View.OnClickListener {
    private static String TAG = "CarMainFragment";

    private CarIndexInfo carinfo;
    private View view1;
    private View view2;
    private View view3;
    private View viewSafetyLay;
    private TextView viewSafetyText;
    private View viewMainTainLay;
    private View viewMainState;
    private TextView headTxt;
    private TextView titleTV;

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_carmain, null, false);
        return view;
    }

    @Override
    public void init(View view) {
        //  胎压监测
        view1 = $ViewByID(R.id.car_main_txt_tire);
        // 定位寻车
        view2 = $ViewByID(R.id.car_main_txt_findcar);
        //导航同步
        view3 = $ViewByID(R.id.car_main_txt_carlocation);
        //安防提醒
        viewSafetyLay = $ViewByID(R.id.car_main_lay_safety);
        viewSafetyText = $ViewByID(R.id.car_main_txt_safety);
        //车况检测
        viewMainTainLay = $ViewByID(R.id.car_main_lay_maintain);
        //实时车况
        viewMainState = $ViewByID(R.id.car_state_iv);
        titleTV = $ViewByID(R.id.carmian_title);
        headTxt = $ViewByID(R.id.layout_sub_head_txt);//
        titleTV.setText("野马汽车品牌");
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        viewSafetyLay.setOnClickListener(this);
        viewMainTainLay.setOnClickListener(this);
        viewMainState.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    CarOperationConfigParser carOperationConfigParser;

    private void LoadData() {
        //CARINDEX 以及支持的配置项
        BaseParser parser = new DefaultParser<CarIndexInfo>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(Object bInfo) {
                carinfo = ((BaseResponseInfo<CarIndexInfo>) bInfo).getValue();
                remoteConfig();
                ILog.e(TAG, "onSuccess" + bInfo.toString());
            }

            @Override
            public void onError(Object bInfo) {
                ILog.e(TAG, "onError" + bInfo.toString());
                actLoadError((BaseResponseInfo) bInfo);
            }
        }, CarIndexInfo.class);
        HashMap params = new HashMap();
        parser.executePost(URLConfig.getM_CAR_MAIN_URL(), params);
    }

    private void remoteConfig() {
        if (YemaApplication.getInstanse().getRemoteMainInfo() == null) {
            carOperationConfigParser = new CarOperationConfigParser<String>(new BaseParser.ResultCallback() {
                @Override
                public void onSuccess(Object bInfo) {
                    YemaApplication.getInstanse().setRemoteMainInfo(carOperationConfigParser.getReturn());
                    ILog.e(TAG, "onSuccess parser2 " + carOperationConfigParser.getReturn());
                    loadSuss();
                }

                @Override
                public void onError(Object bInfo) {
                    ILog.e(TAG, "onError" + bInfo.toString());
                    actLoadError((BaseResponseInfo) bInfo);
                }
            });
            HashMap params2 = new HashMap();
            carOperationConfigParser.executePost(URLConfig.getM_CAR_CURCARCONFIG_URL(), params2);
        }else{
            loadSuss();
        }
    }

    private void loadSuss() {
        if (null != carinfo) {
            if (!TextUtils.isEmpty(carinfo.getCarname())) {
                titleTV.setText(carinfo.getCarname());
            }
            if (TextUtils.equals("1", carinfo.getIsrunning())) {//1 表示行驶中，0 表示不在行驶中
                headTxt.setText("您的爱车正在行驶中");
            } else if (TextUtils.equals("0", carinfo.getIsrunning())) {
                headTxt.setText("您的爱车正在休息");
            }
            if (!TextUtils.isEmpty(carinfo.getSafetymsg())) {
                viewSafetyText.setText(carinfo.getSafetymsg());
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.car_main_txt_tire://胎压检测
                Intent mIntent = new Intent(getActivity(), CarTiresStateActivity.class);
                startActivity(mIntent);
                break;
            case R.id.car_main_txt_findcar://定位寻车

                break;
            case R.id.car_main_txt_carlocation://导航同步

                break;
            case R.id.car_main_lay_safety://安防提醒
                Intent mIntent3 = new Intent(getActivity(), CarSaftyListActivity.class);
                startActivity(mIntent3);
                break;
            case R.id.car_main_lay_maintain://车况检测报告
                Intent mIntent4 = new Intent(getActivity(), MainTestingActivity.class);
                startActivity(mIntent4);
                break;
            case R.id.car_state_iv:
                Intent mIntent5 = new Intent(getActivity(), CarStateNowActivity.class);
                startActivity(mIntent5);
                break;
            default:
                break;
        }
    }
}
