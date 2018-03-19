package com.hz17car.yema.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseFragment;
import com.hz17car.yema.ui.activity.carstate.CarTiresStateActivity;

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
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.car_main_txt_tire:
                Intent mIntent = new Intent(getActivity(), CarTiresStateActivity.class);
                startActivity(mIntent);
                break;
            case R.id.car_main_txt_findcar:
                break;
            case R.id.car_main_txt_carlocation:
                break;
            default:
                break;
        }
    }
}
