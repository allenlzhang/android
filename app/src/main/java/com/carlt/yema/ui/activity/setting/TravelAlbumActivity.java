package com.carlt.yema.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;

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

import static com.carlt.yema.R.layout.album_edit_dialog;

public class TravelAlbumActivity extends LoadingActivity implements View.OnClickListener {

    private boolean idEditing = true;

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private AlbumImageAdapter adapter;

    private StringBuilder builder;

    private GridView album_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_album);
        initTitle("旅行相册");
        setBtnOptVisible(true);
        setBtnOptText("编辑");
        setOnBtnOptClickListener(this);
        album_list=$ViewByID(R.id.album_list);
        initData();
    }

    private void initData() {
        loadingDataUI();
        AlbumImageParser parser=new AlbumImageParser(callback);
        HashMap<String,String> params=new HashMap<>();
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_QUERY),params);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    /**
     *
     * 查询相册回调
     *
     */
    private BaseParser.ResultCallback callback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            builder=new StringBuilder();
            albumImageInfos= (ArrayList<AlbumImageInfo>) bInfo.getValue();
            adapter=new AlbumImageAdapter(TravelAlbumActivity.this,albumImageInfos);
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

    private void deleteImages(){
        AlbumImageParser parser=new AlbumImageParser(deleteCallback);
        HashMap<String,String> params=new HashMap<>();
        Set<Integer> idx=AlbumImageAdapter.getIsSelected().keySet();
        Iterator<Integer> iterator=idx.iterator();
        if (builder!=null) {
            while (iterator.hasNext()) {
                if (AlbumImageAdapter.getIsSelected().get(iterator.next())) {
                    builder.append(iterator.next()).append(",");
                }
            }
        }
        String paramIdx=builder.substring(0,builder.length()-1);
        params.put("id",paramIdx);
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE),params);
    }

    /**
     *
     * 删除接口回调
     *
     */
    private BaseParser.ResultCallback deleteCallback=new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this,"相册删除成功");
            deleteSelectedImage();

        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this,"相册删除失败");
        }
    };

    private void changeAlbumStatus() {
        if (idEditing) {
            showDialog();
            setBtnOptText(getResources().getString(R.string.travel_album_cancel));
            idEditing = false;
        } else {
            showDialog();
            setBtnOptText(getResources().getString(R.string.travel_album_edit));
            idEditing = true;
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
    private void deleteSelectedImage(){
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
            RelativeLayout root = (RelativeLayout) LayoutInflater.from(this).inflate(
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("marller", "hasFocus---------------------->" + hasFocus);
        if (hasFocus) {
            if (idEditing) {
                setBtnOptText(getResources().getString(R.string.travel_album_edit));
            }
        }

    }
}
