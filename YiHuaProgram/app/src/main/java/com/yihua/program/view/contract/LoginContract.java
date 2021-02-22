package com.yihua.program.view.contract;

import android.graphics.Bitmap;

import com.yihua.program.base.contract.IBasePresenter;
import com.yihua.program.base.contract.IBaseView;

import java.util.Map;

public interface LoginContract {

    interface IView extends IBaseView {
        void onError(String msg);

        void createImageCodeSuccess(Bitmap bitmap);

        void loginSuccess(String msg);

        void loginFailure(String msg);

        void loginShowVerifyFailure(String msg);
    }

    interface IPresenter extends IBasePresenter {
        void CheckPermission();

        void createImageCode(String phone);

        void login( Map<String, String> params);
    }


}
