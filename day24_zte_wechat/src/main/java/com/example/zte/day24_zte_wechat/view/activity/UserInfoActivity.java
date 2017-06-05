package com.example.zte.day24_zte_wechat.view.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zte.day24_zte_wechat.R;

import io.rong.imlib.model.UserInfo;

public class UserInfoActivity extends BaseActivity {

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("userInfo");

    }
}
