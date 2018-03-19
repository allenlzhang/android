package com.carlt.yema.protocolparser;

import com.google.gson.JsonObject;


public class DefaultParser extends BaseParser {
    public DefaultParser(ResultCallback callback) {
        super(callback);
    }

    private static JsonObject mJSON_data;

    @Override
    protected void parser() throws Exception {
        mJSON_data = mJson.get("data").getAsJsonObject();
    }

    public static String getStringValue(String key) {
        String value = "";
        if(mJSON_data!=null){
            value = mJSON_data.get("key").getAsString();
        }
        return value;
    }
}
