package com.hz17car.yema.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseActivity;

import static com.hz17car.yema.R.layout.sex_edit_dialog;

/**
 * Created by marller on 2018\3\17 0017.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private View edit_person_avatar;
    private View edit_person_nickname;
    private View edit_person_sex;

    private TextView person_nickname_txt;
    private TextView person_sex_txt;

    private Dialog nEditSexDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title_personinfo_edit_txt));

        edit_person_avatar=findViewById(R.id.edit_person_avatar);
        edit_person_avatar.setOnClickListener(this);
        edit_person_nickname=findViewById(R.id.edit_person_nickname);
        edit_person_nickname.setOnClickListener(this);
        edit_person_sex=findViewById(R.id.edit_person_sex);
        edit_person_sex.setOnClickListener(this);

        edit_person_nickname=findViewById(R.id.edit_person_nickname);
        person_sex_txt=findViewById(R.id.person_sex_txt);
        nEditSexDialog=createDialog();
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
                Intent nicknameEdit=new Intent(this,NicknameEditActivity.class);
                startActivity(nicknameEdit);
                break;
            case R.id.edit_person_sex:
                showDialog();
                break;
            case R.id.sex_change_OK:
                break;
            case R.id.sex_change_cancel:
                break;
        }
    }

    private Dialog createDialog() {
        Dialog nEditSexDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                sex_edit_dialog, null);
        //初始化视图
        root.findViewById(R.id.sex_change_OK).setOnClickListener(this);
        root.findViewById(R.id.sex_change_cancel).setOnClickListener(this);
        nEditSexDialog.setContentView(root);
        Window dialogWindow = nEditSexDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.sex_edit_dialog_bg);
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 1f; // 透明度
        dialogWindow.setAttributes(lp);
        return nEditSexDialog;
    }
    private void showDialog(){
        if (null!=nEditSexDialog)
            nEditSexDialog.show();
    }
    private void dismissDialog(){
        if (null!=nEditSexDialog&&nEditSexDialog.isShowing())
            nEditSexDialog.dismiss();
    }
}
