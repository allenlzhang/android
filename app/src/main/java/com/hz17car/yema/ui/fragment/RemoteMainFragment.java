package com.hz17car.yema.ui.fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.hz17car.yema.R;
import com.hz17car.yema.base.BaseFragment;
import com.hz17car.yema.utils.PlayRadio;

/**
 * Created by liu on 2018/3/16.
 *
 * 远程页面
 *
 */

public class RemoteMainFragment extends BaseFragment  implements
        AdapterView.OnItemClickListener, View.OnClickListener, View.OnTouchListener {

    private View view;
    //效果声音播放组件
    private PlayRadio mPlayRadio;

    @Override
    protected View inflateView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_remote_main,null,false);
        mPlayRadio = PlayRadio.getInstance(getActivity());
        init();
        return view;
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
