package com.yihua.program.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDexApplication;

import com.yihua.program.dbmodel.SaveLocal;
import com.yihua.program.tools.L;
import com.yihua.program.tools.P;
import com.yihua.program.view.activity.BrowserActivity;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @描述 Application基类
 */
public class BaseApp extends MultiDexApplication {

    public static List<Activity> activities = new LinkedList<>();
    private static Context mContext;//上下文

    @Override
    public void onCreate() {
        super.onCreate();
        //对全局属性赋值
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }


    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static String getVersionName() {
        String versionCode = "";
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = "";
        }
        return versionCode;
    }


    /**
     * 获取当前进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 完全退出
     * 一般用于“退出程序”功能
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 重启当前应用
     */
    public static void reLaunchApp() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 清除所有并重启应用
     */
    public static void clearAllStorageAndRelaunch() {
        //清除未读信息角标
        ShortcutBadger.removeCount(getAppContext());
        P.getInstance(getAppContext()).putLogin(false);
        //清除本地数据
        LitePal.deleteAll(SaveLocal.class);
        //重启当前应用
        reLaunchApp();
    }

    /**
     * 清除所有
     */
    public static void clearAllStorage() {
        //清除未读信息角标
        ShortcutBadger.removeCount(getAppContext());
        P.getInstance(getAppContext()).putLogin(false);
        //清除本地数据
        LitePal.deleteAll(SaveLocal.class);
    }

    /**
     * 添加白名单实现保活
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Activity context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        String logSt = isIgnoring ? "has Ignored" : "no Ignored";
        L.e("ignoreBattery = " + logSt);
        L.e("isIgnoring = " + isIgnoring);
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse(context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
