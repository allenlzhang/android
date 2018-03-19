package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class NicknameEditActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private EditText nickname_input;//里程输入框
    private TextView nickname_input_commit;//确认修改按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_edit);
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        title.setText("修改昵称");

        nickname_input=findViewById(R.id.nickname_input);

        nickname_input_commit=findViewById(R.id.nickname_input_commit);
        nickname_input_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.nickname_input_commit:
                break;
        }
    }
}
