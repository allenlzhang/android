package com.carlt.yema.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.ui.activity.setting.AboutYemaActivity;
import com.carlt.yema.ui.activity.setting.AccountSecurityActivity;
import com.carlt.yema.ui.activity.setting.CarManagerActivity;
import com.carlt.yema.ui.activity.setting.DeviceManageActivity;
import com.carlt.yema.ui.activity.setting.GeneralManageActivity;
import com.carlt.yema.ui.activity.setting.MsgManageActivity;
import com.carlt.yema.ui.activity.setting.PersonInfoActivity;
import com.carlt.yema.ui.activity.setting.TravelAlbumActivity;
import com.carlt.yema.utils.DensityUtil;


/**
 * Created by marller on 2018\3\14 0014.
 */

public class SettingMainFragment extends Fragment implements View.OnClickListener {

    public static SettingMainFragment newInstance() {
        return new SettingMainFragment();
    }

    private View btn_person_info;//用户信息item
    private View btn_travel_album;//旅行相册item
    private View btn_account_security;//账号与安全item
    private View btn_car_manager;//车辆管理item
    private View btn_msg_manager;//消息管理item
    private View btn_device_manager;//设备管理item
    private View btn_general_manager;//通用管理item
    private View btn_clean_cache;//清除缓存item
    private View btn_contact_us;//联系我们item
    private View btn_about_yema;//关于item

    private TextView btn_sign_out;//退出登录按钮


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.activity_setting_main, container, false);
        initComponent(parent);
        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initComponent(View parent) {
        btn_person_info = parent.findViewById(R.id.btn_person_info);
        btn_person_info.setOnClickListener(this);
        btn_travel_album = parent.findViewById(R.id.btn_travel_album);
        btn_travel_album.setOnClickListener(this);
        btn_account_security = parent.findViewById(R.id.btn_account_security);
        btn_account_security.setOnClickListener(this);
        btn_car_manager = parent.findViewById(R.id.btn_car_manager);
        btn_car_manager.setOnClickListener(this);
        btn_msg_manager = parent.findViewById(R.id.btn_msg_manager);
        btn_msg_manager.setOnClickListener(this);
        btn_device_manager = parent.findViewById(R.id.btn_device_manager);
        btn_device_manager.setOnClickListener(this);
        btn_general_manager = parent.findViewById(R.id.btn_general_manager);
        btn_general_manager.setOnClickListener(this);
        btn_clean_cache = parent.findViewById(R.id.btn_clean_cache);
        btn_clean_cache.setOnClickListener(this);
        btn_contact_us = parent.findViewById(R.id.btn_contact_us);
        btn_contact_us.setOnClickListener(this);
        btn_about_yema = parent.findViewById(R.id.btn_about_yema);
        btn_about_yema.setOnClickListener(this);
        btn_sign_out = parent.findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_person_info:
                Intent personInfo = new Intent(this.getActivity(), PersonInfoActivity.class);
                startActivity(personInfo);
                break;
            case R.id.btn_travel_album:
                Intent travelAlbum = new Intent(this.getActivity(), TravelAlbumActivity.class);
                startActivity(travelAlbum);
                break;
            case R.id.btn_account_security:
                Intent certifiedIntent = new Intent(this.getActivity(), AccountSecurityActivity.class);
                startActivity(certifiedIntent);
                break;
            case R.id.btn_car_manager:
                Intent carManager = new Intent(this.getActivity(), CarManagerActivity.class);
                startActivity(carManager);
                break;
            case R.id.btn_msg_manager:
                Intent msgManager = new Intent(this.getActivity(), MsgManageActivity.class);
                startActivity(msgManager);
                break;
            case R.id.btn_device_manager:
                Intent devManager = new Intent(this.getActivity(), DeviceManageActivity.class);
                startActivity(devManager);
                break;
            case R.id.btn_general_manager:
                Intent generalManager = new Intent(this.getActivity(), GeneralManageActivity.class);
                startActivity(generalManager);
                break;
            case R.id.btn_clean_cache:
                showCleanCacheDialog();
                break;
            case R.id.btn_contact_us:
                break;
            case R.id.btn_about_yema:
                Intent aboutYema = new Intent(this.getActivity(), AboutYemaActivity.class);
                startActivity(aboutYema);
                break;
            case R.id.btn_sign_out:

                break;
            case R.id.clean_cache_cancel:

                break;
            case R.id.clean_cache_confirm:

                break;
        }
    }

    private void showCleanCacheDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.CleanCacheDialog);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.clean_cache_dialog,null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.CENTER);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = DensityUtil.dip2px(getActivity(),286);
        //设置窗口高度为包裹内容
        lp.height = DensityUtil.dip2px(getActivity(),159);
        lp.alpha=0.9f;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
          dialogView.findViewById(R.id.clean_cache_cancel).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (dialog.isShowing()) {
                      dialog.dismiss();
                  }
              }
          });
          dialogView.findViewById(R.id.clean_cache_confirm).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  cleanCache();
                  if (dialog.isShowing()) {
                      dialog.dismiss();
                  }
              }
          });
        dialog.show();

    }

    private void cleanCache() {
    }
}
