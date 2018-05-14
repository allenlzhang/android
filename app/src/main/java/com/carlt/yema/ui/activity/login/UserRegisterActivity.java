package com.carlt.yema.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.data.login.UserRegisterParams;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.activity.setting.TermsDeclareActivity;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView back;//返回按钮

    private EditText register_phone_input;//手机号码
    private EditText register_vcode_input;//验证码
    private EditText register_passwd_et;//输入修改密码
    private EditText register_passwd_again_et;//再次输入

    private ImageView register_passwd_toggle;//显示&隐藏密码按钮
    private ImageView register_passwd_again_toggle;//显示&隐藏密码按钮

    private TextView titleText;//页面标题
    private TextView register_txt_declaration;//页面标题
    private TextView register_verification_send;//发送验证码按钮
    private TextView register_commit;//确认修改

    private CheckBox register_check;

    private Dialog mDialog;

    private UserRegisterParams registerParams = new UserRegisterParams();

    private final static String URL_PROVISION = "http://m.cheler.com/yema.html";// 服务条款URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initComponent();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("注册");

        register_phone_input = findViewById(R.id.register_phone_input);
        register_vcode_input = findViewById(R.id.register_vcode_input);
        register_passwd_et = findViewById(R.id.register_passwd_et);
        register_passwd_again_et = findViewById(R.id.register_passwd_again_et);

        register_passwd_toggle = findViewById(R.id.register_passwd_toggle);
        register_passwd_toggle.setOnClickListener(this);
        register_passwd_again_toggle = findViewById(R.id.register_passwd_again_toggle);
        register_passwd_again_toggle.setOnClickListener(this);

        register_verification_send = findViewById(R.id.register_verification_send);
        register_verification_send.setOnClickListener(this);

        register_check = findViewById(R.id.register_check);
        register_check.setOnCheckedChangeListener(this);

        register_txt_declaration = findViewById(R.id.register_txt_declaration);
        register_txt_declaration.setOnClickListener(this);

        register_commit = findViewById(R.id.register_commit);
        register_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_passwd_toggle:
                ActivityControl.passwdToggle(this,register_passwd_et,register_passwd_toggle,view.getTag().toString());
                if (!TextUtils.isEmpty(register_passwd_et.getText().toString())) {
                    register_passwd_et.setSelection(register_passwd_et.getText().toString().length());
                }
                break;
            case R.id.register_passwd_again_toggle:
                ActivityControl.passwdToggle(this,register_passwd_again_et,register_passwd_again_toggle,view.getTag().toString());
                if (!TextUtils.isEmpty(register_passwd_again_et.getText().toString())) {
                    register_passwd_again_et.setSelection(register_passwd_again_et.getText().toString().length());
                }
                break;
            case R.id.register_verification_send:
                String cellPhone = register_phone_input.getText().toString();
                if (TextUtils.isEmpty(cellPhone) || !StringUtils.checkCellphone(cellPhone)) {
                    UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
                } else {
                    CPControl.GetMessageValidateResult("1", cellPhone, validateCodeListener);
                    count = 60;
                    register_verification_send.setText(count + "秒后重发");
                    register_verification_send.setClickable(false);
                    register_verification_send.setBackgroundResource(R.mipmap.btn_code_gray);

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
            case R.id.register_txt_declaration:
                Intent termsDeclare = new Intent(this, TermsDeclareActivity.class);
                termsDeclare.putExtra(TermsDeclareActivity.URL_INFO, URL_PROVISION);
                startActivity(termsDeclare);
                break;
            case R.id.register_commit:
                String commitPhone = register_phone_input.getText().toString();
                String commitVCode = register_vcode_input.getText().toString();
                String passwd = register_passwd_et.getText().toString();
                String passwdAgain = register_passwd_again_et.getText().toString();

                if (!isCommitInvalid(commitPhone, commitVCode, passwd, passwdAgain)) return;

                registerParams.setMobile(commitPhone);
                registerParams.setValidate(commitVCode);
                registerParams.setPassword(passwdAgain);

                mDialog = PopBoxCreat
                        .createDialogWithProgress(UserRegisterActivity.this, "正在加载");
                mDialog.show();
                CPControl.GetRegisteResult(registerParams, listener_register);
                break;
            case R.id.back:
                finish();
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
                    UUToast.showUUToast(UserRegisterActivity.this, msg.obj.toString());
                    break;
                case 1:
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    register_verification_send.setClickable(true);
                    register_verification_send.setText(R.string.usercenter_push_validate1);
                    register_verification_send.setBackgroundResource(R.drawable.verification_sending_bg);

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    int flag = mBaseResponseInfo.getFlag();
                    if (flag == BaseResponseInfo.PHONE_REGISTERED) {
                        UUToast.showUUToast(UserRegisterActivity.this, "该手机号已存在:"
                                + mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(UserRegisterActivity.this, "验证码获取失败:"
                                + mBaseResponseInfo.getInfo());
                    }
                    break;
                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    mUseInfo.setAccount(registerParams.getMobile());
                    mUseInfo.setPassword(registerParams.getPassword());
                    UseInfoLocal.setUseInfo(mUseInfo);

                    UUToast.showUUToast(UserRegisterActivity.this, "注册成功！");
                    LoginInfo.setPin(registerParams.getMobile(), "");
                    LoginControl.logic(UserRegisterActivity.this);
                    finish();
                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    UUToast.showUUToast(UserRegisterActivity.this, "不好意思，注册失败，请稍候再试:"
                            + mBaseResponseInfo.getInfo());
                    break;
                case 10:
                    count--;
                    if (count > 0) {
                        register_verification_send.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        register_verification_send.setClickable(true);
                        register_verification_send.setText(R.string.usercenter_push_validate1);
                        register_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private BaseParser.ResultCallback validateCodeListener = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = UserRegisterActivity.this.getResources().getString(R.string.vcode_send_success);
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

    private BaseParser.ResultCallback listener_register = new BaseParser.ResultCallback() {
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        register_commit.setClickable(isChecked);
        if (isChecked) {
            register_commit.setBackgroundResource(R.drawable.bottom_btn_bg);
        } else {
            register_commit.setBackgroundResource(R.drawable.bottom_btn_gray);
        }

    }

    private boolean isCommitInvalid(String phone, String vcode, String passwd, String passwdAgain) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(passwd) || passwd.length() < 6) {
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
