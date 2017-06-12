package com.example.zte.day24_zte_wechat.module.wechat.bean;

/**
 * Created by Administrator on 2017-06-08.
 */

public class ContactNotificationData {
    private String sourceUserNickname;
    private long version;

    public void setSourceUserNickname(String sourceUserNickname) {
        this.sourceUserNickname = sourceUserNickname;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getSourceUserNickname() {
        return sourceUserNickname;
    }

    public long getVersion() {
        return version;
    }
}
