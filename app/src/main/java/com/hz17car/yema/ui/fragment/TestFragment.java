package com.hz17car.yema.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hz17car.yema.R;

/**
 * Created by Marlon on 2018/3/14.
 */

public class TestFragment extends Fragment {
    private View view;
    private TextView mTextView;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout,container,false);
        mTextView = (TextView) view.findViewById(R.id.fragment_text);
        mTextView.setText(text);
        return view;
    }
}
