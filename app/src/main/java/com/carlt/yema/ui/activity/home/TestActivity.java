package com.carlt.yema.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.carlt.yema.MainActivity;
import com.carlt.yema.R;
import com.carlt.yema.control.EControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.protocolparser.BaseParser;

/**
 * Created by Marlon on 2018/3/19.
 */

public class TestActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_centre_tips);
        TextView one = findViewById(R.id.activity_career_secretary_tips_car);
        TextView two = findViewById(R.id.activity_career_secretary_tips_havemainten);
        one.setOnClickListener(listener);
        two.setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.activity_career_secretary_tips_car:
                    EControl.GetLoginResult(callback);
                    break;
                case R.id.activity_career_secretary_tips_havemainten:
                    break;
            }
        }
    };

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Log.e("KAE","登录成功");
            Intent intent = new Intent(TestActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {

        }
    };
}
