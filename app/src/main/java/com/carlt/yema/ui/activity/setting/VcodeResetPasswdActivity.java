package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;


public class VcodeResetPasswdActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private EditText phoneNumber;//手机号码输入框
    private EditText verficationCode;//验证码输入框
    private EditText passwd;//密码输入框
    private EditText passwd2St;//再次输入密码输入框

    private ImageView passwdToggle;//是否显示密码
    private ImageView passwd2StToggle;//再次输入是否显示密码

    private TextView commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertified_code_reset_passwd);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        commit=$ViewByID(R.id.verification_reset_passwd_commit);
        commit.setOnClickListener(this);
        passwdToggle=$ViewByID(R.id.verification_new_passwd_input_toggle);
        passwdToggle.setOnClickListener(this);
        passwd2StToggle=$ViewByID(R.id.verification_new_passwd_input_again_toggle);
        passwd2StToggle.setOnClickListener(this);

        phoneNumber=$ViewByID(R.id.verification_passwd_phone);
        verficationCode=$ViewByID(R.id.verification_passwd_vcode_input);
        passwd=$ViewByID(R.id.verification_new_passwd_input);
        passwd2St=$ViewByID(R.id.verification_new_passwd_again_input);

        title=$ViewByID(R.id.title);
        title.setText("修改登录密码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verification_reset_passwd_commit:
                isValid();
                break;
            case R.id.verification_new_passwd_input_toggle:
                isValid();
                break;
            case R.id.verification_new_passwd_input_again_toggle:
                isValid();
                break;

        }
    }

    /**
     * 判断原始密码、新密码、再次输入新密码是否合法
     * */
    public void isValid(){

    }
}
