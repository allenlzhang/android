
package com.carlt.yema.protocolparser.car;

import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * 针对获取车系列表解析(二级)
 * 
 * @author daisy
 */
public class CarModeInfoListV2Parser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public CarModeInfoListV2Parser(ResultCallback callback) {
        super(callback);
    }

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {

            JsonObject mData = (JsonObject)mJson.getAsJsonObject("data");
            CarModeInfo mInfoSub = new CarModeInfo();
            mInfoSub.setTitleSub(true);
            mInfoSub.setId(mData.get("id").getAsString());
            mInfoSub.setTitle(mData.get("title").getAsString());
            mInfoSub.setType(CarModeInfo.TYPE_SECOND);
            mCarModeInfoList.add(mInfoSub);

            JsonArray mList = mData.getAsJsonArray("data");
            if (mList != null) {
                int len = mList.size();
                for (int j = 0; j < len; j++) {
                    JsonObject mDataChild = (JsonObject)mList.get(j);
                    CarModeInfo mInfoChild = new CarModeInfo();
                    mInfoChild.setId(mDataChild.get("id").getAsString());
                    mInfoChild.setTitle(mDataChild.get("title").getAsString());
                    mInfoChild.setType(CarModeInfo.TYPE_SECOND);
                    mCarModeInfoList.add(mInfoChild);
                }
            }

    }
}
