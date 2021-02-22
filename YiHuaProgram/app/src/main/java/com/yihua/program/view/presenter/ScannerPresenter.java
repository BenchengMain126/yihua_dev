package com.yihua.program.view.presenter;

import android.app.Activity;

import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.view.contract.ScannerContract;


public class ScannerPresenter extends BasePresenter<ScannerContract.IView> implements ScannerContract.IPresenter {

    public ScannerPresenter(Activity activity, ScannerContract.IView iv) {
        super(activity, iv);
    }

}
