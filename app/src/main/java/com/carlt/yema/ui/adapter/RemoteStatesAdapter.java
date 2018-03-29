package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.car.CarStateInfo;

import java.util.ArrayList;


/**
 * 远程车辆状态Adapter
 * 
 * @author daisy
 */
public class RemoteStatesAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private Context mContext;

	private ArrayList<CarStateInfo> mDataList = new ArrayList<CarStateInfo>();

	public RemoteStatesAdapter(Context context, ArrayList<CarStateInfo> dataList) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mDataList = dataList;
	}

	public void setmDataList(ArrayList<CarStateInfo> dataList) {
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.grid_remote_states_item,
					null);
			convertView.setTag(mHolder);
			mHolder.mImgIcon = (ImageView) convertView
					.findViewById(R.id.remote_states_item_img_icon);
			mHolder.mTxtName = (TextView) convertView
					.findViewById(R.id.remote_states_item_txt_name);
			mHolder.mTxtState = (TextView) convertView
					.findViewById(R.id.remote_states_item_txt_state);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		CarStateInfo mInfo = mDataList.get(position);
		int icon_id = mInfo.getIconId();
		if (icon_id > 0) {
			mHolder.mImgIcon.setImageResource(icon_id);
		}
		String s;
		s = mInfo.getName();
		if (s != null && s.length() > 0) {
			mHolder.mTxtName.setText(s);
		} else {
			mHolder.mTxtName.setText("");
		}
		s = mInfo.getStateDes();
		String value = mInfo.getValue();

		if (position == 3 && value != null && value.length() > 0) {
			// 应众泰厂商要求将空调温度 去掉 2016-12-20
			// s = s + "/" + mInfo.getValue();
		}
		if (s != null && s.length() > 0) {
			mHolder.mTxtState.setText(s);
		} else {
			mHolder.mTxtState.setText("");
		}
		return convertView;
	}

	class Holder {
		private ImageView mImgIcon;// 功能图标

		private TextView mTxtName;// 功能名称

		private TextView mTxtState;// 功能状态

	}

}
