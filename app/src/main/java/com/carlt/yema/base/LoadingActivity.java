
package com.carlt.yema.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.protocolparser.BaseParser;


/**
 * 有加载效果的Activity的基类
 *
 * @author Administrator
 */
public class LoadingActivity extends BaseActivity {
    protected TextView mTxtDes;// 描述文字
    protected TextView mTxtEorrorSub;//错误信息副标题
    protected TextView mTxtNodata;// 没有数据时的描述文字
    protected Button mTxtRetryError;// 重试（刷新）
    private ProgressBar mPBar;
    private RelativeLayout mLayMain;
    private View mMainView;
    private View mViewLoading;// 加载View
    private View mViewError;// 错误提示View
    private View mViewNodata;// 没有数据View

    LayoutInflater mInflater;

    protected View backTV = null;
    protected TextView titleTV = null;
    protected TextView btnOpt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_loading);
        mLayMain = (RelativeLayout) findViewById(R.id.loading_lay_mainlayout);
        mViewLoading = findViewById(R.id.laoding_lay_main);
        mViewError = findViewById(R.id.error_lay_main);
        mViewNodata = findViewById(R.id.nodata_lay_main);

        mTxtDes = (TextView) findViewById(R.id.laoding_txt_des);
        mTxtEorrorSub = (TextView) findViewById(R.id.error_txt_des_sub);
        mTxtNodata = (TextView) findViewById(R.id.nodata_txt_des);
        mTxtRetryError = (Button) findViewById(R.id.error_txt_retry);
        mPBar = (ProgressBar) findViewById(R.id.loading_bar_loading);

        mTxtRetryError.setOnClickListener(mClickListener);
        setMainView(layoutResID);
    }


    /**
     * 使用此方法，需要在 setContentView activity 里 加入layout_title
     *
     * 只有 一个文字标题和返回键的标题
     * @param titleString
     */
    protected void initTitle(String titleString) {

        try{
            backTV = $ViewByID(R.id.back);
            titleTV = $ViewByID(R.id.title);
            btnOpt = $ViewByID(R.id.btnOpt);
        }catch (Exception e){
            //是设置标题出错
            return;
        }
        if(null != backTV){
            backTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        if(null != titleTV){
            titleTV.setText(titleString);
        }
    }

    protected void setBtnOptVisible(boolean visible){
        if (visible) {
            btnOpt.setVisibility(View.VISIBLE);
        } else {
            btnOpt.setVisibility(View.GONE);
        }
    }

    protected void setBtnOptText(String text){
        btnOpt.setText(text);
    }

    protected void setOnBtnOptClickListener(OnClickListener listener){
        btnOpt.setOnClickListener(listener);
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.error_txt_retry:
                   loadingDataUI();
                    reTryLoadData();
                    break;
            }

        }
    };


    /**
     * 添加主View
     *
     * @param layoutId
     */
    private void setMainView(int layoutId) {
        mMainView = LayoutInflater.from(this).inflate(layoutId, null);
        LayoutParams mLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.BELOW, R.id.layout_subtitle);
        mMainView.setLayoutParams(mLayoutParams);
        mLayMain.addView(mMainView);
    }

    /**
     * 数据加载成功
     *
     */
    public void loadSuccessUI() {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载数据
     */
    protected void loadingDataUI() {
        mTxtRetryError.setVisibility(View.GONE);
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.GONE);
        mPBar.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败
     *
     * @param error
     */
    public void loadonErrorUI(BaseResponseInfo error) {
        //并不需要
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.VISIBLE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.GONE);
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
    public void loadNodataUI() {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.VISIBLE);
        mMainView.setVisibility(View.GONE);
        mTxtNodata.setText("暂时没有内容");
    }

    protected BaseParser.ResultCallback mCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            loadDataSuccess(bInfo);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadonErrorUI(bInfo);
            loadDataError(bInfo);
        }
    };

    public void loadDataError(Object bInfo) {
    }

    public void loadDataSuccess(Object bInfo) {

    }


    public void reTryLoadData() {
        //子类选择可以实现，重新加载
    }

}
