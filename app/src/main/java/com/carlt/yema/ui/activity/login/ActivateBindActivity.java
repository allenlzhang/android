package com.carlt.yema.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class ActivateBindActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回按钮

    private TextView titleText;//页面标题
    private TextView activate_commit;//绑定激活按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_bind);
        initComponent();

    }

    private void initComponent(){
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("激活设备");

        activate_commit=findViewById(R.id.activate_commit);
        activate_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
