/**
 * Project Name:Android_Car_Example
 * File Name:InputTipTask.java
 * Package Name:com.amap.api.car.example
 * Date:2015年4月7日上午10:42:41
 */

package com.carlt.yema.utils.map;

import android.content.Context;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlt.yema.ui.adapter.AddressListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:InputTipTask <br/>  
 * Function: 简单封装了Inputtips的搜索服务，将其余提示的adapter进行数据绑定  
 * Date:     2015年4月7日 上午10:42:41 <br/>  
 * @author yiyi.qi
 * @version
 * @since JDK 1.6
 * @see
 */
public class InputTipTask implements InputtipsListener {

    private static InputTipTask mInputTipTask;

    private Inputtips mInputTips;

    private AddressListAdapter mAdapter;

    public static InputTipTask getInstance(AddressListAdapter adapter) {
        if (mInputTipTask == null) {
            mInputTipTask = new InputTipTask();
        }
        //单例情况，多次进入DestinationActivity传进来的RecomandAdapter对象会不是同一个
        mInputTipTask.setRecommandAdapter(adapter);
        return mInputTipTask;
    }

    public void setRecommandAdapter(AddressListAdapter adapter) {
        mAdapter = adapter;
    }

    private InputTipTask() {


    }

    public void searchTips(Context context, String keyWord, String city) {

        InputtipsQuery query = new InputtipsQuery(keyWord, city);

        mInputTips = new Inputtips(context, query);
        mInputTips.setInputtipsListener(this);
        mInputTips.requestInputtipsAsyn();


    }

    @Override
    public void onGetInputtips(List<Tip> tips, int resultCode) {
    	//resultCode == AMapException.CODE_AMAP_SUCCESS && tips != null
        if (tips != null) {
            ArrayList<PositionEntity> positions = new ArrayList<PositionEntity>();
            for (Tip tip : tips) {

                if (tip.getPoint() != null) {

                    positions.add(new PositionEntity(tip.getPoint().getLatitude(), tip.getPoint().getLongitude(), tip.getName(), tip.getAdcode()));
                } else {
                    positions.add(new PositionEntity(0, 0, tip.getName(), tip.getAdcode()));
                }

            }
//            mAdapter.setmList(positions);
            mAdapter.notifyDataSetChanged();
        }
        //TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理
    }

}
  
