package com.yihua.program.view.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yihua.program.api.ApiHelper;
import com.yihua.program.api.AppUrlConfig;
import com.yihua.program.application.App;
import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.bean.LoginResponse;
import com.yihua.program.dbmodel.SaveLocal;
import com.yihua.program.tools.L;
import com.yihua.program.utils.AuthorizationUtils;
import com.yihua.program.utils.IOSToast;
import com.yihua.program.view.contract.LoginContract;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.litepal.LitePal;

import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter {

    public LoginPresenter(Activity activity, LoginContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void CheckPermission() {
        XXPermissions.with(mActivity)
                .permission(Permission.CAMERA)
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            L.e("权限已授予");
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!App.isIgnoringBatteryOptimizations(mActivity)) {
                                    App.requestIgnoreBatteryOptimizations(mActivity);
                                }
                            }*/
                        } else {
                            IOSToast.show(mActivity, "部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            IOSToast.show(mActivity, "部分权限被永久拒绝授权，请手动授予");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(mActivity, denied);
                        } else {
                            IOSToast.show(mActivity, "应用权限未正常授予");
                        }
                    }
                });
    }


    @Override
    public void createImageCode(String phone) {
        String url = AppUrlConfig.createImageCode + phone;
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.onError("网络异常，请稍后再试");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        mView.createImageCodeSuccess(response);
                    }
                });
    }

    @Override
    public void login(Map<String, String> params) {
        showProgressDialog();
        ApiHelper.getYiHuaApi()
                .login(params)
                .compose(Transformer.<LoginResponse>switchSchedulers())
                .subscribe(new CommonObserver<LoginResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        dismissProgressDialog();
                        mView.onError("网络异常，请稍后再试");
                    }

                    @Override
                    protected void onSuccess(LoginResponse data) {
                        dismissProgressDialog();
                        if (data.getCode() == 1) {
                            //保存登录返回的信息
                            LitePal.deleteAll(SaveLocal.class, "key = ?", "loginInfos");
                            SaveLocal saveLocal = new SaveLocal();
                            saveLocal.setKey("loginInfos");
                            saveLocal.setValue(JSON.toJSONString(data.getData(), SerializerFeature.WriteMapNullValue));
                            saveLocal.save();

                            mView.loginSuccess(data.getMsg());
                        } else if (data.getCode() == -903) {
                            mView.loginShowVerifyFailure(data.getMsg());
                        } else {
                            mView.loginFailure(data.getMsg());
                        }
                    }
                });
    }

    /**
     * fastjson过滤器将null值转换为字符串
     */
    public static final ValueFilter FILTER = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if (v == null)
                return "";
            return v;
        }
    };

}
