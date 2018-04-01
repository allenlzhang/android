package com.carlt.yema.protocolparser;

import com.carlt.yema.data.set.AlbumImageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class AlbumImageParser extends BaseParser {

    private ArrayList<AlbumImageInfo> albumImageInfos;

    public AlbumImageParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<AlbumImageInfo>>(){}.getType();
        albumImageInfos=gson.fromJson(mJson.get("data").getAsString(),type);
        mBaseResponseInfo.setValue(albumImageInfos);
    }
}
