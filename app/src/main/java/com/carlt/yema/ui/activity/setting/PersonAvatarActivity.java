package com.carlt.yema.ui.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.set.AvatarInfo;
import com.carlt.yema.http.HttpLinker;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.FileUtil;
import com.carlt.yema.utils.LocalConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener {

    private static final String TAG = "PersonAvatarActivity";

    public static final String IMAGE_UNSPECIFIED = "image/*";//系统相册mime

    private ImageView image_display;//头像展示
    private TextView avatar_photograph;//调用系统相机
    private TextView avatar_album;//调用系统相册
    private TextView avatar_cancel;//取消按钮
    private View view;

    private Intent intent;

    private String avatarName;
    private String avatarPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_display);
        intent = getIntent();
        initTitle("");
        initComponent();
        setBtnOptText("修改");
        setBtnOptVisible(true);
        setOnBtnOptClickListener(this);
        loadSuccessUI();
    }

    private void initComponent() {
        image_display = $ViewByID(R.id.avatar_display);
        view = $ViewByID(R.id.avatar_image_opt);
        avatar_photograph = $ViewByID(R.id.avatar_photograph);
        avatar_photograph.setOnClickListener(this);
        avatar_album = $ViewByID(R.id.avatar_album);
        avatar_album.setOnClickListener(this);
        avatar_cancel = $ViewByID(R.id.avatar_cancel);
        avatar_cancel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if (intent != null && !TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            Glide.with(this).load(LoginInfo.getAvatar_img()).into(image_display);
        } else {
            image_display.setImageResource(R.mipmap.default_avater);
        }
        super.onResume();
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

    /**
     * 以下部分是用于头像拍照上传
     **/

    public static final int NONE = 0;

    public static final int PHOTOHRAPH = 1;// 拍照

    public static final int PHOTOZOOM = 2; // 缩放

    public static final int PHOTORESOULT = 3;// 结果

    private static final int PHOTO_REQUEST_CUT = 5;// 结果

    public static final int QRCode = 4;


    private String ImageName;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AvatarInfo info = (AvatarInfo) msg.obj;
            Intent backIntent = new Intent(PersonAvatarActivity.this, PersonInfoActivity.class);
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(PersonAvatarActivity.this, "头像上传成功");
                    intent.putExtra("imageId", info.getId());
                    break;
                case 1:
                    intent.putExtra("imageId", "-1");
                    UUToast.showUUToast(PersonAvatarActivity.this, "头像上传失败");
                    break;

            }
            PersonAvatarActivity.this.setResult(RESULT_OK, backIntent);
            finish();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    startPhotoZoom(data.getData());
                    break;
                // 如果是调用相机拍照时
                case 2:
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + avatarName);
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //图片路径
            avatarPath = FileUtil.saveFile(PersonAvatarActivity.this, "temphead.jpg", photo);
            System.out.println("----------路径----------" + avatarPath);
        }
    }

    // 调用系统的相册
    public void usePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 1);
    }

    public void useCamera() {
        avatarName = System.currentTimeMillis() + ".jpg";
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(LocalConfig.mImageCacheSavePath_SD, avatarName)));
        startActivityForResult(intent, 2);
    }

    private Response uploadAvatarRequest(File image) {
        Response response = null;
        HashMap<String, Object> params = new HashMap<>();
        params.put("fileOwner", "avatar");
        try {
            response = HttpLinker.uploadImage(URLConfig.getM_OSS_UPLOAD_URL(), params, image);
            Log.d(TAG, "uploadAvatar-------->" + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
