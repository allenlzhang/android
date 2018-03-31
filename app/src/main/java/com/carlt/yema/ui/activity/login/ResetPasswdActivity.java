package com.carlt.yema.ui.activity.login;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ResetPasswdActivity extends BaseActivity implements View.OnClickListener{


    private EditText forget_passwd_phone_et;//手机号码
    private EditText verification_code_et;//验证码
    private EditText change_passwd_et;//输入修改密码
    private EditText change_passwd_again_et;//再次输入

    private ImageView back;//返回按钮
    private CheckedTextView change_passwd_toggle;//显示&隐藏密码按钮
    private CheckedTextView change_passwd_again_toggle;//显示&隐藏密码按钮

    private TextView bt_verification_send;//发送验证码按钮
    private TextView titleText;//页面标题
    private TextView change_passwd_commit;//提交密码修改
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);
        initComponent();
    }

    private void initComponent(){
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("忘记密码");

        forget_passwd_phone_et=findViewById(R.id.forget_passwd_phone_et);
        verification_code_et=findViewById(R.id.verification_code_et);
        change_passwd_et=findViewById(R.id.change_passwd_et);
        change_passwd_again_et=findViewById(R.id.change_passwd_again_et);

        change_passwd_toggle=findViewById(R.id.change_passwd_toggle);
        change_passwd_toggle.setOnClickListener(this);
        change_passwd_again_toggle=findViewById(R.id.change_passwd_again_toggle);
        change_passwd_again_toggle.setOnClickListener(this);

        bt_verification_send=findViewById(R.id.bt_verification_send);
        bt_verification_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_toggle:
                change_passwd_toggle.toggle();
                passwdInputToggle();
                break;
            case R.id.change_passwd_again_toggle:
                change_passwd_again_toggle.toggle();
                passwdInputAgainToggle();
                break;
            case R.id.bt_verification_send:
                String cellPhone = forget_passwd_phone_et.getText().toString();
                if (TextUtils.isEmpty(cellPhone) || !StringUtils.checkCellphone(cellPhone)) {
                    UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
                } else {
                    CPControl.GetMessageValidateResult("1", cellPhone, validateCodeListener);
                    count = 60;
                    bt_verification_send.setText(count + "秒后重发");
                    bt_verification_send.setClickable(false);
                    bt_verification_send.setBackgroundResource(R.mipmap.btn_code_gray);

                    task = new TimerTask() {

                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 10;
                            mHandler.sendMessage(msg);

                        }
                    };
                    timer.schedule(task, 1000, 1000);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.change_passwd_commit:
                String commitPhone = forget_passwd_phone_et.getText().toString();
                String commitVCode = verification_code_et.getText().toString();
                String passwd = change_passwd_et.getText().toString();
                String passwdAgain = change_passwd_again_et.getText().toString();

                if (!isCommitInvalid(commitPhone, commitVCode, passwd, passwdAgain)) return;

                mDialog = PopBoxCreat
                        .createDialogWithProgress(ResetPasswdActivity.this, "正在加载");
                mDialog.show();
                commitPasswdReset(commitPhone,commitVCode,passwdAgain);
                break;
        }
    }

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo = null;
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(ResetPasswdActivity.this, msg.obj.toString());
                    break;
                case 1:
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    bt_verification_send.setClickable(true);
                    bt_verification_send.setText(R.string.usercenter_push_validate1);
                    bt_verification_send.setBackgroundResource(R.drawable.verification_send_bg);

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    int flag = mBaseResponseInfo.getFlag();
                    if (flag == BaseResponseInfo.PHONE_REGISTERED) {
                        UUToast.showUUToast(ResetPasswdActivity.this, "该手机号已存在:"
                                + mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(ResetPasswdActivity.this, "验证码获取失败:"
                                + mBaseResponseInfo.getInfo());
                    }
                    break;
                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    mUseInfo.setAccount(forget_passwd_phone_et.getText().toString());
                    mUseInfo.setPassword(mBaseResponseInfo.getValue().toString());
                    UseInfoLocal.setUseInfo(mUseInfo);

                    UUToast.showUUToast(ResetPasswdActivity.this, "注册成功！");
                    LoginInfo.setPin(forget_passwd_phone_et.getText().toString(), "");
                    LoginControl.logic(ResetPasswdActivity.this);
                    finish();
                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    UUToast.showUUToast(ResetPasswdActivity.this, "不好意思，注册失败，请稍候再试:"
                            + mBaseResponseInfo.getInfo());
                    break;
                case 10:
                    count--;
                    if (count > 0) {
                        bt_verification_send.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        bt_verification_send.setClickable(true);
                        bt_verification_send.setText(R.string.usercenter_push_validate1);
                        bt_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    BaseParser.ResultCallback callback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private BaseParser.ResultCallback validateCodeListener = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = ResetPasswdActivity.this.getResources().getString(R.string.vcode_send_success);
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private void commitPasswdReset(String phone,String vCode,String againPasswd){
        DefaultStringParser parser=new DefaultStringParser(callback);
        HashMap<String,String> params=new HashMap<>();
        params.put("mobile",phone);
        params.put("validate",vCode);
        params.put("newpassword",againPasswd);
        parser.executePost(URLConfig.getM_PASSWORD_RETRIEVE(),params);
    }
    
    private void passwdInputToggle() {

        if (change_passwd_toggle.isChecked()) {
            change_passwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            change_passwd_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void passwdInputAgainToggle() {
        if (change_passwd_again_toggle.isChecked()) {
            change_passwd_again_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            change_passwd_again_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private boolean isCommitInvalid(String phone, String vcode, String passwd, String passwdAgain) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        } else if (vcode.length()<6) {
            UUToast.showUUToast(this, "验证码错误");
            return false;
        }else if (TextUtils.isEmpty(passwd) || passwd.length() < 6) {
            UUToast.showUUToast(this, "密码至少为6位");
            return false;
        } else if (TextUtils.isEmpty(passwdAgain) || !passwd.equals(passwdAgain)) {
            UUToast.showUUToast(this, "两次输入密码不一致");
            return false;
        } else {
            return true;
        }

    }

}
