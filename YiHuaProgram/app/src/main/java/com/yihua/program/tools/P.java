package com.yihua.program.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharedPreferences
 */

public class P {
    private static final String SP_NAME = "common";
    private static P mSpUtils;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

//    TODO 上线关闭
//    private String BASE_URL_STR = "http://www.bazhuawang.com:8888/";  //产线
//    private String BASE_URL_STR = "http://121.15.166.3:8888/";         //测试
    private String BASE_URL_STR = "http://121.15.166.3:9000/";      //测试

    private P(Context context) {
        this.context = context;
        sp = this.context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static P getInstance(Context context) {
        if (mSpUtils == null) {
            synchronized (P.class) {
                if (mSpUtils == null) {
                    mSpUtils = new P(context);
                    return mSpUtils;
                }
            }
        }
        return mSpUtils;
    }


    /**
     * baseUrl
     */
    public void putBaseUrl(String baseUrl) {
        editor.putString("baseUrl", baseUrl);
        editor.commit();
    }

    public String getBaseUrl() {
        return sp.getString("baseUrl", BASE_URL_STR);
    }


    /**
     * account
     */
    public void putAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public String getAccount() {
        return sp.getString("account", null);
    }


    public String getH5Url() {
        return "file:///android_asset/TestH5/index.html";
    }


    /**
     * isLogin
     */
    public boolean isLogin() {
        return sp.getBoolean("isLogin", false);
    }

    public void putLogin(boolean value) {
        editor.putBoolean("isLogin", value);
        editor.commit();
    }


    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public void remove(String key) {
        sp.edit().remove(key).commit();
    }

}
