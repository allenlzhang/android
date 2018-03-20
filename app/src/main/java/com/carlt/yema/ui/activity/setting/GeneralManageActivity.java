package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class GeneralManageActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{

    private ImageView back;
    private TextView tilte;
    private CheckBox voiceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_manage);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);
        tilte=$ViewByID(R.id.title);
        tilte.setText(getResources().getString(R.string.general_manager_txt));
        voiceToggle=$ViewByID(R.id.btn_remote_voice_affect);
        voiceToggle.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
