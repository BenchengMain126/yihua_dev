package com.yihua.program.view.contract;

import com.yihua.program.base.contract.IBasePresenter;
import com.yihua.program.base.contract.IBaseView;

import java.util.Map;

/**
 * Created by yyj on 2018/09/10. email: 2209011667@qq.com
 */

public interface SmsLoginContract {

    interface IView extends IBaseView {

        void onError(String msg);

        void getVerificationCodeSuccess(String msg);

        void getVerificationCodeFailure(String msg);


        void loginOrRegisterSuccess(String msg);

        void loginOrRegisterFailure(String msg);


    }

    interface IPresenter extends IBasePresenter {


        void getVerificationCode(String phone);

        void loginOrRegisterByMobile(Map<String, String> params);

    }


}
