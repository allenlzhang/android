package com.hz17car.yema.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hz17car.yema.ui.fragment.CarMainFragment;
import com.hz17car.yema.ui.fragment.HomeFragment;
import com.hz17car.yema.ui.fragment.RemoteMainFragment;
import com.hz17car.yema.ui.fragment.TestFragment;


/**
 * Created by Marlon on 2017/12/26.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private int numTab;
    public FragmentAdapter(FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                CarMainFragment carMainFragment = new CarMainFragment();
                return carMainFragment;
            case 2:
                RemoteMainFragment remoteMainFragment = new RemoteMainFragment();
                return remoteMainFragment;
            case 3:
                TestFragment fourFragment = new TestFragment();
                fourFragment.setText("设置");
                return fourFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numTab;
    }
}
