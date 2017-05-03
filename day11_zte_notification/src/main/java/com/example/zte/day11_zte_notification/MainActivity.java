package com.example.zte.day11_zte_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notifyManager;
    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        builder.setContentTitle("测试标题")
                .setContentText("测试内容")
                .setTicker("测试通知来啦")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this,Main2Activity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(false)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher);

    }

    public void sendNotification(View view) {
        notifyManager.notify(0,builder.build());
    }
}
