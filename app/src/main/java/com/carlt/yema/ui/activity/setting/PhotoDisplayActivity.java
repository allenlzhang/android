package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
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
import com.carlt.yema.ui.view.UUToast;

import java.util.HashMap;

public class PhotoDisplayActivity extends LoadingActivity implements View.OnClickListener {

    private ImageView image_display;
    private TextView photo_save;
    private TextView photo_delete;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
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
                break;
            case R.id.photo_delete:
                deleteImages();
                break;
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

    /**
     * 删除接口回调
     */
    private BaseParser.ResultCallback deleteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PhotoDisplayActivity.this, "相册删除成功");

        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PhotoDisplayActivity.this, "相册删除失败");
        }
    };
}
