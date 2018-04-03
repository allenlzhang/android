package com.carlt.yema.protocolparser.user;

import com.carlt.yema.data.set.AvatarInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.google.gson.JsonObject;

/**
 * Created by marller on 2018\4\3 0003.
 */

public class AvatarParser extends BaseParser {

    AvatarInfo mAvatarInfo=new AvatarInfo();

    public AvatarParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject data=mJson.get("data").getAsJsonObject();
        mAvatarInfo.setFileName(mJson.get("fileName").getAsString());
        mAvatarInfo.setFileExt(mJson.get("fileExt").getAsString());
        mAvatarInfo.setFilePath(mJson.get("filePath").getAsString());
        mAvatarInfo.setFileSize(mJson.get("fileSize").getAsString());
        mAvatarInfo.setFileOwner(mJson.get("fileOwner").getAsString());
        mAvatarInfo.setFileUid(mJson.get("fileUid").getAsString());
        mAvatarInfo.setId(mJson.get("id").getAsString());
        mAvatarInfo.setFileId(mJson.get("fileId").getAsString());
        mBaseResponseInfo.setValue(mAvatarInfo);
    }
    public static AvatarInfo parser(JsonObject json){
        AvatarInfo mAvatarInfo=new AvatarInfo();
        JsonObject data=json.get("data").getAsJsonObject();
        mAvatarInfo.setFileName(json.get("fileName").getAsString());
        mAvatarInfo.setFileExt(json.get("fileExt").getAsString());
        mAvatarInfo.setFilePath(json.get("filePath").getAsString());
        mAvatarInfo.setFileSize(json.get("fileSize").getAsString());
        mAvatarInfo.setFileOwner(json.get("fileOwner").getAsString());
        mAvatarInfo.setFileUid(json.get("fileUid").getAsString());
        mAvatarInfo.setId(json.get("id").getAsString());
        LoginInfo.setAvatar_id(json.get("id").getAsString());
        mAvatarInfo.setFileId(json.get("fileId").getAsString());
        LoginInfo.setAvatar_img(json.get("filePath").getAsString());
      return mAvatarInfo;
    }
}
