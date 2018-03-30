package com.carlt.yema.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.carlt.yema.R;
import com.carlt.yema.base.BaseFragment;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.CareerInfo;
import com.carlt.yema.data.home.MilesInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.activity.home.InformationCentreActivity;
import com.carlt.yema.ui.activity.home.ReportActivity;
import com.carlt.yema.ui.view.UUToast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marlon on 2018/3/15.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private ImageView mIvReport;
    private RelativeLayout mRlInformationCentre;
    private TextView mTxtDate;
    private String currentDate;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTxtObd;   //仪表盘里程
    private TextView mTxtEnduranceMile; //续航里程
    private TextView mTxtAvgSpeed;  //平均速度
    private TextView mTxtAvgFuel;   //平均油耗
    private MilesInfo milesInfo;    //野马远程 读取里程实体类

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }

    @Override
    public void init(View view) {
        mIvReport = $ViewByID(R.id.activity_home_iv_report);
        mRlInformationCentre = $ViewByID(R.id.activity_home_relative2);
        mTxtDate = $ViewByID(R.id.home_txt_date);
        mTextView3 = $ViewByID(R.id.activity_home_txt3);
        mTextView4 = $ViewByID(R.id.activity_home_txt4);
        mTxtObd = $ViewByID(R.id.layout_report_grid_linear2_txt2);
        mTxtEnduranceMile = $ViewByID(R.id.layout_report_grid_linear1_txt2);
        mTxtAvgSpeed = $ViewByID(R.id.layout_report_grid_linear3_txt2);
        mTxtAvgFuel = $ViewByID(R.id.layout_report_grid_linear4_txt2);
        mIvReport.setOnClickListener(this);
        mRlInformationCentre.setOnClickListener(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = format.format(new Date(System.currentTimeMillis()));
        mTxtDate.setText(currentDate);
    }

    @Override
    public void loadData() {
        super.loadData();
        CPControl.GetCareerResult(callback);
        CPControl.GetMilesInfoResult(remoteCallback);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_home_iv_report:
                Intent mIntent = new Intent(getContext(), ReportActivity.class);
                mIntent.putExtra("c", 0);
                mIntent.putExtra(ReportActivity.DAY_INITIAL, currentDate);
                startActivity(mIntent);
                break;
            case R.id.activity_home_relative2:
                Intent mIntent1 = new Intent(getContext(), InformationCentreActivity.class);
                startActivity(mIntent1);
                break;
        }
    }

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 0;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 1;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    BaseParser.ResultCallback remoteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 2;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 3;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    actLoadSuccess((CareerInfo)((BaseResponseInfo) msg.obj).getValue());
                    break;
                case 1:
                    actLoadError((CareerInfo)((BaseResponseInfo) msg.obj).getValue());
                    break;
                case 2:
                    loadRemoteSuccess((MilesInfo)((BaseResponseInfo) msg.obj).getValue());
                    break;
                case 3:
                    loadRemoteError((MilesInfo)((BaseResponseInfo) msg.obj).getValue());
                    break;
            }
        }
    };

    private void loadRemoteError(BaseResponseInfo bInfo) {
        UUToast.showUUToast(getContext(),bInfo.getInfo());
    }

    protected void loadRemoteSuccess(BaseResponseInfo bInfo){
        milesInfo = (MilesInfo) bInfo;
        if (milesInfo!=null){
            mTxtObd.setText(milesInfo.getObd()+"");
            mTxtEnduranceMile.setText(milesInfo.getEnduranceMile()+"");
            mTxtAvgSpeed.setText(milesInfo.getAvgSpeed()+"");
            mTxtAvgFuel.setText(milesInfo.getAvgFuel()+"");
        }
    }


    @Override
    protected void actLoadNoData() {
        super.actLoadNoData();
    }

    @Override
    protected void actLoadError(BaseResponseInfo bInfo) {
        super.actLoadError(bInfo);
    }

    @Override
    protected void actLoadSuccess(BaseResponseInfo binfo) {
        super.actLoadSuccess(binfo);
        CareerInfo mCareerInfo = (CareerInfo) binfo;

        if (mCareerInfo != null) {
            // 车秘书未读信息条数
            String unreadmessage = mCareerInfo.getUnreadmessage();
            // 车秘书最新消息内容
            String latestmessage = mCareerInfo.getLatestmessage();

            if (unreadmessage != null && unreadmessage.length() > 0) {
                if (unreadmessage.equals("0")) {
                    mTextView3.setVisibility(View.GONE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView4.setText("最后一条：" + latestmessage + "");
                    } else {
                        mTextView4.setText("最后一条：");
                    }
                } else {
                    mTextView3.setVisibility(View.VISIBLE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView3.setText(unreadmessage);
                        mTextView4.setText("新消息：" + latestmessage + "");
                    } else {
                        mTextView4.setText("新消息：");
                    }
                }
            } else {
                mTextView3.setVisibility(View.GONE);
                if (latestmessage != null && latestmessage.length() > 0
                        && !latestmessage.equals("null")) {
                    mTextView4.setText(latestmessage + "");
                } else {
                    mTextView4.setText("");
                }
            }
        }
    }
}
