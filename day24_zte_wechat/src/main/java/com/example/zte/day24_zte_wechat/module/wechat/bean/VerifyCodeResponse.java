package com.example.zte.day24_zte_wechat.module.wechat.bean;

/**
 * Created by Administrator on 2017-05-27.
 */

public class VerifyCodeResponse {

    private int code;
    private ResultEntity result;

    public int getCode() {
        return code;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private String verification_token;

        public void setVerification_token(String verification_token) {
            this.verification_token = verification_token;
        }

        public String getVerification_token() {
            return verification_token;
        }
    }
}
