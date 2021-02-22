package com.yihua.program.tools;

import android.app.Activity;
import android.content.Intent;

import com.yihua.program.application.App;
import com.yihua.program.utils.NetUtils;
import com.yihua.program.view.activity.BrowserActivity;
import com.yihua.program.view.activity.LoginActivity;
import com.yihua.program.view.activity.ResetPswActivity;
import com.yihua.program.view.activity.SmsLoginActivity;

/**
 * Created by yyj on 2018/07/09. email: 2209011667@qq.com
 * Intent 页面跳转的统一封装类。
 */

public class I {

    public static void toLoginActivity(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void toSmsLoginActivity(Activity context) {
        Intent intent = new Intent(context, SmsLoginActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void toResetPswActivity(Activity context) {
        Intent intent = new Intent(context, ResetPswActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void toBrowserActivity(Activity context) {
        if(!NetUtils.isConnectedAndToast(context)){
            App.clearAllStorage();
            toLoginActivity(context);
            return;
        }

        Intent intent = new Intent(context, BrowserActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
