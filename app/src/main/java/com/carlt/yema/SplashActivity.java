package com.carlt.yema;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.http.HttpLinker;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.activity.login.UserLoginActivity;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.CipherUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity implements Callback {

    private LoginInfo mLoginInfo;
    private Dialog mDialog;// 加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash();
    }

    private void splash(){
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        if (TextUtils.isEmpty(mUseInfo.getAccount()) || TextUtils.isEmpty(mUseInfo.getPassword())) {
            Intent loginIntent = new Intent(this, UserLoginActivity.class);
            startActivity(loginIntent);
        } else if (!LoginInfo.isDeviceActivate()) {
            LoginControl.logic(this);
        } else {
            login(mUseInfo.getAccount(),mUseInfo.getPassword());
        }
        finish();
    }

    /**
     * 登录
     */
    private void login(String userPhone, String passwd) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("version", YemaApplication.Version + "");
        mMap.put("mobile", userPhone);
        mMap.put("password", CipherUtils.md5(passwd));
        mMap.put("move_deviceid", YemaApplication.NIMEI);
        mMap.put("move_device_name", YemaApplication.MODEL_NAME);
        mMap.put("move_model", YemaApplication.MODEL);
        mMap.put("softtype", "android");
        StringBuffer sysinfo = new StringBuffer(YemaApplication.ANDROID_VERSION);
        sysinfo.append(",");
        sysinfo.append(YemaApplication.DISPLAY);
        sysinfo.append(",");
        sysinfo.append(YemaApplication.MODEL_NAME);
        mMap.put("sysinfo", sysinfo.toString());
        String url = URLConfig.getM_LOGIN_URL();
        HttpLinker.post(url, mMap, this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadSuccess(msg.obj);
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    break;
                case 1:
                    LoadErro(msg.obj);
                    break;

            }
        }
    };

    @Override
    public void onFailure(Call call, IOException e) {
        mLoginInfo.setInfo(BaseParser.MSG_ERRO);
        Message msg = new Message();
        msg.what = 1;
        msg.obj = mLoginInfo;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (null==response) {
            UUToast.showUUToast(this,BaseResponseInfo.NET_ERROR);
            return;
        }
        String content = response.body().string();
        try {
            JSONObject mJson = new JSONObject(content);
            JSONObject mJSON_data = mJson.getJSONObject("data");
            LoginControl.parseLoginInfo(mJSON_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        mLoginInfo = gson.fromJson(content, LoginInfo.class);
        if (mLoginInfo != null) {
            if (response.isSuccessful() && mLoginInfo.getFlag() == 200) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = mLoginInfo;
                mHandler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = mLoginInfo;
                mHandler.sendMessage(msg);
            }
        } else {
            UUToast.showUUToast(this, BaseResponseInfo.NET_ERROR);
        }
    }

    /**
     * 加载成功
     */
    private void loadSuccess(Object obj) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        ActivityControl.initXG();
        //业务代码，为了测试暂时注释掉
        splash();
    }

    /**
     * 加载失败（野马项目没有授权流程）
     */
    private void LoadErro(Object erro) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        LoginInfo mLoginInfo = (LoginInfo) erro;

        if (mLoginInfo != null) {
            String info=mLoginInfo.getInfo();
            // 其它
            if (info != null && info.length() > 0) {
                UUToast.showUUToast(SplashActivity.this, info);
            } else {
                UUToast.showUUToast(SplashActivity.this, "登录失败...");
            }

        }
    }
}
