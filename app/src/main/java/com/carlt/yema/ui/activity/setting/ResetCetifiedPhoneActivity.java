package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class ResetCetifiedPhoneActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回按钮
    private TextView title;//标题

    private EditText reset_phone_input;//新手机号
    private EditText reset_certified_code_input;//验证码

    private TextView reset_phone_commit;//修改提交

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);
        initComponent();
    }

    private void initComponent() {
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        reset_phone_commit=findViewById(R.id.certified_input_commit);
        reset_phone_commit.setOnClickListener(this);

        title=findViewById(R.id.title);
        title.setText("绑定手机");
        reset_phone_input=findViewById(R.id.certified_phone_input);
        reset_certified_code_input=findViewById(R.id.certified_code_input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.reset_phone_commit:
                Intent resetCertified=new Intent();
                startActivity(resetCertified);
                break;
        }
    }
}
