package com.hz17car.yema.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hz17car.yema.R;
import com.hz17car.yema.YemaApplication;
import com.hz17car.yema.base.BaseActivity;
import com.hz17car.yema.ui.view.UUToast;

/**
 * Created by marller on 2018\3\15 0015.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {


    private TextView login_version;//版本信息
    private TextView forgot_passwd;//忘记密码
    private TextView user_regist;//用户注册
    private TextView login_commit;//登录按钮

    private ImageView login_logo;//野马Logo图片
    private ImageView passwd_toggle;//显示密码

    private EditText user_phone;//用户账号（手机）
    private EditText user_passwd;//用户密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initComponent();
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
                if (isInputDataInvalid()){

                }
                break;
            case R.id.user_regist:
                Intent registerIntent=new Intent(this,UserRegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.forgot_passwd:
                Intent resetPasswdIntent=new Intent(this,ResetPasswdActivity.class);
                startActivity(resetPasswdIntent);
                break;
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
     *
     * */
    private boolean isInputDataInvalid(){
        if (TextUtils.isEmpty(user_phone.getText().toString())) {
            UUToast.showUUToast(this,"手机号码不正确",Toast.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(user_passwd.getText().toString())) {
            UUToast.showUUToast(this,"密码不正确",Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

}
