package com.carlt.yema.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.data.set.ModifyCarInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.SelectCarTypeView;
import com.carlt.yema.ui.view.UUToast;

public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;//返回按钮
    private LinearLayout title_bar;

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_vin_code;//vin码输入框

    private SelectCarTypeView mCarTypeView;

    private Dialog mDialog;

    private String brandid;// 车型id

    private String optionid;// 车系id

    private String carid;// 车款id

    private String carName;// 车款Name

    private String carLogo;// 车标icon的url

    private String carYear;// 车款年限

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        intent=getIntent();
        initComponent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("绑定设备");
        btn_select_car = findViewById(R.id.btn_select_car);
        btn_select_car.setOnClickListener(this);
        bind_commit = findViewById(R.id.bind_commit);
        bind_commit.setOnClickListener(this);
        car_vin_code = findViewById(R.id.car_vin_code);
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("vin"))) {
            car_vin_code.setText(intent.getStringExtra("vin"));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_select_car:
                // 选择车系
                if (mCarTypeView == null) {
                    mCarTypeView = new SelectCarTypeView(
                            DeviceBindActivity.this, mOnCarTypeItemClick);
                }
                CarModeInfo carModeInfo2 = new CarModeInfo();
                carModeInfo2.setTitle(SelectCarTypeView.TITLE);
                carModeInfo2.setId(SelectCarTypeView.OPTIONID);
                mCarTypeView.pullDataSecond(carModeInfo2,
                        SelectCarTypeView.TYPE_SERIES);
                mCarTypeView.showMenu();
                break;
            case R.id.bind_commit:
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 修改车型成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    carName = LoginInfo.getCarname();
                    btn_select_car.setText(carName);
                    UUToast.showUUToast(DeviceBindActivity.this, "修改车型成功！");
                    break;

                case 1:
                    // 修改车型失败
                    BaseResponseInfo mBaseResponseInfo1 = (BaseResponseInfo) msg.obj;

                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if (mBaseResponseInfo1 != null) {
                        if (mBaseResponseInfo1.getFlag() == 1020) {
                            PopBoxCreat.showUUUpdateDialog(DeviceBindActivity.this, null);
                            return;
                        }

                        String info = mBaseResponseInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(DeviceBindActivity.this, info);
                        } else {
                            UUToast.showUUToast(DeviceBindActivity.this, "修改车型失败...");
                        }
                    } else {
                        UUToast.showUUToast(DeviceBindActivity.this, "修改车型失败...");
                    }
                    break;


            }
        };
    };

    /**
     * 选择车型、车系、车款的回调
     */
    private SelectCarTypeView.OnCarTypeItemClick mOnCarTypeItemClick = new SelectCarTypeView.OnCarTypeItemClick() {

        @Override
        public void onClick(CarModeInfo carModeInfo, int type) {
            switch (type) {
                case SelectCarTypeView.TYPE_MODEL:
                    // 车型
                    brandid = carModeInfo.getId();
                    carLogo = carModeInfo.getCarlogo();
                    mCarTypeView.pullDataSecond(carModeInfo,
                            SelectCarTypeView.TYPE_SERIES);
                    break;
                case SelectCarTypeView.TYPE_SERIES:
                    // 车系
                    optionid = carModeInfo.getId();
                    break;
                case SelectCarTypeView.TYPE_CAR:
                    // 车款
                    if (mCarTypeView != null) {
                        // 让第三级选车型popwindow消失
                        carid = carModeInfo.getId();
                        carName = carModeInfo.getTitle();
                        carYear = carModeInfo.getYear();

                        PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

                            @Override
                            public void onRightClick() {
                                // 取消

                            }

                            @Override
                            public void onLeftClick() {
                                // 确定
                                ModifyCarInfo modifyCarInfo = new ModifyCarInfo();
                                brandid = SelectCarTypeView.BRANDID;
                                if (brandid != null && brandid.length() > 0
                                        && optionid != null
                                        && optionid.length() > 0 && carid != null
                                        && carid.length() > 0) {
                                    modifyCarInfo.setBrandid(brandid);
                                    modifyCarInfo.setOptionid(optionid);
                                    modifyCarInfo.setCarid(carid);

                                    modifyCarInfo.setCarname(carName);
                                    modifyCarInfo.setCarlogo(carLogo);
                                    modifyCarInfo.setYear(carYear);
                                } else {
                                    UUToast.showUUToast(DeviceBindActivity.this,
                                            "您还没有选择爱车车型哦！");
                                    return;
                                }
                                mCarTypeView.dissmiss();
                                mDialog = PopBoxCreat.createDialogWithProgress(
                                        DeviceBindActivity.this, "数据提交中...");
                                mDialog.show();
                                CPControl.GetUpdateCarTypeResult(modifyCarInfo,
                                        listener_car);
                            }
                        };
                        PopBoxCreat.createDialogWithTitle(DeviceBindActivity.this,
                                "提示", "您选择的车型是\n" + carName,
                                "温馨提示：请正确选择您的爱车车型，否则可能会有部分远程控制功能无法正常支持；", "确定",
                                "取消", click);
                    }
                    break;
            }

        }
    };

    private BaseParser.ResultCallback listener_car = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };
}
