package com.carlt.yema.utils;

import com.carlt.yema.model.LoginInfo;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class CreateHashMap {

    private static HashMap create(String keys[], String values[]) {
        HashMap<String, String> params = new HashMap<>();
        String token= LoginInfo.getToken();
        ILog.e("info","token=="+token);
//        if(!TextUtils.isEmpty(token)){
//            params.put("token", LoginInfo.getToken());
//        }
        if(keys != null && values != null ){
            int length_key = keys.length;
            int length_value = values.length;
            if ( length_key > 0 && length_value > 0) {
                for (int i = 0; i < length_key; i++) {
                    params.put(keys[i], values[i]);
                }
            }
        }

        return params;
    }


    public static void addDefault(HashMap<String,String> param){
        HashMap<String,String> mp = create(new String[]{},new String[]{});
        if(param != null){
            param.putAll(mp);
        }else{
            param = mp;
        }
    }

}
