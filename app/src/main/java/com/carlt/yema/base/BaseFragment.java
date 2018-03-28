package com.carlt.yema.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by user on 2016/12/23.
 */

public abstract class BaseFragment extends Fragment {
    protected View mView;
    private boolean isDestory = false;
    protected Context mCtx;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isDestory = false;
        if (mView == null) {
            mCtx = getActivity();
            mView = inflateView(inflater);
            init(mView);
        }
        return mView;
    }

    protected abstract View inflateView(LayoutInflater inflater);

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory = true;
        ViewGroup vg = ((ViewGroup) mView.getParent());
        if (vg != null) {
            vg.removeView(mView);
        }
    }

    public void init(View view) {
    }

    /***
     * 加载数据调用
     */
    public void loadData() {
//        isDestory = false;
    }

    protected void actLoadError(BaseResponseInfo bInfo) {
    }

    protected void actLoadNoData() {
    }

    protected void actLoadSuccess(BaseResponseInfo binfo) {
    }

    /**
     * 简化按 ID 查找
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(int id) {
        return mView == null ? null : (T) mView.findViewById(id);
    }

    /**
     * 简化按 ID 查找
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(View v, int id) {
        return v == null ? null : (T) v.findViewById(id);
    }



}
