package com.carlt.yema.ui.activity.setting;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.carlt.yema.ui.adapter.FileImageAdapter;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.LocalConfig;
import com.carlt.yema.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TravelAlbumActivity extends LoadingActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = TravelAlbumActivity.class.getName();

    private boolean idEditing = true;//是否为可编辑状态

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private AlbumImageAdapter adapter;//相册适配器

    private FileImageAdapter imageAdapter;

    private List<File> imageList;

    private GridView album_list;//相册列表

    private View album_image_item_opt;//底部操作

    private TextView album_select_all;//全选按钮

    private TextView album_delete;//删除按钮

    private boolean isSelectAll;

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
            albumImageInfos = (ArrayList<AlbumImageInfo>) bInfo.getValue();
            if (albumImageInfos != null && albumImageInfos.size() > 0) {
                adapter = new AlbumImageAdapter(TravelAlbumActivity.this, albumImageInfos);
                album_list.setAdapter(adapter);
            } else {
                initLocalImages(bInfo);
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            initLocalImages(bInfo);
        }
    };

    private void initLocalImages(final BaseResponseInfo bInfo) {
        requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
            @Override
            public void granted() {
                File travelDir = new File(LocalConfig.mTravelImageCacheSavePath_SD);
                File[] travelImages = travelDir.listFiles();
                if (travelImages != null && travelImages.length > 0) {
                    imageList = Arrays.asList(travelImages);
                    if (travelImages != null && travelImages.length > 0) {
                        loadSuccessUI();
                        imageAdapter = new FileImageAdapter(TravelAlbumActivity.this, imageList);
                        album_list.setAdapter(imageAdapter);
                    } else {
                        bInfo.setInfo("暂未拉取到图片");
                        loadonErrorUI(bInfo);
                    }
                } else {
                    bInfo.setInfo("暂未拉取到图片");
                    loadonErrorUI(bInfo);
                }
            }

            @Override
            public void denied() {
                UUToast.showUUToast(TravelAlbumActivity.this, "部分权限获取失败，正常功能受到影响");
                bInfo.setInfo("暂未拉取到图片");
                loadonErrorUI(bInfo);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpt:
                changeAlbumStatus();
                break;
            case R.id.album_select_all:
                isSelectAll = true;
                if (albumImageInfos != null && albumImageInfos.size() > 0) {
                    selectAll();
                }
                break;
            case R.id.album_delete:
                if (isSelectAll) {
                    PopBoxCreat.createDialogWithTitle(this, "温馨提示", "确认是否删除所选内容", null, "取消", "删除", new PopBoxCreat.DialogWithTitleClick() {
                        @Override
                        public void onLeftClick() {
                            idEditing = false;
                            changeAlbumStatus();
                        }

                        @Override
                        public void onRightClick() {
                            if (albumImageInfos != null && albumImageInfos.size() > 0) {
                                deleteAllImages();
                            } else {
                                deleteAllImageFiles();
                            }
                        }
                    });
                } else {
                    if (albumImageInfos != null && albumImageInfos.size() > 0) {
                        deleteImages();
                    } else {
                        deleteImageFiles();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
        if (album_image_item_opt.getVisibility() == View.GONE) {
            if (albumImageInfos != null && albumImageInfos.size() > 0) {
                final AlbumImageInfo info = (AlbumImageInfo) adapterView.getItemAtPosition(i);
                requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        new Thread() {
                            @Override
                            public void run() {
                                savePicture(info);
                                super.run();
                            }
                        }.start();
                        Intent imageIntent = new Intent(TravelAlbumActivity.this, PhotoDisplayActivity.class);
                        imageIntent.putExtra("imagePath", info.getImagePath());
                        imageIntent.putExtra("imageId", info.getId());
                        imageIntent.putExtra("imageName", info.getUploadTime());
                        startActivityForResult(imageIntent, 0);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(TravelAlbumActivity.this, "部分权限获取失败，正常功能受到影响");
                        Intent imageIntent = new Intent(TravelAlbumActivity.this, PhotoDisplayActivity.class);
                        imageIntent.putExtra("imagePath", info.getImagePath());
                        imageIntent.putExtra("imageId", info.getId());
                        imageIntent.putExtra("imageName", info.getUploadTime());
                        startActivityForResult(imageIntent, 0);
                    }
                });
            } else {
                final File imageFile = (File) adapterView.getItemAtPosition(i);
                Intent imageIntent = new Intent(TravelAlbumActivity.this, PhotoDisplayActivity.class);
                imageIntent.putExtra("imagePath", imageFile.getAbsolutePath());
                startActivityForResult(imageIntent, 0);
            }
        } else {
            if (albumImageInfos != null && albumImageInfos.size() > 0) {
                if (!adapter.isIsHide()) {
                    AlbumImageAdapter.ViewHolder holder = (AlbumImageAdapter.ViewHolder) view.getTag();
                    holder.status.toggle();
                    AlbumImageAdapter.getIsSelected().put(i, holder.status.isChecked());
                    if (!holder.status.isChecked()) {
                        isSelectAll = false;
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (!imageAdapter.isIsHide()) {
                    FileImageAdapter.ViewHolder holder = (FileImageAdapter.ViewHolder) view.getTag();
                    holder.status.toggle();
                    FileImageAdapter.getIsSelected().put(i, holder.status.isChecked());
                    if (!holder.status.isChecked()) {
                        isSelectAll = false;
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /*
        * 删除照片
        * */
    private void deleteImages() {
        DefaultStringParser parser = new DefaultStringParser(deleteCallback);
        HashMap<String, String> params = new HashMap<>();
        if (AlbumImageAdapter.getIsSelected() != null) {
            Set<Integer> idx = AlbumImageAdapter.getIsSelected().keySet();
            Log.e(TAG, "Hash大小" + idx.size());
            Log.e(TAG, "albumImageInfos" + albumImageInfos.size());
            Iterator<Integer> iterator = idx.iterator();
            File travelDir = new File(LocalConfig.mTravelImageCacheSavePath_SD);
            File[] travelImages = travelDir.listFiles();
            if (travelImages != null && travelImages.length > 0) {
                imageList = Arrays.asList(travelImages);
            }
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                int selectIndex = iterator.next();
                if (AlbumImageAdapter.getIsSelected().get(selectIndex)) {
                    int imageId = albumImageInfos.get(selectIndex).getId();
                    builder.append(imageId).append(",");
//                    albumImageInfos.remove(selectIndex);
                    if (imageList != null && imageList.size() > 0) {
                        for (int i = 0; i < imageList.size(); i++) {
                            File imageFile = imageList.get(i);
                            if (imageFile.getAbsoluteFile().toString().contains(String.valueOf(imageId))) {
                                imageFile.delete();
                            }
                        }
                    }
                }
            }
            if (builder.length() <= 0) {
                UUToast.showUUToast(this, "您还没有选择照片");
                return;
            }
            String paramIdx = builder.substring(0, builder.length() - 1);
            params.put("id", paramIdx);
            parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
        }

    }


    /**
     * 删除照片
     */
    private void deleteImageFiles() {

        if (FileImageAdapter.getIsSelected() != null) {
            Set<Integer> idx = FileImageAdapter.getIsSelected().keySet();
            Iterator<Integer> iterator = idx.iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                int selectIndex = iterator.next();
                if (FileImageAdapter.getIsSelected().get(selectIndex)) {
                    if (imageList != null && imageList.size() > 0) {
                        File imageFile = imageList.get(selectIndex);
                        if (imageFile != null) {
                            imageFile.delete();
                        }
                        builder.append(imageFile.getAbsoluteFile()).append(",");
                    }
                }
            }
            initData();
            changeAlbumStatus();
            if (builder.length() <= 0) {
                UUToast.showUUToast(this, "您还没有选择照片");
                return;
            }

        }

    }

    /*
    * 删除所有照片
    * */
    private void deleteAllImages() {
        DefaultStringParser parser = new DefaultStringParser(deleteCallback);
        HashMap<String, String> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (albumImageInfos != null && albumImageInfos.size() > 0) {
            for (int i = 0; i < albumImageInfos.size(); i++) {
                AlbumImageInfo albumImageInfo = albumImageInfos.get(i);
                builder.append(albumImageInfo.getId()).append(",");
            }
            albumImageInfos.removeAll(albumImageInfos);
        }
        File travelDir = new File(LocalConfig.mTravelImageCacheSavePath_SD);
        File[] travelImages = travelDir.listFiles();
        if (travelImages != null && travelImages.length > 0) {
            imageList = Arrays.asList(travelImages);
            if (imageList != null && imageList.size() > 0) {
                for (int i = 0; i < imageList.size(); i++) {
                    File imageFile = imageList.get(i);
                    imageFile.delete();
                }
            }
        }
        String paramIdx = builder.substring(0, builder.length() - 1);
        params.put("id", paramIdx);
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
    }

    /*
    * 删除所有照片
    * */
    private void deleteAllImageFiles() {
        File travelDir = new File(LocalConfig.mTravelImageCacheSavePath_SD);
        File[] travelImages = travelDir.listFiles();
        if (travelImages != null && travelImages.length > 0) {
            imageList = Arrays.asList(travelImages);
            if (imageList != null && imageList.size() > 0) {
                for (int i = 0; i < imageList.size(); i++) {
                    File imageFile = imageList.get(i);
                    imageFile.delete();
                }
                changeAlbumStatus();
            }
        }

    }

    /**
     * 删除接口回调
     */
    private BaseParser.ResultCallback deleteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this, "相册删除成功");
            initData();
            idEditing = false;
            changeAlbumStatus();
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
            isSelectAll = false;
            if (albumImageInfos != null && albumImageInfos.size() > 0) {
                adapter.setIsHide(false);
                adapter.notifyDataSetChanged();
            } else {
                imageAdapter.setIsHide(false);
                imageAdapter.notifyDataSetChanged();
            }
        } else {
            setBtnOptText(getResources().getString(R.string.travel_album_edit));
            idEditing = true;
            album_image_item_opt.setVisibility(View.GONE);
            cancelSelectall();
            cancelSelectallFiles();
        }
    }

    /**
     * 全选
     */
    private void selectAll() {
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                adapter.setAlbumImageInfos(albumImageInfos);
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    AlbumImageAdapter.getIsSelected().put(i, true);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 全选
     */
    private void selectAllFile() {
        if (imageAdapter != null) {
            if (null != imageList && imageList.size() > 0) {
                imageAdapter.setAlbumImageInfos(imageList);
                for (int i = 0; i < imageList.size(); i++) {
                    FileImageAdapter.getIsSelected().put(i, true);
                }
            }
            imageAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 全不选
     */
    private void cancelSelectall() {
        // 遍历list的长度，将已选的按钮设为未选
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                adapter.setAlbumImageInfos(albumImageInfos);
                adapter.checkedInit();
                adapter.setIsHide(true);
                adapter.notifyDataSetChanged();
            }
        }
        if (imageAdapter != null) {
            if (imageList != null && imageList.size() > 0) {
                imageAdapter.setAlbumImageInfos(imageList);
                imageAdapter.checkedInit();
                imageAdapter.setIsHide(true);
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 全不选
     */
    private void cancelSelectallFiles() {
        // 遍历list的长度，将已选的按钮设为未选
        if (imageAdapter != null) {
            if (null != imageList && imageList.size() > 0) {
                for (int i = 0; i < imageList.size(); i++) {
                    FileImageAdapter.getIsSelected().put(i, false);
                }
            }
            imageAdapter.setIsHide(true);
            imageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除选中项
     */
    private void deleteSelectedImage() {
        if (adapter != null) {
            AlbumImageAdapter.getIsSelected().clear();
            adapter.setAlbumImageInfos(albumImageInfos);
            adapter.notifyDataSetChanged();
            cancelSelectall();
        }
    }

    public void savePicture(AlbumImageInfo info) {
        Log.d(TAG, "savePicture------->" + info.getImagePath());
        try {
            URL pictureUrl = new URL(info.getImagePath());
            InputStream in = pictureUrl.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();
            String pictureName = LocalConfig.mTravelImageCacheSavePath_SD + "travel_" + info.getId() + ".png";
            File file = new File(pictureName);
            FileOutputStream out;

            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
