package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.car.SaftyMsgInfo;
import com.carlt.yema.utils.StringUtils;

import java.util.List;

/**
 * Created by Marlon on 2018/3/30.
 */

public class CarSaftyAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<SaftyMsgInfo> mList;

    public CarSaftyAdapter(Context mContext,List<SaftyMsgInfo> mList) {
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_car_safty_list,null);
            viewHolder.mImg = view.findViewById(R.id.item_car_safty_img);
            viewHolder.mTxtTitle = view.findViewById(R.id.item_car_safty_txt_title);
            viewHolder.mTxtMsg = view.findViewById(R.id.item_car_safty_txt_msg);
            viewHolder.mTxtDate = view.findViewById(R.id.item_car_safty_txt_date);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SaftyMsgInfo info = mList.get(i);
        if (!StringUtils.isEmail(info.getTitle())){
            viewHolder.mTxtTitle.setText(info.getTitle());
        }else {}
        if (info.getTitle().equals("防盗警报")){
            viewHolder.mImg.setImageResource(R.drawable.icon_security_warning);
        }else if (info.getTitle().equals("启动警报")){
            viewHolder.mImg.setImageResource(R.drawable.icon_start_the_alarm);
        }else if (info.getTitle().equals("碰撞警报")){
            viewHolder.mImg.setImageResource(R.drawable.icon_collision_alarm);
        }else if (info.getTitle().equals("胎压")){
            viewHolder.mImg.setImageResource(R.drawable.icon_tire_pressure);
        }else if (info.getTitle().equals("熄火")){
            viewHolder.mImg.setImageResource(R.drawable.icon_flameout);
        }else if (info.getTitle().equals("震动")){
            viewHolder.mImg.setImageResource(R.drawable.icon_shake);
        }else {}
        if (!StringUtils.isEmail(info.getContent())){
            viewHolder.mTxtMsg.setText(info.getContent());
        }else {}
        if (!StringUtils.isEmail(info.getCreatedate())){
            viewHolder.mTxtDate.setText(info.getCreatedate());
        }else {}
        return null;
    }

    class ViewHolder{
        ImageView mImg;
        TextView mTxtTitle;
        TextView mTxtMsg;
        TextView mTxtDate;
    }

}
