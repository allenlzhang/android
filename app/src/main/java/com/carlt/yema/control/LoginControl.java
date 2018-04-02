package com.carlt.yema.control;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.carlt.yema.MainActivity;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.ui.activity.login.ActivateBindActivity;
import com.carlt.yema.ui.activity.login.DeviceBindActivity;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUUpdateDialog;


/**
 * 登录控制
 *
 * @author daisy
 */
public class LoginControl {
    public static Activity mCtx;
    public static UUUpdateDialog.DialogUpdateListener mDialogUpdateListener;

    public static void logic(final Activity mContext) {
        mCtx = mContext;
        YemaApplication.getInstanse().setIsshowupdata(false);
        String className = mContext.getClass().getName();
        // 判断是否绑定设备
        String s = LoginInfo.getDeviceidstring();
        Log.e("info", "deviceidstring==" + s);
        // 测试代码开始
        // s="1234567890123456";
        // 测试代码结束
        if (s != null && s.length() > 0 && !s.equals("null")) {
            // 已绑定设备,判断是否激活设备
            boolean isDeviceActivate = LoginInfo.isDeviceActivate();
            Log.e("info", "isDeviceActivate==" + isDeviceActivate);
            if (isDeviceActivate) {

                // 野马绑定和激活合并，如果激活直接进入主页
                Intent mainIntent = new Intent(mContext,
                        MainActivity.class);
                mainIntent.putExtra("page", "1");
                mContext.startActivity(mainIntent);

            } else {
                // 未激活设备
                String vin = LoginInfo.getVin(LoginInfo.getMobile());
                if (vin == null || vin.equals("")) {
                    Intent loginIntent = new Intent(mContext,
                            DeviceBindActivity.class);
                    loginIntent.putExtra("account", LoginInfo.getVin(LoginInfo.getMobile()));
                    mContext.startActivity(loginIntent);
                } else {
                    boolean isUpdating = LoginInfo.isUpgradeing();
                    // 是否需要升级
                    if (isUpdating) {
                        // 设备正在升级，跳转至升级页面
                        PopBoxCreat.showUUUpdateDialog(mContext,
                                new UUUpdateDialog.DialogUpdateListener() {

                                    @Override
                                    public void onSuccess() {
                                        LoginInfo.setUpgradeing(false);
                                        LoginControl.logic(mCtx);
                                        if (mDialogUpdateListener != null) {
                                            mDialogUpdateListener.onSuccess();
                                        }
                                    }

                                    @Override
                                    public void onFailed() {
                                        if (mDialogUpdateListener != null) {
                                            mDialogUpdateListener.onFailed();
                                        }
                                    }
                                });
                    } else {
                        // 设备不需要升级，跳转至激活盒子
                        Intent loginIntent = new Intent(mContext,
                                ActivateBindActivity.class);
                        loginIntent.putExtra("account", LoginInfo.getMobile());
                        mContext.startActivity(loginIntent);
                    }
                }

            }
        } else {
            Intent loginIntent = new Intent(mContext,
                    DeviceBindActivity.class);
            loginIntent.putExtra("account", LoginInfo.getMobile());
            mContext.startActivity(loginIntent);
        }
    }
}
