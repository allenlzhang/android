package com.hz17car.yema.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hz17car.yema.ui.fragment.HomeFragment;
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
                TestFragment twoFragment = new TestFragment();
                twoFragment.setText("座驾");
                return twoFragment;
            case 2:
                TestFragment threeFragment = new TestFragment();
                threeFragment.setText("远程");
                return threeFragment;
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
