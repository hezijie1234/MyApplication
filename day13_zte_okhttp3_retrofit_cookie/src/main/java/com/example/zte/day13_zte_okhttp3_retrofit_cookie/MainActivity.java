package com.example.zte.day13_zte_okhttp3_retrofit_cookie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "111";
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(HttpTool.getInstance(this).hasCookie()){
            startActivity(new Intent(this,Main2Activity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_password);
    }

    public void login(View view) {
        AppHttpService service = HttpTool.getInstance(this).createService(AppHttpService.class);
        Call<ResponseBody> responseBodyCall = service.queryData(userName.getText().toString(), password.getText().toString(), getDeviceId(this));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.isSuccessful()){
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String c = jsonObject.optString("c");
                        //返回码为0表示登录成功
                        if(Integer.parseInt(c) == 0){
                            startActivity(new Intent(MainActivity.this,Main2Activity.class));
                            finish();
                            //返回码为1001表示没有登录，或者登录缓存过期。无法访问服务器数据。
                        }else if(Integer.parseInt(c) == 1001){
                            HttpTool.getInstance(MainActivity.this).deleteCookie();
                            Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.getContext().startActivity(intent);
                        }else {
                            String m = jsonObject.optString("m");
                            Log.e(TAG, "访问数据失败信息: "+m );
                        }
                    }
                    Log.e(TAG, "onResponse: "+response.code() );
                    if(response.body() != null){
                        Log.e(TAG, "onResponse: "+response.body().string() );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " );
            }
        });
    }
    public static String getDeviceId(Context context){
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if(!TextUtils.isEmpty(tm.getDeviceId())){
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }
}
