package com.example.zte.day_zte_notification_push;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.java_websocket.drafts.Draft_10;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017-04-26.
 */

public class MyApplication extends Application {

    private static Context context;
    private static ExampleClient client;

    public static ExampleClient getClient() {
        return client;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            client = new ExampleClient(new URI("ws://10.0.2.2:8887"),new Draft_10());
            try {
                client.connectBlocking();
                client.send("_user_login_xzzzqscy");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("webSecket", "WebSecket连接异常" + e.getMessage());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("webSecket", "uri异常" + e.getMessage());
        }
    }
}
