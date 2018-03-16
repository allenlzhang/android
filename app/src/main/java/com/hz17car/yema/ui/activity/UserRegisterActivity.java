package com.hz17car.yema.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseActivity;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回按钮

    private EditText register_phone_input;//手机号码
    private EditText register_vcode_input;//验证码
    private EditText register_passwd_et;//输入修改密码
    private EditText register_passwd_again_et;//再次输入

    private ImageView register_passwd_toggle;//显示&隐藏密码按钮
    private ImageView register_passwd_again_toggle;//显示&隐藏密码按钮

    private TextView titleText;//页面标题
    private TextView register_verification_send;//发送验证码按钮
    private TextView register_commit;//确认修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initComponent();
    }

    private void initComponent(){
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("注册");

        register_phone_input=findViewById(R.id.register_phone_input);
        register_vcode_input=findViewById(R.id.register_vcode_input);
        register_passwd_et=findViewById(R.id.register_passwd_et);
        register_passwd_again_et=findViewById(R.id.register_passwd_again_et);

        register_passwd_toggle=findViewById(R.id.register_passwd_toggle);
        register_passwd_toggle.setOnClickListener(this);
        register_passwd_again_et=findViewById(R.id.register_passwd_again_et);
        register_passwd_again_et.setOnClickListener(this);

        register_verification_send=findViewById(R.id.register_verification_send);
        register_verification_send.setOnClickListener(this);

        register_commit=findViewById(R.id.register_commit);
        register_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_toggle:
                passwdInputToggle(view.getTag().toString());
                break;
            case R.id.change_passwd_again_toggle:
                passwdInputAgainToggle(view.getTag().toString());
                break;
            case R.id.bt_verification_send:
                break;
            case R.id.register_commit:
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private void passwdInputToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                register_passwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                register_passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                register_passwd_toggle.setTag("off");
            } else {
                register_passwd_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                register_passwd_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                register_passwd_toggle.setTag("on");
            }
        }
    }

    private void passwdInputAgainToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                register_passwd_again_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                register_passwd_again_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                register_passwd_again_toggle.setTag("off");
            } else {
                register_passwd_again_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                register_passwd_again_toggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                register_passwd_again_toggle.setTag("on");
            }
        }
    }
}
