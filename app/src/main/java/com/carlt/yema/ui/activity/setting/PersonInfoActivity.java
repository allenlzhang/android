package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.DensityUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marller on 2018\3\17 0017.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG="PersonInfoActivity";

    private ImageView back;
    private TextView title;

    private View edit_person_avatar;
    private View edit_person_nickname;
    private View edit_person_sex;

    private TextView person_nickname_txt;
    private TextView person_sex_txt;
    private ImageView usr_avatar;

    private OptionsPickerView mSexOptions;//性别选择
    private static String[] sexItems = {"男", "女", "保密"};
    private List<String> sexList;
    private String gender;

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
        usr_avatar = findViewById(R.id.usr_avatar);
        person_sex_txt = findViewById(R.id.person_sex_txt);
        person_nickname_txt = findViewById(R.id.person_nickname_txt);
        if (!TextUtils.isEmpty(LoginInfo.getRealname())){
            person_nickname_txt.setText(LoginInfo.getRealname());
        }
        initSexSelector();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (!TextUtils.isEmpty(LoginInfo.getGender())){
            if (LoginInfo.getGender().equals("1")) {
                person_sex_txt.setText("男");
            } else if (LoginInfo.getGender().equals("2")) {
                person_sex_txt.setText("女");
            } else {
                person_sex_txt.setText("保密");
            }

        }
        Glide.with(this).load(LoginInfo.getAvatar_img()).into(usr_avatar);
        person_nickname_txt.setText(LoginInfo.getRealname());
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
                Intent avatarIntent = new Intent(this, PersonAvatarActivity.class);
                //avatarIntent.putExtra("avatar",avatarPath);
                startActivityForResult(avatarIntent, 1);
                break;
            case R.id.edit_person_nickname:
                Intent nicknameEdit = new Intent(this, NicknameEditActivity.class);
                startActivityForResult(nicknameEdit, 0);
                break;
            case R.id.edit_person_sex:
                mSexOptions.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null)return;
        if (requestCode == 0) {
            if ( !TextUtils.isEmpty(data.getStringExtra("nickName"))) {
                person_nickname_txt.setText(data.getStringExtra("nickName"));
            }
        } else {
            if (!TextUtils.isEmpty(data.getStringExtra("imageId"))&&!data.getStringExtra("imageId").equals(-1)) {
                HashMap<String,String> params=new HashMap<>();
                params.put("avatar",data.getStringExtra("imageId"));
                chanageInfoRequest(params);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSexSelector() {
        sexList = Arrays.asList(sexItems);
        mSexOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = sexList.get(options1);
                HashMap<String,String> params=new HashMap<>();
                params.put("gender",tx);
                chanageInfoRequest(params);
                gender = tx;
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
                .setContentTextSize(DensityUtil.sp2px(PersonInfoActivity.this, 14))
                .setDividerType(WheelView.DividerType.FILL)
                .setLineSpacingMultiplier(2.0f)
                .isDialog(false)
                .build();
        mSexOptions.setPicker(sexList);//添加数据

    }

    private void chanageInfoRequest(HashMap<String, String> params) {
        DefaultStringParser parser = new DefaultStringParser(callback);
        parser.executePost(URLConfig.getM_USER_EDIT_INFO(), params);
    }

    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PersonInfoActivity.this, "资料修改成功");
            person_sex_txt.setText(gender);
            LoginInfo.setGender(gender);
            if (!TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
                Glide.with(PersonInfoActivity.this).load(LoginInfo.getAvatar_img()).into(usr_avatar);
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PersonInfoActivity.this, "资料修改失败");
        }
    };

}
