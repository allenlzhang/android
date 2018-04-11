package com.carlt.yema.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marller on 2018\3\15 0015.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener, BaseParser.ResultCallback {


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

    }

    @Override
    protected void onResume() {

        isTimeOut = false;
        if (!TextUtils.isEmpty(LoginInfo.getMobile())) {
            user_phone.setText(LoginInfo.getMobile());
        }
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
                if (!TextUtils.isEmpty(user_passwd.getText().toString())) {
                    user_passwd.setSelection(user_passwd.getText().toString().length());
                }
                break;
            case R.id.login_commit:
                userPhone = user_phone.getText().toString();
                passwd = user_passwd.getText().toString();
                if (isInputDataInvalid(userPhone, passwd)) {
                    mDialog = PopBoxCreat.createDialogWithProgress(UserLoginActivity.this, "正在验证信息...");
                    mDialog.show();
                    mLoginInfo = new LoginInfo();
//                    login(userPhone, passwd);
                    CPControl.GetLogin(userPhone,passwd,this);
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
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    mUseInfo.setAccount(user_phone.getText().toString());
                    mUseInfo.setPassword(user_passwd.getText().toString());
                    UseInfoLocal.setUseInfo(mUseInfo);
                    break;
                case 1:
                    LoadErro(msg.obj);
                    break;

            }
        }
    };


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
     * 加载成功
     */
    private void loadSuccess(Object obj) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mUseInfo.setAccount(userPhone);
        mUseInfo.setPassword(passwd);
        UseInfoLocal.setUseInfo(mUseInfo);
        ActivityControl.initXG();
        //业务代码，为了测试暂时注释掉
        LoginControl.logic(this);
    }

    /**
     * 加载失败（野马项目没有授权流程）
     */
    private void LoadErro(Object erro) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        BaseResponseInfo mLoginInfo = (BaseResponseInfo) erro;

        if (mLoginInfo != null) {
           String info=mLoginInfo.getInfo();
            // 其它
            if (info != null && info.length() > 0) {
                UUToast.showUUToast(UserLoginActivity.this, info);
            } else {
                UUToast.showUUToast(UserLoginActivity.this, "登录失败...");
            }

        }
    }

    @Override
    public void onSuccess(BaseResponseInfo bInfo) {

        try {
            String dataValue = (String) bInfo.getValue();
            JSONObject mJSON_data = new JSONObject(dataValue);
            LoginControl.parseLoginInfo(mJSON_data);
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = new Message();
            bInfo.setInfo("解析失败");
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    }

    @Override
    public void onError(BaseResponseInfo bInfo) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = bInfo;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("info", "KeyEvent.KEYCODE_BACK--login");
            ActivityControl.onExit();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
