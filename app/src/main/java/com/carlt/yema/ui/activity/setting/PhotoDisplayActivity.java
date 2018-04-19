package com.carlt.yema.ui.activity.setting;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.LocalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class PhotoDisplayActivity extends LoadingActivity implements View.OnClickListener {

    private ImageView image_display;
    private TextView photo_save;
    private TextView photo_delete;
    private Intent intent;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        initTitle("");
        intent = getIntent();
        initComponent();
    }

    private void initComponent() {
        image_display = $ViewByID(R.id.image_display);
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("imagePath"))) {
            Glide.with(this).load(intent.getStringExtra("imagePath")).into(image_display);
        }
        photo_save = $ViewByID(R.id.photo_save);
        photo_save.setOnClickListener(this);
        photo_delete = $ViewByID(R.id.photo_delete);
        photo_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.photo_save:
                requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        mDialog = PopBoxCreat.createDialogWithProgress(PhotoDisplayActivity.this, "正在保存...");
                        mDialog.show();
                        final String url = intent.getStringExtra("imagePath");
                        if (intent != null && !TextUtils.isEmpty(url)) {
                            new Thread() {
                                @Override
                                public void run() {
                                    savePicture(url);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                    super.run();
                                }
                            }.start();
                        }
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(PhotoDisplayActivity.this,"部分权限获取失败，正常功能受到影响");
                    }
                });

                break;
            case R.id.photo_delete:
                mDialog = PopBoxCreat.createDialogWithProgress(this, "正在删除...");
                mDialog.show();
                if (intent.getIntExtra("imageId", -1) > 0) {
                    deleteImages();
                } else {
                    deleteLocaleImage();
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mDialog!=null) {
                        mDialog.dismiss();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void savePicture(String url) {
        try {
            URL pictureUrl = new URL(url);
            InputStream in = pictureUrl.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();
            String pictureName = LocalConfig.mTravelImageCacheSavePath_SD + "travel_"+ intent.getStringExtra("imageId")+ ".png";
            if (url.equals(pictureName)) {
                return;
            }
            File file = new File(pictureName);
            FileOutputStream out;

            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    * 删除照片
    * */
    private void deleteImages() {
        if (intent != null) {
            DefaultStringParser parser = new DefaultStringParser(deleteCallback);
            HashMap<String, String> params = new HashMap<>();
            params.put("id", intent.getIntExtra("imageId", -1) + "");
            parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
        }
    }

    private void deleteLocaleImage(){
        if (TextUtils.isEmpty(intent.getStringExtra("imagePath"))) {
            return;
        }
        File imageFile=new File(intent.getStringExtra("imagePath"));
        imageFile.delete();
        if (mDialog!=null) {
            mDialog.dismiss();
        }
    }

    /**
     * 删除接口回调
     */
    private BaseParser.ResultCallback deleteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PhotoDisplayActivity.this, "相册删除成功");
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PhotoDisplayActivity.this, "相册删除失败");
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    };
}
