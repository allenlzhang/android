package com.carlt.yema.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.MainActivity;
import com.carlt.yema.R;
import com.carlt.yema.SplashActivity;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.ActivityControl;
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
import com.carlt.yema.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.yema.ui.view.UUTimerDialog;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.jar.Manifest;

public class ActivateBindActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;//返回按钮

    private TextView titleText;//页面标题
    private TextView activate_commit;//绑定激活按钮

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private UUTimerDialog mDialog;

    private String vinCode ="";

    private String carType = "";

    private int ActivateCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_bind);
        initComponent();
        initSubTitle();
    }

    private void initComponent(){
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("激活设备");
        Intent intent = getIntent();
        if (intent != null) {
            vinCode = intent.getStringExtra("vin");
            carType = intent.getStringExtra("carType");
        }
        activate_commit=findViewById(R.id.activate_commit);
        activate_commit.setOnClickListener(this);
    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

        mTextViewSecretary.setText("设备绑定成功！激活设备后就能使用野马管家的全部功能啦！");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                back();
                break;
            case R.id.activate_commit:
                DialogWithTitleClick click = new DialogWithTitleClick() {

                    @Override
                    public void onLeftClick() {
                        // 调用激活设备接口
                        mDialog = PopBoxCreat.createUUTimerDialog(
                                ActivateBindActivity.this, "激活中...");
                        mDialog.show();
                        listener_time = System.currentTimeMillis();
                        ActivateCount++;
                        mTextViewSecretary.setText("已收到激活请求，正在连接野马设备…");
                        activateDevice();
                    }

                    @Override
                    public void onRightClick() {
                        // TODO Auto-generated method stub

                    }
                };
                PopBoxCreat.createDialogWithTitle(ActivateBindActivity.this, "激活",
                        "您确定激活野马设备吗？", "", "确定", "取消", click);
                break;
        }
    }

    private void activateDevice(){
        DefaultStringParser parser=new DefaultStringParser(activateCallback);
        HashMap<String,String> params=new HashMap<>();
        parser.executePost(URLConfig.getM_DEVICE_ACTIVATE(),params);
    }

    private final static long ONEMIN = 1000 * 60;

    private long listener_time;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    activateDevice();
                    break;
                case 3:
                    ActivityControl.initXG();
//                    LoginControl.logic(ActivateBindActivity.this);
                    Intent intent = new Intent(ActivateBindActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 4:
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    Intent mIntent4 = new Intent(ActivateBindActivity.this,
                            UserLoginActivity.class);
                    finish();
                    ActivateBindActivity.this.overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
                    startActivity(mIntent4);
                    break;

            }

            super.handleMessage(msg);
        }
    };

    BaseParser.ResultCallback activateCallback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 下发激活指令成功
            UUToast.showUUToast(ActivateBindActivity.this,"野马设备已成功激活");
//            Intent intent=new Intent(ActivateBindActivity.this, MainActivity.class);
//            startActivity(intent);
            UseInfo mUseInfo = UseInfoLocal.getUseInfo();
            CPControl.GetLogin(mUseInfo.getAccount(), mUseInfo.getPassword(), listener_login);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            boolean t = (System.currentTimeMillis() - listener_time) > ONEMIN;
            int flagCode = bInfo.getFlag();
            if (flagCode == 2997 && !t) {
                mHandler.sendEmptyMessageDelayed(0,1000);
            } else {
                errorSwitch(bInfo);
            }

        }
    };

    private final static String e1 = "设备连接失败，请在手机信号良好的地方重新尝试";

    private final static String e2 = "设备连接失败，使用疑问请拨打电话4006-506-507咨询客服";

    private final static String e3 = "设备连接失败，请联系您的经销商检测设备是否正常";

    private final static String e4 = "您的车型排量或设备型号信息有误，请联系您的经销商进行处理";

    private final static String e5 = "请先将爱车熄火，再重新点击激活";
    private void errorSwitch(BaseResponseInfo mBaseResponseInfo) {
        int code = mBaseResponseInfo.getFlag();
        // 测试用
        // code=1021;
        if (code == 1020) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            PopBoxCreat.showUUUpdateDialog(ActivateBindActivity.this, null);
        }else if (code == BaseResponseInfo.ERRO){
            mTextViewSecretary.setText("激活失败，网络不稳定，请稍后重新再试");
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        }else if (code == 2997) {
            // 下发不成功的情况
            if (ActivateCount == 1) {
                mTextViewSecretary.setText(e1);
            } else if (ActivateCount == 2) {
                mTextViewSecretary.setText(e2);
            } else if (ActivateCount > 2) {
                mTextViewSecretary.setText(e3);
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } else {
            if (code == 3004) {
                mTextViewSecretary.setText(e4);
            }
//            else if (code == 3005) {
//                mTextViewSecretary.setText(e5);
//            }
            else {
//                mTextViewSecretary.setText(e3);
                mTextViewSecretary.setText(mBaseResponseInfo.getInfo());
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
        UUToast.showUUToast(ActivateBindActivity.this,"激活失败");
    }

    private void back(){
        Intent backIntent=new Intent(this,DeviceBindActivity.class);
        backIntent.putExtra("from","com.carlt.yema.ActivateBindActivity");
        backIntent.putExtra("vin",vinCode);
        backIntent.putExtra("carType",carType);
        startActivity(backIntent);
        finish();
        ActivityControl.clearAllActivity();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private BaseParser.ResultCallback listener_login = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {

            String dataValue = (String) o.getValue();
            JSONObject mJSON_data = null;
            try {
                mJSON_data = new JSONObject(dataValue);
                LoginControl.parseLoginInfo(mJSON_data);
                final Message msg = new Message();
                msg.what = 3;
                msg.obj = o;
                mHandler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = 4;
                msg.obj = o;
                mHandler.sendMessage(msg);
            }
        }
            @Override
            public void onError(BaseResponseInfo o) {
                Message msg = new Message();
                msg.what = 4;
                msg.obj = o;
                mHandler.sendMessage(msg);

            }

        };
}
