package com.yihua.program.view.presenter;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.yihua.program.api.ApiHelper;
import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.bean.CommonResponse;
import com.yihua.program.bean.LoginResponse;
import com.yihua.program.dbmodel.SaveLocal;
import com.yihua.program.utils.MD5Util;
import com.yihua.program.view.contract.SmsLoginContract;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SmsLoginPresenter extends BasePresenter<SmsLoginContract.IView> implements SmsLoginContract.IPresenter {

    public SmsLoginPresenter(Activity activity, SmsLoginContract.IView iv) {
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
    public void loginOrRegisterByMobile(Map<String, String> params) {
        showProgressDialog();
        ApiHelper.getYiHuaApi()
                .loginOrRegisterByMobile(params)
                .compose(Transformer.<LoginResponse>switchSchedulers())
                .subscribe(new CommonObserver<LoginResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        mView.onError("网络异常，请稍后再试");
                        dismissProgressDialog();
                    }

                    @Override
                    protected void onSuccess(LoginResponse data) {
                        dismissProgressDialog();
                        if (data.getCode() == 1) {
                            //保存登录返回的信息
                            LitePal.deleteAll(SaveLocal.class, "key = ?", "loginInfos");
                            SaveLocal saveLocal_loginInfos = new SaveLocal();
                            saveLocal_loginInfos.setKey("loginInfos");
                            saveLocal_loginInfos.setValue(JSON.toJSONString(data.getData(), SerializerFeature.WriteMapNullValue));
                            saveLocal_loginInfos.save();

                            mView.loginOrRegisterSuccess(data.getMsg());
                        } else {
                            mView.loginOrRegisterFailure(data.getMsg());
                        }
                    }
                });
    }


}
