package com.example.zte.day18_zte_singletask;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "111";
    private LinearLayout bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: A" );
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(intent != null){
            Bundle extras = intent.getExtras();
            if(extras != null){
                String data = extras.getString("data");
                Log.e(TAG, "onStart: "+data );
            }
        }
        Log.e(TAG, "onStart: A" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:A " );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: A" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop:A " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:A " );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: A" );
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if(intent != null){
//            Bundle extras = intent.getExtras();
//            if(extras != null){
//                String data = extras.getString("data");
//                Log.e(TAG, "onNewIntent: "+data );
//            }
//        }
//    }
    private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
};
    public void intentClick(View view) {
        startActivity(new Intent(this,TestActivity.class));
    }
}
