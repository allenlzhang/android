package com.carlt.yema.ui.activity.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carlt.yema.MainActivity;
import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.http.HttpLinker;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUAuthorDialog;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.CipherUtils;
import com.carlt.yema.utils.CreatPostString;
import com.carlt.yema.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by marller on 2018\3\15 0015.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener, Callback {


    private TextView login_version;//版本信息
    private TextView forgot_passwd;//忘记密码
    private TextView user_regist;//用户注册
    private TextView login_commit;//登录按钮

    private ImageView login_logo;//野马Logo图片
    private ImageView passwd_toggle;//显示密码

    private EditText user_phone;//用户账号（手机）
    private EditText user_passwd;//用户密码

    private String userPhone;
    private String passwd;
    private LoginInfo mLoginInfo;
    private UseInfo mUseInfo;// 本地记录用户使用app情况

    private Dialog mDialog;// 加载

    public static boolean isTimeOut;// 倒计时是否结束

    public final static int ERRO_CODE_NOEXIST = 1010;// 用户不存在


    public final static int ERRO_CODE_DATA = 1025;// 用户名或密码错误


    UUAuthorDialog mAuthorDialog;

    private static final String TAG = "UserLoginActivity";

    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initComponent();
        mUseInfo = UseInfoLocal.getUseInfo();
        resultIntent = getIntent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void initComponent() {
        login_version = (TextView) findViewById(R.id.login_version);
        login_version.setText(YemaApplication.VersionName);
        forgot_passwd = (TextView) findViewById(R.id.forgot_passwd);
        forgot_passwd.setOnClickListener(this);
        user_regist = (TextView) findViewById(R.id.user_regist);
        user_regist.setOnClickListener(this);
        login_commit = (TextView) findViewById(R.id.login_commit);
        login_commit.setOnClickListener(this);

        login_logo = findViewById(R.id.login_logo);

        passwd_toggle = findViewById(R.id.passwd_toggle);
        passwd_toggle.setOnClickListener(this);

        user_phone = findViewById(R.id.user_phone);
        user_passwd = findViewById(R.id.user_passwd);
        if (resultIntent != null) {
            if (!TextUtils.isEmpty(resultIntent.getStringExtra("account"))) {
                user_phone.setText(resultIntent.getStringExtra("account"));
            }
        }

    }

    @Override
    protected void onResume() {

        isTimeOut = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.passwd_toggle:
                passwdToggle(view.getTag().toString());
                break;
            case R.id.login_commit:
                userPhone = user_phone.getText().toString();
                passwd = user_passwd.getText().toString();
                if (isInputDataInvalid(userPhone, passwd)) {
                    mDialog = PopBoxCreat.createDialogWithProgress(UserLoginActivity.this, "正在验证信息...");
                    mDialog.show();
                    mLoginInfo = new LoginInfo();
                    login(userPhone, passwd);
                }
                break;
            case R.id.user_regist:
                Intent registerIntent = new Intent(this, UserRegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.forgot_passwd:
                Intent resetPasswdIntent = new Intent(this, ResetPasswdActivity.class);
                startActivity(resetPasswdIntent);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadSuccess(msg.obj);
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
        Log.d(TAG, "__onResponse__" + response.toString());
        String content = response.body().string();
        try {
            JSONObject mJson = new JSONObject(content);
            JSONObject mJSON_data = mJson.getJSONObject("data");
            JSONObject mJSON_data2 = mJSON_data.getJSONObject("member");
            String access_token = mJSON_data2.getString("access_token");
            LoginInfo.setAccess_token(access_token);
            YemaApplication.TOKEN = access_token;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        mLoginInfo = gson.fromJson(content, LoginInfo.class);

        if (response.isSuccessful() && mLoginInfo.getFlag() == 200) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = mLoginInfo;
            mHandler.sendMessage(msg);
        } else {
            mLoginInfo.setInfo(BaseParser.MSG_ERRO + mLoginInfo.getFlag());
            Message msg = new Message();
            msg.what = 1;
            msg.obj = mLoginInfo;
            mHandler.sendMessage(msg);
        }
    }

    private void passwdToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                user_passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                passwd_toggle.setTag("off");
            } else {
                user_passwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                passwd_toggle.setTag("on");
            }
        }
    }

    /**
     * 判断用户输入是否合法
     */
    private boolean isInputDataInvalid(String phoneNum, String passwd) {
        if (TextUtils.isEmpty(user_phone.getText().toString()) && !StringUtils.checkCellphone(phoneNum)) {
            UUToast.showUUToast(this, "手机号码不正确", Toast.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(user_passwd.getText().toString())) {
            UUToast.showUUToast(this, "密码不正确", Toast.LENGTH_LONG);
            return false;
        }
        return true;
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


    /**
     * 跳转绑定
     */
    public void accountBinding(Context mCtx) {
        String className = mContext.getClass().getName();
        // 判断是否绑定设备
        String deviceInfo = LoginInfo.getDeviceidstring();
        if (deviceInfo != null && deviceInfo.length() > 0 && !deviceInfo.equals("null")) {
            // 已绑定设备,判断是否激活设备
            boolean isDeviceActivate = LoginInfo.isDeviceActivate();
            Log.e("info", "isDeviceActivate==" + isDeviceActivate);
            if (isDeviceActivate) {
                // 已激活设备，
                Intent carPage = new Intent(mCtx, MainActivity.class);
                carPage.putExtra("page", "1");
                startActivity(carPage);
            } else {
                // 未激活设备
                Intent bindDevice = new Intent(mContext,
                        DeviceBindActivity.class);
                bindDevice.putExtra("vin", user_phone.getText().toString());
                mContext.startActivity(bindDevice);
            }
        } else {
            Intent bindDevice = new Intent(mContext,
                    DeviceBindActivity.class);
            bindDevice.putExtra("vin", user_phone.getText().toString());
            mContext.startActivity(bindDevice);

        }
    }

    /**
     * 获取Token
     */
    private void getToken() {
        String url = URLConfig.getM_USER_ACCESSTOKEN();
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        String account = mUseInfo.getAccount() + "";
        String password = mUseInfo.getPassword();
        // Post参数
        String md5 = CipherUtils.md5(password + "");
        Log.i("DEBUG", md5);
        HashMap<String, String> params = CreatPostString.getToken(account, md5);
        HttpLinker.post(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginInfo.setInfo(BaseParser.MSG_ERRO);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();

                Gson gson = new Gson();
                mLoginInfo = gson.fromJson(content, LoginInfo.class);
                if (mLoginInfo.getFlag() == 200) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = mLoginInfo;
                    mHandler.sendMessage(msg);
                } else {
                    mLoginInfo.setInfo(BaseParser.MSG_ERRO + mLoginInfo.getFlag());
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = mLoginInfo;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 加载成功
     */
    private void loadSuccess(Object obj) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mUseInfo.setAccount(userPhone);
        mUseInfo.setPassword(passwd);
        UseInfoLocal.setUseInfo(mUseInfo);
        //TODO TEST DATA
        Intent carPage = new Intent(this, MainActivity.class);
        carPage.putExtra("page", "1");
        startActivity(carPage);
//        accountBinding(this);
    }

    /**
     * 加载失败（野马项目没有授权流程）
     */
    private void LoadErro(Object erro) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) erro;

        if (mBaseResponseInfo != null) {
            String info = mBaseResponseInfo.getInfo();
            int flag = mBaseResponseInfo.getFlag();

            // 其它
            if (info != null && info.length() > 0) {
                UUToast.showUUToast(UserLoginActivity.this, info);
            } else {
                UUToast.showUUToast(UserLoginActivity.this, "登录失败...");
            }

        }
    }

}
