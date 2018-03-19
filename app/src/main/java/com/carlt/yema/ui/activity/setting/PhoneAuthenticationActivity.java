package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class PhoneAuthenticationActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回按钮
    private TextView title;//标题

    private EditText certified_phone_input;//原手机号
    private EditText certified_code_input;//验证码

    private TextView certified_input_commit;//鉴权提交

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);
        initComponent();
    }

    private void initComponent() {
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        certified_input_commit=findViewById(R.id.certified_input_commit);
        certified_input_commit.setOnClickListener(this);

        title=findViewById(R.id.title);
        title.setText("身份认证");
        certified_phone_input=findViewById(R.id.certified_phone_input);
        certified_code_input=findViewById(R.id.certified_code_input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.certified_input_commit:
                Intent resetCertified=new Intent(this,ResetCetifiedPhoneActivity.class);
                startActivity(resetCertified);
                break;
        }
    }
}
