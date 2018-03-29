
package com.carlt.yema.protocolparser.car;


import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.MyParse;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 针对获取车款列表解析(三级)
 * 
 * @author daisy
 */
public class CarModeInfoListV3Parser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public CarModeInfoListV3Parser(ResultCallback callback) {
        super(callback);
    }

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            JSONObject jsonObject=new JSONObject(mJSON_data.toString());
            JSONArray mNames = jsonObject.names();
            int length = mNames.length();
            for (int i = 0; i < length; i++) {
                for (int j = i + 1; j < length; j++) {
                    int a = MyParse.parseInt(mNames.optString(i));
                    int b = MyParse.parseInt(mNames.optString(j));
                    if (a < b) {
                        int temp = a;
                        a = b;
                        b = temp;

                        mNames.put(i, a);
                        mNames.put(j, b);
                    }

                }

            }
            for (int i = 0; i < length; i++) {
                String name = mNames.optString(i);
                CarModeInfo mInfoSub = new CarModeInfo();
                mInfoSub.setTitleSub(true);
                mInfoSub.setTitle(name);
                mInfoSub.setYear(name);
                mInfoSub.setType(CarModeInfo.TYPE_THIRD);
                mCarModeInfoList.add(mInfoSub);
                JSONArray mJSON_infos = jsonObject.getJSONArray(name);
                for (int j = 0; j < mJSON_infos.length(); j++) {
                    JSONObject mJSON_info = (JSONObject)mJSON_infos.opt(j);
                    CarModeInfo mInfo = new CarModeInfo();
                    mInfo.setId(mJSON_info.optString("id"));
                    mInfo.setTitle(mJSON_info.optString("title"));
                    mInfo.setTitleSub(false);
                    mInfo.setType(CarModeInfo.TYPE_THIRD);
                    mInfo.setYear(name);
                    mCarModeInfoList.add(mInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
