package com.yihua.program.base.contract;


import com.yihua.program.base.activity.inter.IContext;
import com.yihua.program.base.activity.inter.ILifeCycle;

/**
 * Created by yyj on 2018/07/09. email: 2209011667@qq.com
 */

public interface IBasePresenter extends IContext, ILifeCycle {

    void finish();

    void log(String s);
}
