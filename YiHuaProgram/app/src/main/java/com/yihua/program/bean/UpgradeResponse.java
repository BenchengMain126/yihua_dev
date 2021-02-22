package com.yihua.program.bean;


import java.util.List;

public class UpgradeResponse {


    /**
     * {
     * "code": 1,
     * "msg": "成功！",
     * "data": {
     * "description": null,
     * "ifForceUpdate": "0",
     * "versionName": "1.5.0",
     * "ifElasticFrame": "1",
     * "versionCode": "150",
     * "url": "http://qiniu.bazhuawang.com/app-install"
     * }
     * }
     */


    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String versionNo;
        private List<String> updateContent;
        private String createDate;
        private String versionCode;
        private String downloadUrl;
        private String ifForceUpdate;
        private String ifElasticFrame;

        public String getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public List<String> getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(List<String> updateContent) {
            this.updateContent = updateContent;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getIfForceUpdate() {
            return ifForceUpdate;
        }

        public void setIfForceUpdate(String ifForceUpdate) {
            this.ifForceUpdate = ifForceUpdate;
        }

        public String getIfElasticFrame() {
            return ifElasticFrame;
        }

        public void setIfElasticFrame(String ifElasticFrame) {
            this.ifElasticFrame = ifElasticFrame;
        }
    }


}
