package com.hz17car.yema.data;

import com.google.gson.annotations.SerializedName;

/**
 * 所有Http请求返回数据的基类
 */
public class BaseResponseInfo<T> {

    public final static int SUCCESS = 200;

    public final static int ERRO = 0;
    public final static String NET_ERROR = "网络异常";

    public final static int TOKEN_EMPTY = 1001;//token为空
    public final static int TOKEN_EXPIRES = 1002;//无效的access token
    public final static int TOKEN_RESET = 1003;//用户token重置被踢
    public final static int PRARM_ERROR = 1004;//参数错误
    public final static int DATA_NONEXISTENT = 1005;//该条数据已经不存在


    @SerializedName("code")
    private int flag;

    @SerializedName("msg")
    private String info;

    public int getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = Integer.parseInt(flag);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @SerializedName("data")
    private T mValue;

    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        this.mValue = value;
    }

    public boolean isSuccessful() {
        return flag == SUCCESS;
    }


    @Override
    public String toString() {
        return "BaseResponseInfo{" +
                "flag=" + flag +
                ", info='" + info + '\'' +
                ", mValue=" + mValue +
                '}';
    }
}
