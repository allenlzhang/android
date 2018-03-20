package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class MsgManageActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private ImageView back;
    private TextView title;

    private CheckBox insurance_expiration_reminder;
    private CheckBox annual_inspection_reminder;
    private CheckBox driving_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_manage);
        initComponent();
    }

    private void initComponent() {

        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);

        title=$ViewByID(R.id.title);
        title.setText(getResources().getString(R.string.msg_manager_txt));

        insurance_expiration_reminder=$ViewByID(R.id.insurance_expiration_reminder);
        annual_inspection_reminder=$ViewByID(R.id.annual_inspection_reminder);
        driving_report=$ViewByID(R.id.driving_report);

    }


    @Override
    public void onClick(View view) {
        finish();

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
