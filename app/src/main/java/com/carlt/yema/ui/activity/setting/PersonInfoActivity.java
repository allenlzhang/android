package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.graphics.Color;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marller on 2018\3\17 0017.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PersonInfoActivity";

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
    String sexFlag = "";

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
        if (!TextUtils.isEmpty(LoginInfo.getRealname())) {
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
        if (!TextUtils.isEmpty(LoginInfo.getGender())) {
            if (LoginInfo.getGender().equals("1")) {
                person_sex_txt.setText("男");
            } else if (LoginInfo.getGender().equals("2")) {
                person_sex_txt.setText("女");
            } else {
                person_sex_txt.setText("保密");
            }

        }
        if (!TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            Glide.with(this).load(LoginInfo.getAvatar_img()).into(usr_avatar);
        }
        if (!TextUtils.isEmpty(LoginInfo.getRealname())) {
            person_nickname_txt.setText(LoginInfo.getRealname());
        }
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
                nicknameEdit.putExtra("nickname",person_nickname_txt.getText().toString());
                startActivityForResult(nicknameEdit, 0);
                break;
            case R.id.edit_person_sex:
                mSexOptions.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == 0) {
            if (!TextUtils.isEmpty(data.getStringExtra("nickName"))) {
                person_nickname_txt.setText(data.getStringExtra("nickName"));
            }
        } else {
            if (!TextUtils.isEmpty(data.getStringExtra("imageId"))) {
                HashMap<String, String> params = new HashMap<>();
                params.put("avatar", data.getStringExtra("imageId"));
                chanageInfoRequest(params,imgCallback);
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

                if (tx.equals("男")) sexFlag = "1";
                else if (tx.equals("女")) sexFlag = "2";
                else sexFlag = "3";
                HashMap<String, String> params = new HashMap<>();
                params.put("gender", sexFlag);
                chanageInfoRequest(params,sexCallback);
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
                .setBgColor(Color.parseColor("#95161922"))
                .setSelectOptions(0)
                .setContentTextSize(20)
                .setDividerType(WheelView.DividerType.FILL)
                .setLineSpacingMultiplier(0.5f)
                .isDialog(false)
                .build();
        mSexOptions.setPicker(sexList);//添加数据

    }

    private void chanageInfoRequest(HashMap<String, String> params,BaseParser.ResultCallback callback) {
        DefaultStringParser parser = new DefaultStringParser(callback);
        parser.executePost(URLConfig.getM_USER_EDIT_INFO(), params);
    }

    private BaseParser.ResultCallback imgCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PersonInfoActivity.this, "资料修改成功");
            parseAvatarUrl(bInfo);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (bInfo != null && !TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(PersonInfoActivity.this, bInfo.getInfo());
            }
        }
    };
    private BaseParser.ResultCallback sexCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PersonInfoActivity.this, "资料修改成功");
            LoginInfo.setGender(sexFlag);
            person_sex_txt.setText(gender);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (bInfo != null && !TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(PersonInfoActivity.this, bInfo.getInfo());
            }
        }
    };
    private void parseAvatarUrl(BaseResponseInfo bInfo) {
        String data = bInfo.getValue().toString();
        try {
            JSONObject json=new JSONObject(data);
            if (json != null) {
//                String avatar_url = json.optString("avatar_img");
//                LoginInfo.setAvatar_img(avatar_url);
                String avatar_url = LoginInfo.getAvatar_img();
                if (!TextUtils.isEmpty(avatar_url)) {
                    LoginInfo.setAvatar_img(avatar_url);
                    Glide.with(PersonInfoActivity.this).load(LoginInfo.getAvatar_img()).into(usr_avatar);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
