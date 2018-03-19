package com.carlt.yema.ui.activity.login;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class ResetPasswdActivity extends BaseActivity implements View.OnClickListener{


    private EditText forget_passwd_phone_et;//手机号码
    private EditText verification_code_et;//验证码
    private EditText change_passwd_et;//输入修改密码
    private EditText change_passwd_again_et;//再次输入

    private ImageView back;//返回按钮
    private ImageView change_passwd_toggle;//显示&隐藏密码按钮
    private ImageView change_passwd_again_toggle;//显示&隐藏密码按钮

    private TextView bt_verification_send;//发送验证码按钮
    private TextView titleText;//页面标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);
        initComponent();
    }

    private void initComponent(){
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("忘记密码");

        forget_passwd_phone_et=findViewById(R.id.forget_passwd_phone_et);
        verification_code_et=findViewById(R.id.verification_code_et);
        change_passwd_et=findViewById(R.id.change_passwd_et);
        change_passwd_again_et=findViewById(R.id.change_passwd_again_et);

        change_passwd_toggle=findViewById(R.id.change_passwd_toggle);
        change_passwd_toggle.setOnClickListener(this);
        change_passwd_again_toggle=findViewById(R.id.change_passwd_again_toggle);
        change_passwd_again_toggle.setOnClickListener(this);

        bt_verification_send=findViewById(R.id.bt_verification_send);
        bt_verification_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_toggle:
                passwdChangeToggle(view.getTag().toString());
                break;
            case R.id.change_passwd_again_toggle:
                passwdChangeAgainToggle(view.getTag().toString());
                break;
            case R.id.bt_verification_send:
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private void passwdChangeToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                change_passwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                change_passwd_toggle.setTag("off");
            } else {
                change_passwd_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                change_passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                change_passwd_toggle.setTag("on");
            }
        }
    }

    private void passwdChangeAgainToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                change_passwd_again_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_passwd_again_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                change_passwd_again_toggle.setTag("off");
            } else {
                change_passwd_again_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                change_passwd_again_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                change_passwd_again_toggle.setTag("on");
            }
        }
    }

}
