package com.yihua.program.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.yihua.program.application.BaseApp;

/**
 * @创建者 蒋楚
 * @描述 跟网络相关的工具类
 */
public class NetUtils {

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断当前网络是否可以上网并吐司提醒
     */
    public static boolean isConnectedAndToast(Context context) {
        boolean flag = isNetworkAvailable(context);
        if (!flag) {
            Toast.makeText(BaseApp.getAppContext(), "请检查网络状态", Toast.LENGTH_LONG).show();
        }
        return flag;
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}  