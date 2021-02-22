package com.yihua.program.view.presenter;

import android.app.Activity;

import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.yihua.program.api.ApiHelper;
import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.bean.CommonResponse;
import com.yihua.program.utils.MD5Util;
import com.yihua.program.view.contract.ResetPswContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ResetPwsPresenter extends BasePresenter<ResetPswContract.IView> implements ResetPswContract.IPresenter {

    public ResetPwsPresenter(Activity activity, ResetPswContract.IView iv) {
        super(activity, iv);
    }


    @Override
    public void getVerificationCode(String phone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String checkSign = MD5Util.stringToMD5(phone + time);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("checkSign", checkSign);

        showProgressDialog();
        ApiHelper.getYiHuaApi()
                .getVerificationCode(params)
                .compose(Transformer.<CommonResponse>switchSchedulers())
                .subscribe(new CommonObserver<CommonResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        dismissProgressDialog();
                        mView.onError("网络异常，请稍后再试");
                    }

                    @Override
                    protected void onSuccess(CommonResponse data) {
                        dismissProgressDialog();
                        if (data.getCode() == 1 || data.getCode() == -900) {
                            mView.getVerificationCodeSuccess(data.getMsg());
                        } else {
                            mView.getVerificationCodeFailure(data.getMsg());
                        }
                    }
                });
    }

    @Override
    public void forgetPassword(Map<String, String> params) {
        showProgressDialog();
        ApiHelper.getYiHuaApi()
                .forgetPassword(params)
                .compose(Transformer.<CommonResponse>switchSchedulers())
                .subscribe(new CommonObserver<CommonResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        mView.onError("网络异常，请稍后再试");
                    }

                    @Override
                    protected void onSuccess(CommonResponse data) {
                        dismissProgressDialog();
                        if (data.getCode() == 1) {
                            mView.forgetPasswordSuccess(data.getMsg());
                        } else {
                            mView.forgetPasswordFailure(data.getMsg());
                        }
                    }
                });
    }
}
