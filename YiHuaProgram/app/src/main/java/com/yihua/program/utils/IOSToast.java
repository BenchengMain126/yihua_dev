package com.yihua.program.utils;

import android.app.Activity;
import android.view.Gravity;

import com.yihua.program.R;
import com.yihua.program.widget.MProgressDialog;
import com.yihua.program.widget.toast.XToast;


/**
 * desc   : 仿 IOS 弹框
 */
public class IOSToast {

    private static final int TIME = 3000;

    public static void showSucceed(Activity activity, CharSequence text) {
        new XToast(activity)
                .setDuration(TIME)
                .setView(R.layout.toast_hint)
                .setAnimStyle(android.R.style.Animation_Toast)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                .setText(android.R.id.message, text)
                .show();
    }

    public static void showFail(Activity activity, CharSequence text) {
        new XToast(activity)
                .setDuration(TIME)
                .setView(R.layout.toast_hint)
                .setAnimStyle(android.R.style.Animation_Toast)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                .setText(android.R.id.message, text)
                .show();
    }

    public static void showWarn(Activity activity, CharSequence text) {
        new XToast(activity)
                .setDuration(TIME)
                .setView(R.layout.toast_hint)
                .setAnimStyle(android.R.style.Animation_Toast)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_warning)
                .setText(android.R.id.message, text)
                .show();
    }

    public static void show(Activity activity, CharSequence text) {
        new XToast(activity)
                .setDuration(TIME)
                .setView(R.layout.toast)
                .setAnimStyle(android.R.style.Animation_Toast)
                .setText(android.R.id.message, text)
                .setGravity(Gravity.BOTTOM)
                .setYOffset(100)
                .show();
    }


    private static MProgressDialog mMProgressDialog;

    public static void showLoading(Activity activity) {
        showLoading(activity, "");
    }

    public static void showLoading(Activity activity, CharSequence text) {
        if (mMProgressDialog == null) {
            mMProgressDialog = new MProgressDialog(activity);
        }
        mMProgressDialog.show();
    }

    public static void dismissLoading() {
        if (mMProgressDialog != null) {
            mMProgressDialog.dismiss();
            mMProgressDialog = null;
        }
    }


}