package com.example.day3_zte_okhttp_demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.day3_zte_okhttp_demo.api.ApiImpl;
import com.example.day3_zte_okhttp_demo.api.OnResponseListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnResponseListener {

    private static final String TAG = "111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadByOkhttp(View view) {
        ApiImpl.getInstance().testOkhttp(this);
//        File externalStorageDirectory = Environment.getExternalStorageDirectory();
//        String path = externalStorageDirectory.getPath();
//        Log.e(TAG, "loadByOkhttp: path"+path );
    }

    @Override
    public void onAPISuccess(String flag, JSONObject json) {
        GiftBean.DataBean data = JSON.parseObject(json.optString("data"), GiftBean.DataBean.class);
        Log.e(TAG, "onAPISuccess: "+data.getItems().get(0).toString() );
    }

    @Override
    public void onAPIError(String flag, int code, String error) {
        Log.e(TAG, "onAPIError: "+flag );
        Log.e(TAG, "onAPIError: "+error+"-----code"+code );
    }
}
