package com.carlt.yema.ui.activity.setting;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.carlt.yema.utils.PhotoUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

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
                requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                            imageUri = Uri.fromFile(fileUri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                //通过FileProvider创建一个content类型的Uri
                                imageUri = FileProvider.getUriForFile(PersonAvatarActivity.this, "com.carlt.yema.fileprovider", fileUri);
                        PhotoUtils.takePicture(PersonAvatarActivity.this, imageUri, CODE_CAMERA_REQUEST);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(PersonAvatarActivity.this,"部分权限获取失败，正常功能受到影响");
                    }
                });
                break;
            case R.id.avatar_album:
                requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        PhotoUtils.openPic(PersonAvatarActivity.this, CODE_GALLERY_REQUEST);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(PersonAvatarActivity.this,"部分权限获取失败，正常功能受到影响");
                    }
                });
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
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.carlt.yema.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    setPicToView(bitmap);
                    break;
            }
        }

            super.onActivityResult(requestCode, resultCode, data);
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
    private void setPicToView(Bitmap bitmap) {

            //图片路径
            avatarPath = FileUtil.saveFile(PersonAvatarActivity.this, "temphead.jpg", bitmap);
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
        //创建一个file，用来存储拍照后的照片
        File outputfile = new File(LocalConfig.mImageCacheSavePath_SD
                + avatarName);
        try {
            if (outputfile.exists()){
                outputfile.delete();//删除
            }
            outputfile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageuri ;
        if (Build.VERSION.SDK_INT >= 24){
            imageuri = FileProvider.getUriForFile(this,
                    "com.carlt.yema", //可以是任意字符串
                    outputfile);
        }else{
            imageuri = Uri.fromFile(outputfile);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,2);
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


}
