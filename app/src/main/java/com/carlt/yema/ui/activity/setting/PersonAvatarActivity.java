package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener{

    private ImageView image_display;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_display);
        initComponent();
        setBtnOptText("修改");
        setBtnOptVisible(true);
        setOnBtnOptClickListener(this);
    }

    private void initComponent() {
        image_display=$ViewByID(R.id.image_display);
        view=$ViewByID(R.id.avatar_image_opt);
        Glide.with(this).load(getIntent().getStringExtra("imagePath")).into(image_display);
    }


    @Override
    public void onClick(View view) {
        if (this.view.getVisibility() == View.GONE) {
            this.view.setVisibility(View.VISIBLE);
        } else {
            this.view.setVisibility(View.GONE);
        }

    }

}
