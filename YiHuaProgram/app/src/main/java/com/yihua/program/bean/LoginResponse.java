package com.yihua.program.bean;

/**
 *  登录成功后的实体类
 */
public class LoginResponse {

   /* {
        "code":"1",
        "msg":"success!",
        "data":{
            "token":"5a0b09ce57664b5a98fd118904e057d9",
            "loginUser":{
                "guid":"535099322987970560",
                "name":"程乐华",
                "account":"15807691356",
                "source":"2",
                "organGuid":null,
                "funcs":null,
                "userPortrait":"http://qiniu.bazhuawang.com/default.png",
                "readOnly":false
                },
            "organDTO":null
         }
    }*/

    private int code;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
