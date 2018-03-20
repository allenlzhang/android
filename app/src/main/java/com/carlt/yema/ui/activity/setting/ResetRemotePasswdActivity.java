package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.ui.view.PasswordView;

public class ResetRemotePasswdActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private PasswordView old_remote_passwd;
    private PasswordView new_remote_passwd;
    private PasswordView new_remote_passwd_again;

    private TextView reset_remote_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_remote_passwd);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);

        title=$ViewByID(R.id.title);
        title.setText("修改远程密码");

        old_remote_passwd=$ViewByID(R.id.old_remote_passwd);
        new_remote_passwd=$ViewByID(R.id.new_remote_passwd);
        new_remote_passwd_again=$ViewByID(R.id.new_remote_passwd_again);

        reset_remote_commit=$ViewByID(R.id.reset_remote_commit);
        reset_remote_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (R.id.back){
            case R.id.back:
                finish();
                break;
            case R.id.reset_remote_commit:
                isValid();
                break;
        }
    }

    private void isValid(){

    }
}
