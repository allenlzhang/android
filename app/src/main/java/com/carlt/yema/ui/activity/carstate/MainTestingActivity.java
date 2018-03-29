package com.carlt.yema.ui.activity.carstate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车况检测报告
 */
public class MainTestingActivity extends LoadingActivity{

    private GridView gridView;

    private List<Map<String,Object>> dataList;
    private int[] icon ={R.mipmap.abs,R.mipmap.tpms,R.mipmap.esp
            ,R.mipmap.engine,R.mipmap.epb,R.mipmap.svs,R.mipmap.srs,R.mipmap.engine,R.mipmap.eps};

    private int[] iconLight ={R.mipmap.abs_light,R.mipmap.tpms_light,R.mipmap.esp_light
            ,R.mipmap.engine_light,R.mipmap.epb_light,R.mipmap.svs_light,R.mipmap.srs_light,R.mipmap.engine_light,R.mipmap.eps_light};

    private String[] iconName={"ABS故障指示灯","TPMS故障指示灯","ESP故障指示灯","发动机冷却液温度故障指示灯","EPB故障指示灯"
            ,"发动机系统故障指示灯","SRS故障指示灯","发动机排放故障指示灯","EPS故障指示灯"};

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tain);
        initTitle("车况检测报告");
        initView();
    }

    private void initView() {
        gridView = $ViewByID(R.id.gridView);
        dataList=new ArrayList<>();
        adapter=new SimpleAdapter(this,getData(),R.layout.main_test_item,new String[]{"main_test_item_image","main_test_item_text"},
                new int[]{R.id.main_test_item_image,R.id.main_test_item_text});
        gridView.setAdapter(adapter);
    }
    private List<Map<String,Object>> getData(){
        for(int i=0;i<icon.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("main_test_item_image",icon[i]);
            map.put("main_test_item_text",iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

}
