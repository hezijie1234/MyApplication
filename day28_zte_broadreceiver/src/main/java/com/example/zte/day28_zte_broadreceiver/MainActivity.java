package com.example.zte.day28_zte_broadreceiver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //以为当静态注册的广播被关闭之后，就只有调用同样的方法才能将其开启，所以在初始化的时候调用方法打开这个广播，以免广播失效。
        getPackageManager().setComponentEnabledSetting(new ComponentName("com.example.zte.day28_zte_broadreceiver",StaticBroadReceiver.class.getName()), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        initView();
    }

    private void initView() {
        sendBtn = (Button) findViewById(R.id.button1);
        button = (Button) findViewById(R.id.button);
        //点击这个按钮广播会被关闭，除非再次调用方法，将COMPONENT_ENABLED_STATE_ENABLED设置成功。
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPackageManager().setComponentEnabledSetting(new ComponentName("com.example.zte.day28_zte_broadreceiver",StaticBroadReceiver.class.getName()), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("sta_action");
                sendBroadcast(intent);
            }
        });
    }
}
