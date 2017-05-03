package com.example.zte.day_zte_notification_push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "111";
    private ExampleClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = ((MyApplication)getApplicationContext()).getClient();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client != null){
            client.send("_user_logout_xzzzqscy");
            client.close();
        }
        finish();
        System.exit(0);
    }
}
