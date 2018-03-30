package com.carlt.yema.data.car;

import com.carlt.yema.data.BaseResponseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 座驾页实时车况
 * Created by liu on 2018/3/30.
 */

public class CarNowStatusInfo extends BaseResponseInfo {
    private String isrunning;

    private ArrayList<CarNowStatusItemInfo> list;

    public String getIsrunning() {
        return isrunning;
    }

    public void setIsrunning(String isrunning) {
        this.isrunning = isrunning;
    }

    public ArrayList<CarNowStatusItemInfo> getList() {
        return list;
    }

    public void setList(ArrayList<CarNowStatusItemInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CarNowStatusInfo{" +
                "isrunning='" + isrunning + '\'' +
                ", list=" + list +
                '}';
    }
    /**
     * "name": "车辆状态",
     "value": "休眠",
     "company":""
     */
    public class CarNowStatusItemInfo{

        private String name;
        private String value;
        private String company;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        @Override
        public String toString() {
            return "CarNowStatusItemInfo{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", company='" + company + '\'' +
                    '}';
        }
    }
}
