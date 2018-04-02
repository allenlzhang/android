package com.carlt.yema.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.set.AlbumImageInfo;
import com.carlt.yema.protocolparser.AlbumImageParser;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.AlbumImageAdapter;
import com.carlt.yema.ui.view.UUToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TravelAlbumActivity extends LoadingActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

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
        initData();
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

    private void initData() {
        loadingDataUI();
        AlbumImageParser parser = new AlbumImageParser(callback);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_QUERY), params);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
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

    }

    private void deleteImages() {
        AlbumImageParser parser = new AlbumImageParser(deleteCallback);
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
//            showDialog();
            setBtnOptText(getResources().getString(R.string.travel_album_cancel));
            album_image_item_opt.setVisibility(View.VISIBLE);
            idEditing = false;
        } else {
//            showDialog();
            setBtnOptText(getResources().getString(R.string.travel_album_edit));
            idEditing = true;
            album_image_item_opt.setVisibility(View.GONE);
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
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void showDialog() {
        Dialog mAlbumDialog = null;
        Window dialogWindow = null;
        if (idEditing) {
            mAlbumDialog = new Dialog(this, R.style.BottomDialog);
            RelativeLayout root = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.
                    album_edit_dialog, null);
            //初始化视图
            root.findViewById(R.id.album_select_all).setOnClickListener(this);
            root.findViewById(R.id.album_delete).setOnClickListener(this);
            mAlbumDialog.setContentView(root);
            dialogWindow = mAlbumDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();

            lp.alpha = 0.95f; // 透明度
            dialogWindow.setAttributes(lp);
            mAlbumDialog.show();
        } else {
            if (null != mAlbumDialog) {
                dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                mAlbumDialog.dismiss();
                mAlbumDialog = null;

            }
        }

    }

}
