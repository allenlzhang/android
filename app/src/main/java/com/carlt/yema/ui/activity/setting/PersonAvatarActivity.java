package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.utils.LocalConfig;

import java.io.File;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener{

    private ImageView image_display;
    private TextView avatar_photograph;
    private TextView avatar_album;
    private TextView avatar_cancel;
    private View view;
    /** 以下部分是用于头像拍照上传 **/

    public static final int NONE = 0;

    public static final int PHOTOHRAPH = 1;// 拍照

    public static final int PHOTOZOOM = 2; // 缩放

    public static final int PHOTORESOULT = 3;// 结果

    public static final int QRCode = 4;

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private String ImageName;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_display);
        intent=getIntent();
        initTitle("");
        initComponent();
        setBtnOptText("修改");
        setBtnOptVisible(true);
        setOnBtnOptClickListener(this);
        loadSuccessUI();
    }

    private void initComponent() {
        image_display=$ViewByID(R.id.avatar_display);
        view=$ViewByID(R.id.avatar_image_opt);
        avatar_photograph=$ViewByID(R.id.avatar_photograph);
        avatar_photograph.setOnClickListener(this);
        avatar_album=$ViewByID(R.id.avatar_album);
        avatar_album.setOnClickListener(this);
        avatar_cancel=$ViewByID(R.id.avatar_cancel);
        avatar_cancel.setOnClickListener(this);
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("avatarPath"))) {
            Glide.with(this).load(getIntent().getStringExtra("avatarPath")).into(image_display);
        } else {
            image_display.setImageResource(R.mipmap.default_avater);
        }
//        Glide.with(this).load(getIntent().getStringExtra("imagePath")).into(image_display);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpt:
                if (this.view.getVisibility() == View.GONE) {
                    this.view.setVisibility(View.VISIBLE);
                } else {
                    this.view.setVisibility(View.GONE);
                }
                break;
            case R.id.avatar_photograph:
                useCamera();
                break;
            case R.id.avatar_album:
                usePhoto();
                break;
            case R.id.avatar_cancel:
                this.view.setVisibility(View.GONE);
                break;
        }
    }


    // 调用系统的相册
    public void usePhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        // 调用剪切功能
        startActivityForResult(intent, PHOTOZOOM);
    }

    public void useCamera() {
        ImageName = System.currentTimeMillis() + ".jpg";
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(LocalConfig.mImageCacheSavePath_SD, ImageName)));
        startActivityForResult(intent, PHOTOHRAPH);
    }

}
