package com.yihua.program.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @描述 正则工具类
 */

public class RegularUtils {

    public static boolean isMobile(String phoneNumber) {
        String MOBLIE_PHONE_PATTERN = "^[1][3456789][0-9]\\d{8}$";
        Pattern p = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPwd(String password) {
//        String str = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
//        String str = "^[0-9]{6,16}$";

        String str = "^[0-9A-Za-z]{6,16}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
