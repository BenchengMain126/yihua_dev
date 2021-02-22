package com.yihua.program.view.activity;

import android.os.Bundle;

import com.yihua.program.R;
import com.yihua.program.base.activity.BaseActivity;
import com.yihua.program.utils.StatusBarUtil;
import com.yihua.program.view.contract.LaunchContract;
import com.yihua.program.view.presenter.LaunchPresenter;

public class LaunchActivity extends BaseActivity<LaunchContract.IPresenter> implements LaunchContract.IView {

    @Override
    protected void initPresenter() {
        mPresenter = new LaunchPresenter(activity, this);
    }

    @Override
    protected void initSome(Bundle savedInstanceState) {
        super.initSome(savedInstanceState);
        StatusBarUtil.initStatusBar(activity, true, true, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mPresenter.goMain();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
