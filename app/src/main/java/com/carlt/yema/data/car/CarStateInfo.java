
package com.carlt.yema.data.car;


import com.carlt.yema.R;
import com.carlt.yema.data.BaseResponseInfo;

public class CarStateInfo extends BaseResponseInfo {
    public final static int[] iconId_opens = {
            R.mipmap.remote_unlock, R.mipmap.remote_door_open, R.mipmap.remote_window_open,R.mipmap.remote_engine_start,
            R.mipmap.remote_air_open, R.mipmap.remote_top_open
    };

    public final static int[] iconId_closes = {
            R.mipmap.remote_lock, R.mipmap.remote_door_close,R.mipmap.remote_window_close, R.mipmap.remote_engine_stop,
            R.mipmap.remote_air_colse,R.mipmap.remote_top_close
    };

    public final static String[] names = {
            "车锁", "车门","车窗","发动机", "空调", "天窗"
    };

    int iconId;// 状态iconid

    String name;// 状态名称

    String stateDes;// 状态描述

    String value;// 此状态下的相关数值
    
    String state;//后台给的原始状态

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateDes() {
        return stateDes;
    }

    public void setStateDes(String stateDes) {
        this.stateDes = stateDes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
