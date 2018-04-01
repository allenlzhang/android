package com.carlt.yema.data.set;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class AlbumImageInfo extends BaseResponseInfo {

    private int id;
    private String imagePath;
    private String thumbnailPath;
    private String uploadTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
