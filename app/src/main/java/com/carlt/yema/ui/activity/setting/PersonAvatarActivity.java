package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;

public class PersonAvatarActivity extends LoadingActivity {

    private ImageView image_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        initComponent();
    }

    private void initComponent() {
        image_display=$ViewByID(R.id.image_display);
        Glide.with(this).load(getIntent().getStringExtra("imagePath")).into(image_display);
    }


}
