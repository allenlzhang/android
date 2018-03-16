package com.hz17car.yema.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hz17car.yema.R;
import com.hz17car.yema.data.BaseResponseInfo;
import com.hz17car.yema.protocolparser.BaseParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/12/24.
 */

public class BaseTabActivity extends BaseActivity {
    protected TextView mTxtDes;// 描述文字

    protected TextView mTxtEorrorSub;//错误信息副标题

    protected TextView mTxtNodata;// 没有数据时的描述文字

    protected Button mTxtRetryError;// 重试（刷新）

    private ProgressBar mPBar;

    private View mViewLoading;// 加载View

    private View mViewError;// 错误提示View

    private View mViewNodata;// 没有数据View

    protected RelativeLayout mTitleView;

    protected LinearLayout mSubTitleView;

    protected LayoutInflater mInflater;

    protected int mIndex = 0;

    //所有的二级标题
    protected List<SubItemHolder> mTitleHolders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
        super.setContentView(R.layout.activity_comm_tab);
        initBase();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = mInflater.inflate(layoutResID, null);
        LinearLayout fl = $ViewByID(R.id.tab_fragment_container);
        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(flp);
        fl.addView(view);
    }

    protected void initBase() {
        mViewLoading = findViewById(R.id.laoding_lay_main);
        mViewError = findViewById(R.id.error_lay_main);
        mViewNodata = findViewById(R.id.nodata_lay_main);
        mSubTitleView = $ViewByID(R.id.layout_subtitle);
        mTitleView = $ViewByID(R.id.title);

        mTxtDes = (TextView) findViewById(R.id.laoding_txt_des);
        mTxtEorrorSub = (TextView) findViewById(R.id.error_txt_des_sub);
        mTxtNodata = (TextView) findViewById(R.id.nodata_txt_des);
        mTxtRetryError = (Button) findViewById(R.id.error_txt_retry);
        mPBar = (ProgressBar) findViewById(R.id.loading_bar_loading);

        mTxtRetryError.setOnClickListener(mClickListener);
    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.error_txt_retry:
                    LoadData();
                    break;
            }

        }
    };


    protected void initSubTitle(String[] titles) {
        mSubTitleView.setVisibility(View.VISIBLE);
        for (int i = 0; i < titles.length; i++) {
            String txt = titles[i];
            View v = mInflater.inflate(R.layout.title_sub_item_layout, null);
            SubItemHolder sih = new SubItemHolder();
            sih.v = v;
            sih.mLine = v.findViewById(R.id.title_sub_item_line);
            sih.mSubTitle = (TextView) v.findViewById(R.id.title_sub_item_txt);
            sih.mSubTitle.setText(txt);
            v.setTag(i);
            v.setOnClickListener(subTitleClick);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1;
            v.setLayoutParams(param);
            mSubTitleView.addView(v);
            mTitleHolders.add(sih);
        }
        SubItemHolder si = mTitleHolders.get(0);
        si.mLine.setVisibility(View.VISIBLE);
        si.mSubTitle.setTextColor(getResources().getColor(R.color.app_white));
    }

    /**
     * 加载数据
     */
    protected void LoadData() {
        mTxtRetryError.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);

        mPBar.setVisibility(View.VISIBLE);
        //mTxtDes.setText("loading...");
    }

    View.OnClickListener subTitleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int dx = (int) v.getTag();
            if (dx != mIndex) {
                SubItemHolder ss = mTitleHolders.get(mIndex);
                ss.mLine.setVisibility(View.GONE);
                ss.mSubTitle.setTextColor(getResources().getColor(R.color.app_white));
                mIndex = dx;
                switch (dx) {
                    case 0:
                        SubItemHolder si = mTitleHolders.get(dx);
                        si.mLine.setVisibility(View.VISIBLE);
                        si.mSubTitle.setTextColor(getResources().getColor(R.color.app_white));
                        break;
                    case 1:
                        SubItemHolder si2 = mTitleHolders.get(dx);
                        si2.mLine.setVisibility(View.VISIBLE);
                        si2.mSubTitle.setTextColor(getResources().getColor(R.color.app_white));
                        break;
                    case 2:
                        SubItemHolder si3 = mTitleHolders.get(dx);
                        si3.mLine.setVisibility(View.VISIBLE);
                        si3.mSubTitle.setTextColor(getResources().getColor(R.color.app_white));
                        break;
                }
                LoadData();
            }
        }
    };


    /**
     * 数据加载成功
     *
     * @param data
     */
    public void LoadSuccess(BaseResponseInfo data) {
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     *
     * @param error
     */
    public void LoadError(BaseResponseInfo error) {
        //并不需要
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.VISIBLE);
        mViewNodata.setVisibility(View.GONE);
        mPBar.setVisibility(View.GONE);

        BaseResponseInfo mInfo = (BaseResponseInfo) error;
        String info = "";
        if (mInfo != null) {
            info = mInfo.getInfo();
        } else {
            info = "数据加载失败，请重试...";
        }
        mTxtRetryError.setVisibility(View.VISIBLE);
        mTxtEorrorSub.setText(info);
    }

    /**
     * 没有数据
     */
    public void LoadNodata() {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.VISIBLE);
        mTxtNodata.setText("暂时没有内容");
    }

    protected BaseParser.ResultCallback mCallback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(Object bInfo) {
            LoadSuccess((BaseResponseInfo) bInfo);
        }

        @Override
        public void onError(Object bInfo) {
            LoadError((BaseResponseInfo)bInfo);
        }
    };

    public void addFragment(BaseFragment fragment) {
        fragment.setLoadingActivity(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tab_fragment_container, fragment)
                .commit();
    }

    public void removeFragment(BaseFragment fragment) {
        fragment.setLoadingActivity(null);
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

}
