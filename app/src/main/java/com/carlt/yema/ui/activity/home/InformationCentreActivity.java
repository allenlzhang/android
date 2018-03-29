package com.carlt.yema.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.InformationCentreInfo;
import com.carlt.yema.data.home.InformationCentreInfoList;

import java.util.ArrayList;

/**
 * Created by Marlon on 2018/3/16.
 */

public class InformationCentreActivity extends LoadingActivity {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    public final static String TIPS_TITLE = "tips_title";

    public final static String TIPS_TYPE = "tips_type";

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_centre);
        setTitle(R.layout.head_back);

        initTitle();
        init();
        loadingDataUI();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("信息中心");
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void init() {
        mListView = (ListView) findViewById(R.id.activity_information_centre_list);
    }



    @Override
    public void loadDataSuccess(Object data) {
        if (data != null) {
            InformationCentreInfoList mCentreInfoLists;
            mCentreInfoLists = (InformationCentreInfoList) data;
            final ArrayList<InformationCentreInfo> mList = mCentreInfoLists.getmAllList();
            TypeAdapter mAdapter = new TypeAdapter(mList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    InformationCentreInfo mInfo = mList.get(position);
                    String title_s = mInfo.getName();
                    int type = mInfo.getId();
                    Intent mIntent = new Intent(InformationCentreActivity.this, RemindActivity.class);
                    mIntent.putExtra(TIPS_TITLE, title_s);
                    mIntent.putExtra(TIPS_TYPE, type);
                    startActivity(mIntent);

                }
            });

            int unreadCount = mCentreInfoLists.getUnreadCount();
//            if (unreadCount > 0) {
//                mTextViewSecretary.setText(LoginInfo.getSecretaryName() + "：您有未读消息哦！");
//            } else {
//                mTextViewSecretary.setText(LoginInfo.getSecretaryName() + "：我是您的私人汽车助理");
//            }
        }
    }



    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (count > 0) {
            loadingDataUI();
        }
        count++;
    }

    class TypeAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private ArrayList<InformationCentreInfo> mList;

        public TypeAdapter(ArrayList<InformationCentreInfo> list) {
            mInflater = LayoutInflater.from(InformationCentreActivity.this);
            mList = list;
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            convertView = mInflater.inflate(R.layout.list_item_secretary_types, null);
            TextView mTxtTile = (TextView) convertView.findViewById(R.id.activity_information_centre_title);
            TextView mTxtDes = (TextView) convertView.findViewById(R.id.activity_information_centre_des);
            ImageView mImgIcon = (ImageView) convertView.findViewById(R.id.activity_information_centre_icon);
            ImageView mImgDot = (ImageView) convertView.findViewById(R.id.activity_information_centre_dot);
            View line = convertView.findViewById(R.id.line);

            line.setVisibility(View.VISIBLE);
            InformationCentreInfo mInfo = mList.get(position);
            if(position == mList.size() - 1){
                line.setVisibility(View.INVISIBLE);
            }

            String string;
            // 类型名称
            string = mInfo.getName();
            if (string != null && string.length() > 0) {
                mTxtTile.setText(string);
            }

            int imgId = mInfo.getImg();
            if (imgId > 0) {
                mImgIcon.setImageResource(mInfo.getImg());
            }

            // 最后一条消息内容
            string = mInfo.getLastmsg();
            int count = mInfo.getMsgcount();
            if (count > 0) {
                mImgDot.setVisibility(View.VISIBLE);
                if (string != null && string.length() > 0) {
                    mTxtDes.setText("新消息：" + string);
                } else {
                    mTxtDes.setText("");
                }
            } else {
                mImgDot.setVisibility(View.GONE);
                if (string != null && string.length() > 0) {
                    mTxtDes.setText("最后一条：" + string);
                } else {
                    mTxtDes.setText("还没有消息");
                }
            }
            return convertView;
        }

    }

}
