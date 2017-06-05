package com.example.zte.day24_zte_wechat.module.wechat.bean;

/**
 * Created by Administrator on 2017-06-05.
 */

public class FriSearchRequest {
    private String friendId;
    private String message;

    public FriSearchRequest(String userid, String addFrinedMessage) {
        this.message = addFrinedMessage;
        this.friendId = userid;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
