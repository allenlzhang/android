package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.ui.view.PasswordView;

public class VcodeResetRemotePasswdActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private PasswordView old_remote_passwd;
    private PasswordView new_remote_passwd;
    private PasswordView new_remote_passwd_again;

    private TextView reset_remote_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode_reset_remote_passwd);
    }

    @Override
    public void onClick(View view) {

    }
}
