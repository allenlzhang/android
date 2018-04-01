package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CarManagerActivity extends LoadingActivity implements View.OnClickListener {

    private ImageView back;
    private TextView title;
    ;

    private View edit_car_type;//车型
    private View edit_purchase_time;//购车时间
    private View edit_maintenance_mileage;//上次保养里程
    private View edit_maintenance_time;//上次保养时间
    private View edit_insured_time;//上次投保时间
    private View edit_nspection_time;//上次年检时间

    private TextView car_type_txt;//显示
    private TextView purchase_time_txt;//显示
    private TextView maintenance_mileage_txt;//显示
    private TextView maintenance_time_txt;//显示
    private TextView insured_time_txt;//显示
    private TextView nspection_time_txt;//显示
    private TimePickerView pvCustomTime;

    private String buydate = "";
    private String mainten_miles = "";
    private String mainten_date = "";
    private String insurance_date = "";
    private String register_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        initCustomTimePicker();
        initComponent();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.car_manager_txt));
        edit_car_type = findViewById(R.id.edit_car_type);
        edit_car_type.setOnClickListener(this);
        edit_purchase_time = findViewById(R.id.edit_purchase_time);
        edit_purchase_time.setOnClickListener(this);
        edit_maintenance_mileage = findViewById(R.id.edit_maintenance_mileage);
        edit_maintenance_mileage.setOnClickListener(this);
        edit_maintenance_time = findViewById(R.id.edit_maintenance_time);
        edit_maintenance_time.setOnClickListener(this);
        edit_insured_time = findViewById(R.id.edit_insured_time);
        edit_insured_time.setOnClickListener(this);
        edit_nspection_time = findViewById(R.id.edit_nspection_time);
        edit_nspection_time.setOnClickListener(this);

        car_type_txt = findViewById(R.id.car_type_txt);
        purchase_time_txt = findViewById(R.id.purchase_time_txt);
        maintenance_mileage_txt = findViewById(R.id.maintenance_mileage_txt);
        maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), 0));
        maintenance_time_txt = findViewById(R.id.maintenance_time_txt);
        insured_time_txt = findViewById(R.id.insured_time_txt);
        nspection_time_txt = findViewById(R.id.nspection_time_txt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_car_type:
                Intent switchIntent = new Intent(this, CarModeListActivity.class);
                switchIntent.putExtra("switch", true);//标记从车辆管理界面跳转
                startActivity(switchIntent);
                finish();
                break;
            case R.id.edit_maintenance_mileage:
                Intent editMaintenance = new Intent(this, MaintenanceMileageEditActivity.class);
                startActivityForResult(editMaintenance, 0);
                break;
            case R.id.edit_purchase_time:
                pvCustomTime.show(purchase_time_txt);
                break;
            case R.id.edit_maintenance_time:
                pvCustomTime.show(maintenance_time_txt);
                break;
            case R.id.edit_insured_time:
                pvCustomTime.show(insured_time_txt);
                break;
            case R.id.edit_nspection_time:
                pvCustomTime.show(nspection_time_txt);
                break;
        }
    }

    private void modifyCarInfoRequest() {
        DefaultStringParser parser = new DefaultStringParser(modifyCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("buydate", buydate);
        params.put("mainten_miles", mainten_miles);
        params.put("mainten_date", mainten_date);
        params.put("insurance_date", insurance_date);
        params.put("register_date", register_date);
        parser.executePost(URLConfig.getM_CAR_MODIFY(), params);
    }

    private BaseParser.ResultCallback modifyCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadNodataUI();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {

        }
    };


    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                switch (v.getId()) {
                    case R.id.edit_purchase_time:
                        buydate=getTime(date);
                        break;
                    case R.id.edit_maintenance_time:
                        mainten_date=getTime(date);
                        break;
                    case R.id.edit_insured_time:
                        insurance_date=getTime(date);
                        break;
                    case R.id.edit_nspection_time:
                        register_date=getTime(date);
                        break;
                }
                modifyCarInfoRequest();
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_edit_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView _OK = (TextView) v.findViewById(R.id.sex_change_OK);
                        final TextView _cancel = (TextView) v.findViewById(R.id.sex_change_cancel);
                        _OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });

                        _cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });

                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String mileage = data.getStringExtra("mileage");
            if (!TextUtils.isEmpty(mileage)) {
                maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), Integer.parseInt(mileage)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
