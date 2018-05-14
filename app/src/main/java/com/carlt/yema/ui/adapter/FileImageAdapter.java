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

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class FileImageAdapter extends BaseAdapter {

    private static final String TAG = FileImageAdapter.class.getName();

    private LayoutInflater inflater;

    private Context context;

    private List<File> imagePaths;

    private static HashMap<Integer, Boolean> isSelected;

    private boolean isHide = true;


    public FileImageAdapter(Context context, List<File> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        isSelected = new HashMap<>();
        inflater = LayoutInflater.from(context);
        checkedInit();
    }

    public void checkedInit() {
        for (int i = 0; i < imagePaths.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public boolean isIsHide() {
        return isHide;
    }

    public List<File> getAlbumImageInfos() {
        return imagePaths;
    }

    public void setAlbumImageInfos(List<File> imagePaths) {
        this.imagePaths = imagePaths;
    }

    /**
     * 设置CheckBox是否全部隐藏
     */
    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    @Override
    public int getCount() {
        return imagePaths == null ? 0 : imagePaths.size();
    }

    @Override
    public File getItem(int i) {
        return imagePaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        File imageFile = getItem(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.album_image_list_item, null);
            viewHolder.status = view.findViewById(R.id.album_image_check);
            viewHolder.image = view.findViewById(R.id.album_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.status.setChecked(getIsSelected().get(i));
        if (isHide) {
            viewHolder.status.setVisibility(View.GONE);
        } else {
            viewHolder.status.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(imageFile).into(viewHolder.image);
        return view;
    }

    public static class ViewHolder {
        public  CheckBox status;
        public  ImageView image;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        FileImageAdapter.isSelected = isSelected;
    }

}
