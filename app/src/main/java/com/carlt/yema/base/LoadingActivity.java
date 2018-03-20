
package com.carlt.yema.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
public class LoadingActivity extends BaseActivity{
    protected ImageView back;//返回键
    protected TextView title;//标题
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

        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);

        mTxtDes = (TextView) findViewById(R.id.laoding_txt_des);
        mTxtEorrorSub = (TextView) findViewById(R.id.error_txt_des_sub);
        mTxtNodata = (TextView) findViewById(R.id.nodata_txt_des);
        mTxtRetryError = (Button) findViewById(R.id.error_txt_retry);
        mPBar = (ProgressBar) findViewById(R.id.loading_bar_loading);

        mTxtRetryError.setOnClickListener(mClickListener);
        setMainView(layoutResID);
    }


    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.error_txt_retry:
                    LoadData();
                    reTryLoadData();
                    break;
            }

        }
    };

    /**
     * Change the title associated with this activity.  If this is a
     * top-level activity, the title for its window will change.  If it
     * is an embedded activity, the parent can do whatever it wants
     * with it.
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

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
     * @param data
     */
    public void LoadSuccess(BaseResponseInfo data) {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载数据
     */
    protected void LoadData() {
        mTxtRetryError.setVisibility(View.GONE);
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.GONE);

        mPBar.setVisibility(View.VISIBLE);
//        mTxtDes.setText("loading...");
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
    public void LoadNodata() {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.VISIBLE);
        mMainView.setVisibility(View.GONE);

        mTxtNodata.setText("暂时没有内容");
    }

    protected BaseParser.ResultCallback mCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(Object bInfo) {
            LoadSuccess((BaseResponseInfo) bInfo);
        }

        @Override
        public void onError(Object bInfo) {
            LoadError((BaseResponseInfo) bInfo);
        }
    };

    public void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    private OnClickListener onClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.title_img_back:
//                    //标题头左侧按钮
//                    if (mClickTitleListener != null) {
//                        mClickTitleListener.titleLeftClik();
//                    } else {
//                        onBackPressed();
//                    }
//                    break;
//                case R.id.title_txt_right:
//                    //标题头右侧按钮
//                    if (mClickTitleListener != null) {
//                        mClickTitleListener.titleRightClik();
//                    } else {
//
//                    }
//                    break;
//
//            }
        }
    };

    public void reTryLoadData(){
        //子类选择可以实现，重新加载
    };

//    protected CRControl.GetResultListCallback listener = new CRControl.GetResultListCallback() {
//
//        @Override
//        public void onFinished(Object o) {
//            Message msg = new Message();
//            msg.what = 0;
//            msg.obj = o;
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onErro(Object o) {
//            Message msg = new Message();
//            msg.what = 1;
//            msg.obj = o;
//            mHandler.sendMessage(msg);
//
//        }
//    };
//
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    LoadSuccess(msg.obj);
//                    break;
//
//                case 1:
//                    LoadError(msg.obj);
//                    break;
//            }
//        }
//    };
}
