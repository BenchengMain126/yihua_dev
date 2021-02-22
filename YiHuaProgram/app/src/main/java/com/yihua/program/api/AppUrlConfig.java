package com.yihua.program.api;

import com.yihua.program.application.App;
import com.yihua.program.tools.P;

import java.util.HashMap;
import java.util.Map;

public class AppUrlConfig {
    //
    public static final String BASE_URL = P.getInstance(App.getAppContext()).getBaseUrl();
    //生成图片验证码
    public static final String createImageCode = BASE_URL+ "user2/user/createImageCode/?account=";

    public static String BASEURL_KEY = "yihua_api";
    public static String BASEURL_URL = BASE_URL;
    public static Map<String, String> getAllUrl() {
        Map<String, String> map = new HashMap<>();
        map.put(BASEURL_KEY, BASEURL_URL);
        return map;
    }

}
