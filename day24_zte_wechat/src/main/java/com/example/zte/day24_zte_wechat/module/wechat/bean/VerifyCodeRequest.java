package com.example.zte.day24_zte_wechat.module.wechat.bean;

/**
 * Created by Administrator on 2017-05-27.
 */

public class VerifyCodeRequest {

    private String region;
    private String phone;
    private String code;

    public VerifyCodeRequest(String region, String phone, String code) {
        this.region = region;
        this.phone = phone;
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
