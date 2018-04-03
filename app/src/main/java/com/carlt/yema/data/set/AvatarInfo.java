package com.carlt.yema.data.set;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by marller on 2018\4\3 0003.
 */

public class AvatarInfo extends BaseResponseInfo {
    private String fileName;//文件名
    private String fileExt;//文件后缀
    private String filePath;//图片路径
    private String fileSize;//图片大小
    private String fileOwner;//图片类型(avatar:头像)
    private String fileDownCount;//文件下载次数
    private String fileUid;//文件所属UID
    private String id;//图片ID
    private String fileId;//文件id

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(String fileOwner) {
        this.fileOwner = fileOwner;
    }

    public String getFileDownCount() {
        return fileDownCount;
    }

    public void setFileDownCount(String fileDownCount) {
        this.fileDownCount = fileDownCount;
    }

    public String getFileUid() {
        return fileUid;
    }

    public void setFileUid(String fileUid) {
        this.fileUid = fileUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
