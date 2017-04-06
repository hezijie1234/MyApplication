package com.example.day3_zte_okhttp_demo.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ml on 2016/9/2.
 */
public class CallbackWrapper implements Callback {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private OnHttpListener listener;

    public CallbackWrapper(OnHttpListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        this.onHttpFailure(-1, e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        int code = response.code();
        String result = response.body().string();
        Log.i("header",response.headers().toString());

        if (response.isSuccessful()) {
            this.onHttpSuccess(code, result);
        } else {
            this.onHttpFailure(code, result);
        }
    }

    private void onHttpSuccess(final int code, final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("http", "code=" + code);
                Log.d("http", result);
                if (listener != null) {
                    //listener这个对象实际是resultParser的对象。将结果（数据主体）返回给resultParser中的onHttpSuccess方法。
                    listener.onHttpSuccess(code, result);
                }
            }
        });
    }

    private void onHttpFailure(final int code, final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("http", "code=" + code);
                if(error != null) {
                    Log.d("http", error);
                }
                if (listener != null) {
                    listener.onHttpFailure(code, error);
                }
            }
        });
    }

    public interface OnHttpListener {

        void onHttpFailure(int code, String error);

        void onHttpSuccess(int code, String result);
    }
}
