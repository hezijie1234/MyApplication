package com.example.zte.day24_zte_wechat.module.wechat.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-06-05.
 */

public class TestBean implements Parcelable {
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
    }

    public TestBean() {
    }

    protected TestBean(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
    }

    public static final Creator<TestBean> CREATOR = new Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel source) {
            return new TestBean(source);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };
}
