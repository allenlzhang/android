package com.hz17car.yema.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseFragment;

/**
 * Created by liu on 2018/3/16.
 */

public class CarMainFragment extends BaseFragment {
    @Override
    protected View inflateView(LayoutInflater inflater) {
       View view = inflater.inflate(R.layout.layout_carmain,null,false);
        return view;
    }
}
