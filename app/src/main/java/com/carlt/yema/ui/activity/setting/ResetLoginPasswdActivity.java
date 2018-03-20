package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;


public class ResetLoginPasswdActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;;

    private EditText old_passwd_input;//原始密码输入框
    private EditText new_passwd_input;//新密码密码输入框
    private EditText new_passwd_again_input;//新密码再次输入框

    private TextView reset_passwd_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_login_passwd);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        reset_passwd_commit=$ViewByID(R.id.reset_passwd_commit);
        reset_passwd_commit.setOnClickListener(this);

        old_passwd_input=$ViewByID(R.id.old_passwd_input);
        new_passwd_input=$ViewByID(R.id.new_passwd_input);
        new_passwd_again_input=$ViewByID(R.id.new_passwd_again_input);

        title=$ViewByID(R.id.title);
        title.setText("修改登录密码");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.reset_passwd_commit:
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
