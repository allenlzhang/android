package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.UUToast;

import java.util.HashMap;

public class NicknameEditActivity extends LoadingActivity implements View.OnClickListener{


    private EditText nickname_input;//里程输入框
    private TextView nickname_input_commit;//确认修改按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_edit);
        initComponent();
        initTitle("修改昵称");
    }

    private void initComponent(){

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
                if (TextUtils.isEmpty(nickname_input.getText().toString())) {
                    UUToast.showUUToast(this,"昵称不能为空");
                    return;
                } else if (nickname_input.getText().toString().length() > 16) {
                    UUToast.showUUToast(this,"最多可输入16位数字、字母、汉字");
                    return;
                }
                chanageNickNameRequest(nickname_input.getText().toString());
                break;
        }
    }

    private void chanageNickNameRequest(String info){
        DefaultStringParser parser =new DefaultStringParser(callback);
        HashMap<String,String> params=new HashMap<>();
        params.put("realname",info);
        parser.executePost(URLConfig.getM_USER_EDIT_INFO(),params);
        loadingDataUI();
    }

    private BaseParser.ResultCallback callback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            UUToast.showUUToast(NicknameEditActivity.this,"修改资料成功");
            Intent intent=new Intent(NicknameEditActivity.this,PersonInfoActivity.class);
            intent.putExtra("nickName",nickname_input.getText().toString());
            NicknameEditActivity.this.setResult(RESULT_OK,intent);
            LoginInfo.setRealname(nickname_input.getText().toString());
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadSuccessUI();
            UUToast.showUUToast(NicknameEditActivity.this,"修改资料失败");
        }
    };
}
