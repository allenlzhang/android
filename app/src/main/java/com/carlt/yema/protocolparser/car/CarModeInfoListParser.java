
package com.carlt.yema.protocolparser.car;


import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * 拉取车型列表（一级）
 * @author Administrator
 *
 */
public class CarModeInfoListParser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public CarModeInfoListParser(ResultCallback callback) {
        super(callback);
    }

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {
            JsonArray mJSON_data = mJson.getAsJsonArray("data");
            for (int i = 0; i < mJSON_data.size(); i++) {
                CarModeInfo mCarModeInfo = new CarModeInfo();
                JsonObject temp = (JsonObject)mJSON_data.get(i);
                mCarModeInfo.setId(temp.get("id").getAsString());
                mCarModeInfo.setPid(temp.get("pid").getAsString());
                mCarModeInfo.setTitle(temp.get("title").getAsString());
                mCarModeInfo.setTitle_py(temp.get("title_py").getAsString());
                mCarModeInfo.setCarlogo(temp.get("carlogo").getAsString());
                mCarModeInfo.setType(CarModeInfo.TYPE_FIRST);
                mCarModeInfoList.add(mCarModeInfo);
            }
    }
    //
    // public BaseResponseInfo getBaseResponseInfo(String url, String post) {
    // try {
    // InputStream in = CPApplication.ApplicationContext.getAssets().open(
    // "json_violation.txt");
    // mJson = new JSONObject(FileUtil.ToString(in));
    // Log.e("info", "Http响应--" + mJson);
    // mBaseResponseInfo.setFlag(mJson.getString("code"));
    // mBaseResponseInfo.setInfo(mJson.getString("msg"));
    // } catch (Exception e) {
    // Log.e("info", "BaseParser--e==" + e);
    // }
    // if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
    // parser();
    // }
    // return mBaseResponseInfo;
    //
    // }
}
