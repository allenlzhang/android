package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;

import java.util.HashMap;

public class CarManagerActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;;

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

    private String buydate="";
    private String mainten_miles="";
    private String mainten_date="";
    private String insurance_date="";
    private String register_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        title.setText(getResources().getString(R.string.car_manager_txt));
        edit_car_type=findViewById(R.id.edit_car_type);
        edit_car_type.setOnClickListener(this);
        edit_purchase_time=findViewById(R.id.edit_purchase_time);
        edit_purchase_time.setOnClickListener(this);
        edit_maintenance_mileage=findViewById(R.id.edit_maintenance_mileage);
        edit_maintenance_mileage.setOnClickListener(this);
        edit_maintenance_time=findViewById(R.id.edit_maintenance_time);
        edit_maintenance_time.setOnClickListener(this);
        edit_insured_time=findViewById(R.id.edit_insured_time);
        edit_insured_time.setOnClickListener(this);
        edit_nspection_time=findViewById(R.id.edit_nspection_time);
        edit_nspection_time.setOnClickListener(this);

        car_type_txt=findViewById(R.id.car_type_txt);
        purchase_time_txt=findViewById(R.id.purchase_time_txt);
        maintenance_mileage_txt=findViewById(R.id.maintenance_mileage_txt);
        maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), 0));
        maintenance_time_txt=findViewById(R.id.maintenance_time_txt);
        insured_time_txt=findViewById(R.id.insured_time_txt);
        nspection_time_txt=findViewById(R.id.nspection_time_txt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_car_type:
                Intent switchIntent=new Intent(this,CarModeListActivity.class);
                switchIntent.putExtra("switch",true);//标记从车辆管理界面跳转
                startActivity(switchIntent);
                finish();
                break;
            case R.id.edit_purchase_time:
                break;
            case R.id.edit_maintenance_mileage:
                Intent editMaintenance=new Intent(this,MaintenanceMileageEditActivity.class);
                startActivityForResult(editMaintenance,0);
                break;
            case R.id.edit_maintenance_time:
                break;
            case R.id.edit_insured_time:
                break;
            case R.id.edit_nspection_time:
                break;
        }
    }

    private void modifyCarInfoRequest(){
        DefaultStringParser parser=new DefaultStringParser(modifyCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("buydate", buydate);
        params.put("mainten_miles", mainten_miles);
        params.put("mainten_date", mainten_date);
        params.put("insurance_date", insurance_date);
        params.put("register_date",register_date);
        parser.executePost(URLConfig.getM_CAR_MODIFY(),params);
    }

    private BaseParser.ResultCallback modifyCallback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {

        }

        @Override
        public void onError(BaseResponseInfo bInfo) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
           String mileage=data.getStringExtra("mileage");
            if (!TextUtils.isEmpty(mileage) ){
                maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), Integer.parseInt(mileage)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
