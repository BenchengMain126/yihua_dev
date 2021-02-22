package com.yihua.program.api;

import com.yihua.program.bean.CommonResponse;
import com.yihua.program.bean.LoginResponse;
import com.yihua.program.bean.ToTalMessage;
import com.yihua.program.bean.UpgradeResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YiHuaApi {

    /**
     *POST /user/accountVerifyCode
     * 验证账号是否需要图片验证码
     *
     * Implementation Notes
     * 验证账号是否需要图片验证码
     * http://121.15.166.3:8888/user2/user/accountVerifyCode?account=15807691356
     */
    @POST("user2/user/accountVerifyCode")
    @FormUrlEncoded
    Observable<CommonResponse> accountVerifyCode(@FieldMap Map<String, String> params);

    /**
     *POST /user/login
     * 登陆
     *
     * Implementation Notes
     * 登陆接口
     * http://121.15.166.3:8888/user2/user/login?account=13923835774&password=1&source=2
     */
    @POST("user2/user/login")
    @FormUrlEncoded
    Observable<LoginResponse> login(@FieldMap Map<String, String> params);


    /**
     *POST /user/getVerificationCode
     * 手机获取验证码
     *
     * Implementation Notes
     * 手机获取验证码
     * http://121.15.166.3:8888/user2/user/getVerificationCode?mobile=15807691356&checkSign=83294164D62FDEBA862D16ED9FEEC24D
     */
    @POST("user2/user/getVerificationCode")
    @FormUrlEncoded
    Observable<CommonResponse> getVerificationCode(@FieldMap Map<String, String> params);


    /**
     *POST /user/loginOrRegisterByMobile
     * 手机短信验证码登陆
     *
     * Implementation Notes
     * 手机短信验证码登陆
     * http://121.15.166.3:8888/user2/user/loginOrRegisterByMobile?mobile=15807691356&verificationCode=123456&source=2
     */
    @POST("user2/user/loginOrRegisterByMobile")
    @FormUrlEncoded
    Observable<LoginResponse> loginOrRegisterByMobile(@FieldMap Map<String, String> params);



    /**
     *POST /user/forgetPassword
     * 忘记密码
     *
     * Implementation Notes
     * 忘记密码
     * http://121.15.166.3:8888/user2/user/forgetPassword?account=15807691356&verificationCode=123456&newPassword=666666
     */
    @POST("user2/user/forgetPassword")
    @FormUrlEncoded
    Observable<CommonResponse> forgetPassword(@FieldMap Map<String, String> params);





    /**
     * POST /upgrade
     * upgrade
     *
     * http://121.15.166.3:8888/property/upgrade
     *
     */
    @POST("property/upgrade")
    @FormUrlEncoded
    Observable<UpgradeResponse> upgrade(@FieldMap Map<String, String> params);


    /**
     * 未读信息数
     *
     * @param params
     * @return
     */
    @POST("user/user/totalMessage")
    @FormUrlEncoded
    Observable<ToTalMessage> totalMessage(@FieldMap Map<String, String> params);
}
