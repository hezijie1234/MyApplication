package com.example.zte.day24_zte_wechat.module.wechat.bean;

/**
 * Created by Administrator on 2017-05-25.
 */

public class IsPhoneNum {

    private int code;
    private boolean result;

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {

        return code;
    }

    public boolean isResult() {
        return result;
    }
}
