package com.carlt.yema.ui.activity.setting;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.carlt.yema.protocolparser.user.AvatarParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.FileUtil;
import com.carlt.yema.utils.LocalConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener, EasyPermissions.PermissionCallbacks {

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

    private static final int CAMERA_WRITE_READ=3;

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
//                useCamera();
                methodRequires3Permission();
                break;
            case R.id.avatar_album:
                usePhoto();
                break;
            case R.id.avatar_cancel:
                this.view.setVisibility(View.GONE);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AvatarInfo info = (AvatarInfo) msg.obj;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Intent backIntent = new Intent(PersonAvatarActivity.this, PersonInfoActivity.class);
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(PersonAvatarActivity.this, "头像上传成功");
                    intent.putExtra("imageId", info.getId());
                    LoginInfo.setAvatar_img(info.getFilePath());
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

                    File temp = new File(LocalConfig.mImageCacheSavePath_SD
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
            final File avatar = new File(avatarPath);
            Glide.with(this).load(avatar).into(image_display);
            mDialog = PopBoxCreat.createDialogWithProgress(PersonAvatarActivity.this, "正在上传...");
            mDialog.show();
            new Thread() {
                @Override
                public void run() {
                    Response response = uploadAvatarRequest(avatar);
                    if (response != null && response.isSuccessful()) {
                        JsonParser jp = new JsonParser();
                        AvatarInfo avatarInfo = null;
                        try {
                            JsonElement element = jp.parse(response.body().string());
                            JsonObject mJson = element.getAsJsonObject();
                            int flag = mJson.get("code").getAsInt();
                            String info = mJson.get("msg").getAsString();
                            Log.d(TAG, "upload flag--------------------------------->" + flag);
                            Log.d(TAG, "upload msg--------------------------------->" + info);
                            if (flag == 200) {
                                avatarInfo = AvatarParser.parser(mJson);
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = avatarInfo;
                                handler.sendMessage(msg);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
//                            String url=mJson.get("url").getAsString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    super.run();
                }
            }.start();
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

    private Dialog mDialog;

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

    String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        methodRequires3Permission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        UUToast.showUUToast(this, "您未授权授权头像上传需要的权限，如需使用请到对野马管家权限进行设置");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @AfterPermissionGranted(CAMERA_WRITE_READ)
    private void methodRequires3Permission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            useCamera();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "拍照上传头像需要SDcard读写权限，相机使用权限",
                    CAMERA_WRITE_READ, perms);
        }
    }
}
