package com.yihua.program.api;

import com.allen.library.RxHttpUtils;

public class ApiHelper {

    public static YiHuaApi getYiHuaApi() {
        return RxHttpUtils.createApi(AppUrlConfig.BASEURL_KEY, AppUrlConfig.BASEURL_URL, YiHuaApi.class);
    }


}
