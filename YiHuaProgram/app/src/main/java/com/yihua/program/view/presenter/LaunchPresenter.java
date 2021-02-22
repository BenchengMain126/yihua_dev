package com.yihua.program.view.presenter;

import android.app.Activity;
import android.os.Handler;

import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.tools.I;
import com.yihua.program.tools.P;
import com.yihua.program.view.contract.LaunchContract;

/**
 * Created by yyj on 2018/06/29. email: 2209011667@qq.com
 */

public class LaunchPresenter extends BasePresenter<LaunchContract.IView> implements LaunchContract.IPresenter {

    private Handler mHandler = new Handler();

    public LaunchPresenter(Activity activity, LaunchContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void goMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(P.getInstance(mActivity).isLogin()){
                    I.toBrowserActivity(mActivity);
                }else{
                    I.toLoginActivity(mActivity);
                }
            }
        }, 3000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
