package com.carlt.yema.protocolparser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class DefaultParser<T> extends BaseParser<T> {

    private static JsonObject mJSON_data;

    public DefaultParser(ResultCallback callback, Class clazz) {
        super(callback, clazz);
    }

    @Override
    protected void parser() throws Exception {
            mJSON_data = mJson.get("data").getAsJsonObject();
            T t = new Gson().fromJson(mJSON_data, clazz);
            mBaseResponseInfo.setValue(t);

    }

}
