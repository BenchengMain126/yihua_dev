package com.yihua.program.base.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yihua.program.application.App;


public abstract class InitActivity extends AppCompatActivity {
    protected Context context;
    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        App.activities.add(this);

        initSome(savedInstanceState);
        initView();
        initData();
        initListener();
        initEvent();
    }

    protected void initSome(Bundle savedInstanceState) {

    }


    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected void initEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        activity = null;
    }
}
