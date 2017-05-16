package com.example.zte.day18_zte_singletask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.e(TAG, "onCreate:B ");
    }

    public void click(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("data","这是测试数据");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: B" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:B " );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: B" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: B" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:B " );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart:B " );
    }
}
