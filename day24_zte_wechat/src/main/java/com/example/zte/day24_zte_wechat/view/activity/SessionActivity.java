package com.example.zte.day24_zte_wechat.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zte.day24_zte_wechat.R;

public class SessionActivity extends AppCompatActivity {
    public final static int SESSION_TYPE_PRIVATE = 1;
    public final static int SESSION_TYPE_GROUP = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
    }
}
