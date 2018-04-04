package com.carlt.yema.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class VcodeResetPasswdActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private EditText phoneNumber;//手机号码输入框
    private EditText verficationCode;//验证码输入框
    private EditText passwd;//密码输入框
    private EditText passwd2St;//再次输入密码输入框

    private ImageView passwdToggle;//是否显示密码
    private ImageView passwd2StToggle;//再次输入是否显示密码

    private TextView vCodeSend;
    private TextView commit;

    private String mobile;
    private String vCode;
    private String resetPasswd;
    private String confirmPasswd;
    private Dialog mDialog;

    /*
    * 倒计时
	*/
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertified_code_reset_passwd);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        vCodeSend=$ViewByID(R.id.verification_passwd_code_send);
        vCodeSend.setOnClickListener(this);
        commit=$ViewByID(R.id.verification_reset_passwd_commit);
        commit.setOnClickListener(this);
        passwdToggle=$ViewByID(R.id.verification_new_passwd_input_toggle);
        passwdToggle.setOnClickListener(this);
        passwd2StToggle=$ViewByID(R.id.verification_new_passwd_input_again_toggle);
        passwd2StToggle.setOnClickListener(this);

        phoneNumber=$ViewByID(R.id.verification_passwd_phone);
        verficationCode=$ViewByID(R.id.verification_passwd_vcode_input);
        passwd=$ViewByID(R.id.verification_new_passwd_input);
        passwd2St=$ViewByID(R.id.verification_new_passwd_again_input);

        title=$ViewByID(R.id.title);
        title.setText("修改登录密码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verification_passwd_code_send:
                mobile = phoneNumber.getText().toString();
                if (mobile != null && StringUtils.checkCellphone(mobile)) {
                    String phoneOld = LoginInfo.getMobile();
                    if (!mobile.equals(phoneOld)) {
                        UUToast.showUUToast(VcodeResetPasswdActivity.this, "您输入的手机号不是您当前的手机号码，请重新输入...");
                        return;
                    }
                    getVCodeRequest(mobile);
                    count = 60;
                    vCodeSend.setText(count + "秒后重发");
                    vCodeSend.setClickable(false);
                    vCodeSend.setBackgroundResource(R.drawable.verification_send_bg);

                    task = new TimerTask() {

                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 10;
                            mHandler.sendMessage(msg);

                        }
                    };
                    timer.schedule(task, 1000, 1000);

                } else {
                    UUToast.showUUToast(VcodeResetPasswdActivity.this, "请输入正确的手机号");
                }
                break;
            case R.id.verification_reset_passwd_commit:
                mobile=phoneNumber.getText().toString();
                vCode=verficationCode.getText().toString();
                resetPasswd=passwd.getText().toString();
                confirmPasswd=passwd2St.getText().toString();
                if (isCommitInvalid(mobile,vCode,resetPasswd,confirmPasswd)) {
                    editPasswdCommitRequest();
                }
                break;
            case R.id.verification_new_passwd_input_toggle:
                passwdInputToggle(view.getTag().toString());
                break;
            case R.id.verification_new_passwd_input_again_toggle:
               passwdInputAgainToggle(view.getTag().toString());
                break;

        }
    }

    /**
     * 获取验证码接口请求
     */
    private void getVCodeRequest(String mobile) {
        DefaultStringParser parser = new DefaultStringParser(vCodeCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", "2");
        params.put("voiceVerify", "0");
        parser.executePost(URLConfig.getM_AUTH_SET_VALIDATE(), params);
    }

    private BaseParser.ResultCallback vCodeCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(VcodeResetPasswdActivity.this, "验证码已发送成功！");
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 获取验证码失败
            // 停止计时
            if (timer != null) {
                if (task != null) {
                    task.cancel();
                }
            }
            vCodeSend.setClickable(true);
            vCodeSend.setText("重发验证码");
            vCodeSend.setBackgroundResource(R.drawable.verification_send_bg);
            int flag = bInfo.getFlag();
            UUToast.showUUToast(VcodeResetPasswdActivity.this, "验证码获取失败:" + bInfo.getInfo());
        }
    };

    private void editPasswdCommitRequest(){
        DefaultStringParser parser=new DefaultStringParser(commitCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("validate",vCode);
        params.put("newpassword", confirmPasswd);
        parser.executePost(URLConfig.getM_PASSWORD_RETRIEVE(),params);
    }

    private BaseParser.ResultCallback commitCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(VcodeResetPasswdActivity.this,"密码修改成功");
            LoginControl.logic(VcodeResetPasswdActivity.this);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(VcodeResetPasswdActivity.this,bInfo.getInfo());
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    count--;
                    if (count > 0) {
                        vCodeSend.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        vCodeSend.setClickable(true);
                        vCodeSend.setText("重发验证码");
                        vCodeSend.setBackgroundResource(R.drawable.verification_send_bg);
                    }
                    break;
            }
        }

    };

    /**
     * 判断原始密码、新密码、再次输入新密码是否合法
     * */
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

    /**
     * 新密码是否显示按钮
     *
     * */
    private void passwdInputToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwdToggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                passwdToggle.setTag("off");
            } else {
                passwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwdToggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                passwdToggle.setTag("on");
            }
        }
    }
    /**
     * 确认密码是否显示按钮
     *
     * */
    private void passwdInputAgainToggle(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                passwd2St.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwd2StToggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_off, null));
                passwd2StToggle.setTag("off");
            } else {
                passwd2St.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwd2StToggle.setImageDrawable(this.getResources().getDrawable(R.mipmap.passwd_on, null));
                passwd2StToggle.setTag("on");
            }
        }
    }
}
