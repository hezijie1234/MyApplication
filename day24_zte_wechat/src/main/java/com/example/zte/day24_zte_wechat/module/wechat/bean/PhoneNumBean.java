package com.example.zte.day24_zte_wechat.module.wechat.bean;

import java.io.Serializable;



public class PhoneNumBean {


    public PhoneNumBean(String region, String phone ) {
        this.phone = phone;
        this.region = region;
    }

    private String phone;
    private String region;

    public String getPhone() {
        return phone;
    }

    public String getRegion() {
        return region;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
