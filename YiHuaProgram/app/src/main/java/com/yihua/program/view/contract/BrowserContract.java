package com.yihua.program.view.contract;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.yihua.program.base.contract.IBasePresenter;
import com.yihua.program.base.contract.IBaseView;

/**
 * Created by yyj on 2018/09/10. email: 2209011667@qq.com
 */

public interface BrowserContract {

    interface IView extends IBaseView {

        void onError(String msg);
    }

    interface IPresenter extends IBasePresenter {

        void getAppVersionUsing();

    }


}
