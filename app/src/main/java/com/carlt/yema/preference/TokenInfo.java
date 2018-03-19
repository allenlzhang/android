
package com.carlt.yema.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.carlt.yema.YemaApplication;


/**
 * 用户token信息
 * 
 * @author Administrator
 */
public class TokenInfo {
    private final static String TOKEN = "token";

    private static String token = "";

    private final static String VALUE = "value";

    public static String getToken() {
        readConfig();
        return token;
    }

    public static void setToken(String token) {
        TokenInfo.token = token;
        saveConfig(token);
    }

    private static boolean readConfig() {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(TOKEN,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                String mToken = sp.getString(VALUE, "");
                token = mToken;
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig(String value_token) {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(TOKEN,
                Context.MODE_PRIVATE);
        if (null != sp) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                editor.putString(VALUE, value_token);
                editor.apply();
                return true;
            }
        }
        return false;
    }
}
