package com.carlt.yema.ui.activity.setting;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
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
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.LocalConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener {

    private static final String TAG = "PersonAvatarActivity";

    private ImageView image_display;
    private TextView avatar_photograph;
    private TextView avatar_album;
    private TextView avatar_cancel;
    private View view;

    private Intent intent;

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
        if (intent != null && !TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            Glide.with(this).load(LoginInfo.getAvatar_img()).into(image_display);
        } else {
            image_display.setImageResource(R.mipmap.default_avater);
        }
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

    public static final String IMAGE_UNSPECIFIED = "image/*";

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
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            //压缩
            Log.d(TAG, LocalConfig.mImageCacheSavePath_SD + ImageName);
            useCrop(Uri.fromFile(new File(LocalConfig.mImageCacheSavePath_SD + ImageName)));
            return;
        }

        if (data == null)
            return;

        // 读取相册
        if (requestCode == PHOTOZOOM) {
            String[] proj = {
                    Images.Media.DATA
            };

            Uri uri = data.getData();
            String type = data.getType();
            if (uri.getScheme().equals("file") && (type.contains("image/"))) {
                String path_uri = uri.getEncodedPath();
                if (path_uri != null) {
                    path_uri = Uri.decode(path_uri);
                    ContentResolver cr = this.getContentResolver();
                    StringBuffer buff = new StringBuffer();
                    buff.append("(").append(Images.ImageColumns.DATA).append("=")
                            .append("'" + path_uri + "'").append(")");
                    Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{Images.ImageColumns._ID},
                            buff.toString(), null, null);
                    int index = 0;
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(Images.ImageColumns._ID);
                        // set _id value
                        index = cur.getInt(index);
                    }
                    if (index == 0) {
                        // do nothing
                    } else {
                        Uri uri_temp = Uri
                                .parse("content://media/external/images/media/"
                                        + index);
                        if (uri_temp != null) {
                            uri = uri_temp;
                        }
                    }
                }
            }
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor;
            if (Build.VERSION.SDK_INT < 11) {
                cursor = managedQuery(uri, proj, null, null, null);
            } else {
                cursor = PersonAvatarActivity.this.getContentResolver().query(uri, proj, null, null, null);
            }
            if (cursor != null) {
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                final String path = cursor.getString(column_index);
                useCrop(Uri.fromFile(new File(path)));
            }
        }
        if (requestCode == PHOTO_REQUEST_CUT) {
            Intent it = new Intent();
            it.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            it.setData(Uri.fromFile(new File(LocalConfig.mImageCacheSavePath_SD + ImageName)));
            sendBroadcast(it);
            Glide.with(this).load(new File(LocalConfig.mImageCacheSavePath_SD + ImageName)).into(image_display);
            new Thread() {
                @Override
                public void run() {
                    Response response = uploadAvatarRequest(new File(LocalConfig.mImageCacheSavePath_SD + ImageName));
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
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统的相册
    public void usePhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
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

    public void useCrop(Uri imageUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);// 启动裁剪程序
    }

    private Response uploadAvatarRequest(File image) {
        Response response = null;
        HashMap<String, Object> params = new HashMap<>();
        params.put("fileOwner", "avatar");
//        params.put("fileData", image.getName());
        try {
            response = HttpLinker.uploadImage(URLConfig.getM_OSS_UPLOAD_URL(), null, image);
            Log.d(TAG, "uploadAvatar-------->" + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
