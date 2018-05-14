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
        mBaseResponseInfo.setValue(mAvatarInfo);
    }
    public static AvatarInfo parser(JsonObject json){
        AvatarInfo mAvatarInfo=new AvatarInfo();
        JsonObject data=json.get("data").getAsJsonObject();
        mAvatarInfo.setFileName(data.get("fileName").getAsString());
        mAvatarInfo.setFileExt(data.get("fileExt").getAsString());
        mAvatarInfo.setFilePath(data.get("filePath").getAsString());
        mAvatarInfo.setFileSize(data.get("fileSize").getAsString());
        mAvatarInfo.setFileOwner(data.get("fileOwner").getAsString());
        mAvatarInfo.setFileUid(data.get("fileUid").getAsString());
        mAvatarInfo.setId(data.get("id").getAsString());
        LoginInfo.setAvatar_id(data.get("id").getAsString());
        mAvatarInfo.setFileTime(data.get("fileTime").getAsLong());
        LoginInfo.setAvatar_img(data.get("filePath").getAsString());
      return mAvatarInfo;
    }
}
