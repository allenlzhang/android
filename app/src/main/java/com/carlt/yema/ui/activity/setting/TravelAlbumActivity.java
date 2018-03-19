package com.carlt.yema.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

import static com.carlt.yema.R.layout.album_edit_dialog;

public class TravelAlbumActivity extends BaseActivity implements View.OnClickListener{

    private ImageView album_back;//返回

    private TextView album_opt;//相册操作按钮（编辑&取消）

    private Dialog mAlbumDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_album);
        initComponent();
    }

    private void initComponent(){
        album_back=findViewById(R.id.album_back);
        album_back.setOnClickListener(this);
        album_opt=findViewById(R.id.album_opt);
        album_opt.setOnClickListener(this);
        mAlbumDialog=createDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.album_back:
                finish();
                break;
            case R.id.album_opt:
                changeAlbumStatus();
                break;
        }
    }

    private void changeAlbumStatus() {
        if (album_opt.getTag().toString().equals(getResources().getString(R.string.travel_album_edit))) {
            showDialog();
            album_opt.setText(getResources().getString(R.string.travel_album_cancel));
            album_opt.setTag(getResources().getString(R.string.travel_album_cancel));
        } else {
            dismissDialog();
            album_opt.setText(getResources().getString(R.string.travel_album_edit));
            album_opt.setTag(getResources().getString(R.string.travel_album_edit));
        }
    }

    private Dialog createDialog() {
        Dialog mAlbumDialog = new Dialog(this, R.style.BottomDialog);
        RelativeLayout root = (RelativeLayout) LayoutInflater.from(this).inflate(
                album_edit_dialog, null);
        //初始化视图
        root.findViewById(R.id.album_select_all).setOnClickListener(this);
        root.findViewById(R.id.album_delete).setOnClickListener(this);
        mAlbumDialog.setContentView(root);
        Window dialogWindow = mAlbumDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 0.95f; // 透明度
        dialogWindow.setAttributes(lp);
        return mAlbumDialog;
    }
    private void showDialog(){
        if (null!=mAlbumDialog)
            mAlbumDialog.show();
    }
    private void dismissDialog(){
        if (null!=mAlbumDialog&&mAlbumDialog.isShowing())
            mAlbumDialog.dismiss();
    }
}
