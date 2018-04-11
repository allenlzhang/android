package com.carlt.yema;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.ui.activity.login.UserLoginActivity;
import com.carlt.yema.ui.adapter.FragmentAdapter;
import com.carlt.yema.ui.view.NoScrollViewPager;

public class MainActivity extends BaseActivity {

    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;

    private String [] tabTexts = {"首页","座驾","远程","我的"};
    private int [] tabIcons = {R.drawable.tab_home_bg,R.drawable.tab_car_bg,R.drawable.tab_remote_bg,
            R.drawable.tab_my_bg};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void init(){
        mViewPager =  findViewById(R.id.main_view_pager);
        mTabLayout = findViewById(R.id.main_tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),mTabLayout.getTabCount()));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setCustomView(getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1));
        mTabLayout.getTabAt(2).setCustomView(getTabView(2));
        mTabLayout.getTabAt(3).setCustomView(getTabView(3));
        mViewPager.setNoScroll(true);

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        TextView txt_title = (TextView) view.findViewById(R.id.tab_custom_txt);
        txt_title.setText(tabTexts[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.tab_custom_iv);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
