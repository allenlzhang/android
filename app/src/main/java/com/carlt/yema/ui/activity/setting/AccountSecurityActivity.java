package com.carlt.yema.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;

import java.util.HashMap;

public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private ImageView back;//返回
    private TextView title;//标题

    private TextView verified_phone;//显示手机号码的文本

    private View certified_phone_manage;//显示绑定手机的item
    private View logined_passwd_reset;//登录密码管理的item
    private View remote_passwd_reset;//远程控制密码管理item
    private CheckBox btn_remote_no_passwd_ctr;//远程控制无密解锁item
    private Dialog mDialog;

    private String phoneNum;//手机号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        initComponent();
        phoneNum=getIntent().getStringExtra("phone_num");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void initComponent() {
        title=$ViewByID(R.id.title);
        title.setText(getResources().getString(R.string.account_security));

        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        certified_phone_manage=$ViewByID(R.id.certified_phone_manage);
        certified_phone_manage.setOnClickListener(this);
        logined_passwd_reset=$ViewByID(R.id.logined_passwd_reset);
        logined_passwd_reset.setOnClickListener(this);
        remote_passwd_reset=$ViewByID(R.id.remote_passwd_reset);
        remote_passwd_reset.setOnClickListener(this);
        btn_remote_no_passwd_ctr=$ViewByID(R.id.btn_remote_no_passwd_ctr);
        btn_remote_no_passwd_ctr.setOnCheckedChangeListener(this);

        verified_phone=$ViewByID(R.id.verified_phone);
        StringBuilder builder=new StringBuilder(LoginInfo.getMobile());
        if (null!=phoneNum&&!TextUtils.isEmpty(phoneNum)){
            verified_phone.setText(builder.replace(3,7,"****"));
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.certified_phone_manage:
                Intent phoneManage=new Intent(this,PhoneAuthenticationActivity.class);
                startActivity(phoneManage);
                break;
            case R.id.logined_passwd_reset:
                Intent passwdReset=new Intent(this,LoginPasswdManageActivity.class);
                startActivity(passwdReset);
                break;
            case R.id.remote_passwd_reset:
                Intent remoteReset=new Intent(this,RemotePasswdManageActivity.class);
                startActivity(remoteReset);
                break;
        }
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        mDialog = PopBoxCreat.createDialogWithProgress(AccountSecurityActivity.this,
                "提交中...");
        mDialog.show();
        if (checked) {
            noPasswdRequest("1");
        } else {
            noPasswdRequest("0");
        }
    }

    private void noPasswdRequest(String lesspwd_switch){
        DefaultStringParser parser=new DefaultStringParser(noPasswdCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lesspwd_switch", lesspwd_switch);
        params.put("sound_switch", "0");
        parser.executePost(URLConfig.getM_USER_REMOTE_SWITCH(),params);
    }

    private BaseParser.ResultCallback noPasswdCallback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (bInfo != null) {
                String info2 = bInfo.getInfo();
                if (info2 != null && info2.length() > 0) {
                    UUToast.showUUToast(AccountSecurityActivity.this, info2);
                } else {
                    UUToast.showUUToast(AccountSecurityActivity.this, "操作成功！");
                }
            } else {
                UUToast.showUUToast(AccountSecurityActivity.this, "操作成功！");
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 授权状态更新失败
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            btn_remote_no_passwd_ctr.setOnCheckedChangeListener(null);
            if (bInfo != null) {
                String info3 = bInfo.getInfo();
                if (info3 != null && info3.length() > 0) {
                    UUToast.showUUToast(AccountSecurityActivity.this, info3);
                } else {
                    UUToast.showUUToast(AccountSecurityActivity.this, "操作失败...");
                }
            } else {
                UUToast.showUUToast(AccountSecurityActivity.this, "操作失败...");
            }
        }
    };

}
