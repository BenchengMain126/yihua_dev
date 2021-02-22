package com.yihua.program.view.contract;

import com.yihua.program.base.contract.IBasePresenter;
import com.yihua.program.base.contract.IBaseView;

import java.util.Map;

public interface ResetPswContract {

    interface IView extends IBaseView {
        void onError(String msg);

        void getVerificationCodeSuccess(String msg);

        void getVerificationCodeFailure(String msg);

        void forgetPasswordSuccess(String msg);

        void forgetPasswordFailure(String msg);
    }

    interface IPresenter extends IBasePresenter {

        void getVerificationCode(String phone);

        void forgetPassword(Map<String, String> params);
    }


}
