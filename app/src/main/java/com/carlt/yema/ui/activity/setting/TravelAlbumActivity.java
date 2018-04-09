package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.set.AlbumImageInfo;
import com.carlt.yema.protocolparser.AlbumImageParser;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.AlbumImageAdapter;
import com.carlt.yema.ui.adapter.AlbumImageAdapter.ViewHolder;
import com.carlt.yema.ui.view.UUToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TravelAlbumActivity extends LoadingActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG=TravelAlbumActivity.class.getName();

    private boolean idEditing = true;//是否为可编辑状态

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private AlbumImageAdapter adapter;//相册适配器

    private StringBuilder builder;//拼接请求参数

    private GridView album_list;//相册列表

    private View album_image_item_opt;//底部操作

    private TextView album_select_all;//全选按钮

    private TextView album_delete;//删除按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_album);
        initTitle("旅行相册");
        setBtnOptVisible(true);
        setBtnOptText("编辑");
        setOnBtnOptClickListener(this);
        initComponent();
    }

    private void initComponent() {
        album_list = $ViewByID(R.id.album_list);
        album_list.setOnItemClickListener(this);
        album_image_item_opt = $ViewByID(R.id.album_image_item_opt);
        album_select_all = $ViewByID(R.id.album_select_all);
        album_select_all.setOnClickListener(this);
        album_delete = $ViewByID(R.id.album_delete);
        album_delete.setOnClickListener(this);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }


    private void initData() {
        loadingDataUI();
        AlbumImageParser parser = new AlbumImageParser(callback);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_QUERY), params);
    }
    /**
     * 查询相册回调
     */
    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            builder = new StringBuilder();
            albumImageInfos = (ArrayList<AlbumImageInfo>) bInfo.getValue();
            adapter = new AlbumImageAdapter(TravelAlbumActivity.this, albumImageInfos);
            album_list.setAdapter(adapter);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadonErrorUI(bInfo);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpt:
                changeAlbumStatus();
                break;
            case R.id.album_select_all:
                selectAll();
                break;
            case R.id.album_delete:
                UUToast.showUUToast(this, "点击了删除");
                deleteImages();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (album_image_item_opt.getVisibility() == View.GONE) {
            AlbumImageInfo info = (AlbumImageInfo) adapterView.getItemAtPosition(i);
            Intent imageIntent = new Intent(this, PhotoDisplayActivity.class);
            imageIntent.putExtra("imagePath", info.getImagePath());
            imageIntent.putExtra("imageId", info.getId());
            imageIntent.putExtra("imageName", info.getUploadTime());
            startActivity(imageIntent);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.status.toggle();
            AlbumImageAdapter.getIsSelected().put(i, holder.status.isChecked());
        }
    }

    /*
    * 删除照片
    * */
    private void deleteImages() {
        DefaultStringParser parser = new DefaultStringParser(deleteCallback);
        HashMap<String, String> params = new HashMap<>();
        Set<Integer> idx = AlbumImageAdapter.getIsSelected().keySet();
        Iterator<Integer> iterator = idx.iterator();
        if (builder != null) {
            while (iterator.hasNext()) {
                if (AlbumImageAdapter.getIsSelected().get(iterator.next())) {
                    builder.append(iterator.next()).append(",");
                }
            }
        }
        String paramIdx = builder.substring(0, builder.length() - 1);
        params.put("id", paramIdx);
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
    }

    /**
     * 删除接口回调
     */
    private BaseParser.ResultCallback deleteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this, "相册删除成功");
            deleteSelectedImage();

        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this, "相册删除失败");
        }
    };

    private void changeAlbumStatus() {
        if (idEditing) {
            setBtnOptText(getResources().getString(R.string.travel_album_cancel));
            album_image_item_opt.setVisibility(View.VISIBLE);
            idEditing = false;
            if (null!=adapter) {
                adapter.setIsHide(false);
                adapter.notifyDataSetChanged();
            }
        } else {
            setBtnOptText(getResources().getString(R.string.travel_album_edit));
            idEditing = true;
            album_image_item_opt.setVisibility(View.GONE);
            cancelSelectall();
        }
    }

    /**
     * 全选
     */
    private void selectAll() {
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    AlbumImageAdapter.getIsSelected().put(i, true);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 全不选
     */
    private void cancelSelectall() {
        // 遍历list的长度，将已选的按钮设为未选
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    AlbumImageAdapter.getIsSelected().put(i, false);
                }
            }
            adapter.setIsHide(true);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除选中项
     */
    private void deleteSelectedImage() {
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    if (AlbumImageAdapter.getIsSelected().get(i)) {
                        albumImageInfos.remove(i);
                    }
                }
                cancelSelectall();
            }
            adapter.notifyDataSetChanged();
        }
    }

}
