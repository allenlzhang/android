package com.carlt.yema.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.carlt.yema.base.BaseFragment;
import com.carlt.yema.R;

/**
 * Created by Marlon on 2018/3/15.
 */

public class HomeFragment extends BaseFragment {
    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }
}
