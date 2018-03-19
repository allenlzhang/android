package com.hz17car.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseActivity;
import com.hz17car.yema.utils.DensityUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marller on 2018\3\17 0017.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private TextView title;

    private View edit_person_avatar;
    private View edit_person_nickname;
    private View edit_person_sex;

    private TextView person_nickname_txt;
    private TextView person_sex_txt;

    private OptionsPickerView mSexOptions;//性别选择
    private static String[] sexItems = {"男", "女", "保密"};
    private List<String> sexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initComponent();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title_personinfo_edit_txt));

        edit_person_avatar = findViewById(R.id.edit_person_avatar);
        edit_person_avatar.setOnClickListener(this);
        edit_person_nickname = findViewById(R.id.edit_person_nickname);
        edit_person_nickname.setOnClickListener(this);
        edit_person_sex = findViewById(R.id.edit_person_sex);
        edit_person_sex.setOnClickListener(this);

        edit_person_nickname = findViewById(R.id.edit_person_nickname);
        person_sex_txt = findViewById(R.id.person_sex_txt);

        initSexSelector();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_person_avatar:
                break;
            case R.id.edit_person_nickname:
                Intent nicknameEdit = new Intent(this, NicknameEditActivity.class);
                startActivity(nicknameEdit);
                break;
            case R.id.edit_person_sex:
                mSexOptions.show();
                break;
            case R.id.sex_change_OK:
                break;
            case R.id.sex_change_cancel:
                break;
        }
    }

    private void initSexSelector() {
        sexList = Arrays.asList(sexItems);
        mSexOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = sexList.get(options1);
                person_sex_txt.setText(tx);
            }
        })
                .setLayoutRes(R.layout.sex_edit_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView sex_change_OK = (TextView) v.findViewById(R.id.sex_change_OK);
                        final TextView sex_change_cancel = (TextView) v.findViewById(R.id.sex_change_cancel);
                        sex_change_OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSexOptions.returnData();
                                mSexOptions.dismiss();
                            }
                        });

                        sex_change_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSexOptions.dismiss();
                            }
                        });

                    }
                })
                .setSelectOptions(0)
                .setContentTextSize(DensityUtil.sp2px(PersonInfoActivity.this,14))
                .setDividerType(WheelView.DividerType.FILL)
                .setLineSpacingMultiplier(2.0f)
                .isDialog(false)
                .build();
        mSexOptions.setPicker(sexList);//添加数据

    }


}
