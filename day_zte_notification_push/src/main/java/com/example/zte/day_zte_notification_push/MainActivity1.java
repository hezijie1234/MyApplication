package com.example.zte.day_zte_notification_push;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity1 extends AppCompatActivity {

    private MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        app = (MyApplication) getApplication(); // 获得MyApplication对象


        
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        this.finish();

    }
}
