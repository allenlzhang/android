package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carlt.yema.R;
import com.carlt.yema.data.set.AlbumImageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class AlbumImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private static HashMap<Integer,Boolean> isSelected;

    public AlbumImageAdapter(Context context, ArrayList<AlbumImageInfo> albumImageInfos) {
        this.context = context;
        this.albumImageInfos = albumImageInfos;
        inflater=LayoutInflater.from(context);
        //按时间排序
        Collections.sort(this.albumImageInfos, new Comparator<AlbumImageInfo>() {
            @Override
            public int compare(AlbumImageInfo albumImageInfo, AlbumImageInfo albumImageInfo1) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1=null,date2=null;
                try {
                    date1=simpleDateFormat.parse(albumImageInfo.getUploadTime());
                    date2=simpleDateFormat.parse(albumImageInfo1.getUploadTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date2.compareTo(date1);
            }
        });
    }

    @Override
    public int getCount() {
        return albumImageInfos==null?0:albumImageInfos.size();
    }

    @Override
    public AlbumImageInfo getItem(int i) {
        return albumImageInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        AlbumImageInfo mAlbumImageInfo=getItem(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.album_image_list_item, null);
            viewHolder.status = view.findViewById(R.id.album_image_check);
            viewHolder.image = view.findViewById(R.id.album_image);
            view.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.status.setChecked(getIsSelected().get(i));
        Glide.with(context).load(mAlbumImageInfo.getImagePath()).into(viewHolder.image);
        return view;
    }

    private static class ViewHolder{
        CheckBox status;
        ImageView image;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        AlbumImageAdapter.isSelected = isSelected;
    }
}
