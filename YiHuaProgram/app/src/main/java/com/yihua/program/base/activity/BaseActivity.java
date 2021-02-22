package com.yihua.program.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yihua.program.base.contract.IBasePresenter;
import com.yihua.program.base.contract.IBaseView;
import com.yihua.program.utils.StatusBarUtil;
import com.yihua.program.widget.MProgressDialog;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends IBasePresenter> extends InitActivity implements IBaseView {

    protected P mPresenter;

    protected InputMethodManager mInputMethodManager;

    @Override
    protected void initSome(Bundle savedInstanceState) {
        initPresenter();
        checkPresenterIsNull();
        super.initSome(savedInstanceState);
        StatusBarUtil.initStatusBar(activity,true,true,true);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    protected abstract void initPresenter();


    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    protected abstract int getLayoutId();

    @Override
    protected void initView() {
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    protected void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        if (inputMethodManager == null) return;
        if (view != null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private MProgressDialog mMProgressDialog;

    public void showProgressDialog() {
        mMProgressDialog = new MProgressDialog(this);
        mMProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mMProgressDialog != null) {
            mMProgressDialog.dismiss();
            mMProgressDialog = null;
        }
    }
}
