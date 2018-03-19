package com.carlt.yema.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.carlt.yema.base.BaseFragment;
import com.carlt.yema.R;
import com.carlt.yema.ui.activity.carstate.CarSaftyListActivity;
import com.carlt.yema.ui.activity.carstate.CarStateNowActivity;
import com.carlt.yema.ui.activity.carstate.CarTiresStateActivity;
import com.carlt.yema.ui.activity.carstate.MainTestingActivity;

/**
 * Created by liu on 2018/3/16.
 */

public class CarMainFragment extends BaseFragment implements View.OnClickListener{
    @Override
    protected View inflateView(LayoutInflater inflater) {
       View view = inflater.inflate(R.layout.layout_carmain,null,false);
        return view;
    }

    @Override
    public void init(View view) {
        View view1 = $ViewByID(R.id.car_main_txt_tire); //  胎压监测
        View view2 = $ViewByID(R.id.car_main_txt_findcar);// 定位寻车
        View view3 = $ViewByID(R.id.car_main_txt_carlocation);//导航同步
        View viewSafetyLay = $ViewByID(R.id.car_main_lay_safety);//安防提醒
        View viewMainTainLay = $ViewByID(R.id.car_main_lay_maintain);//车况检测
        View viewMainState = $ViewByID(R.id.car_state_iv);//实时车况
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        viewSafetyLay.setOnClickListener(this);
        viewMainTainLay.setOnClickListener(this);
        viewMainState.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
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
