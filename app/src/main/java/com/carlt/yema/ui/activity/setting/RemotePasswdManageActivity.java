package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class RemotePasswdManageActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回
    private TextView title;//标题

    private View remote_passwd_remember;//记得原密码
    private View remote_passwd_forget;//忘记原密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_passwd_manage);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        remote_passwd_remember=$ViewByID(R.id.remote_passwd_remember);
        remote_passwd_remember.setOnClickListener(this);
        remote_passwd_forget=$ViewByID(R.id.remote_passwd_forget);
        remote_passwd_forget.setOnClickListener(this);

        title=$ViewByID(R.id.title);
        title.setText("远程密码管理");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.remote_passwd_remember:
                Intent resetLoginPasswd=new Intent(this,ResetRemotePasswdActivity.class);
                startActivity(resetLoginPasswd);
                break;
            case R.id.remote_passwd_forget:
                Intent resetLoginPasswdByPhone=new Intent(this,VcodeResetRemotePasswdActivity.class);
                startActivity(resetLoginPasswdByPhone);
                break;
        }
        finish();
    }
}
