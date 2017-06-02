package com.example.zte.day24_zte_wechat.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zte.day24_zte_wechat.MainActivity;
import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.module.wechat.bean.LoginRequest;
import com.example.zte.day24_zte_wechat.module.wechat.bean.LoginResponse;
import com.example.zte.day24_zte_wechat.utils.CommonUtils;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;
import com.example.zte.day24_zte_wechat.view.MyApplication;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.activity_login_phone)
    EditText mPhoneNum;
    @BindView(R.id.activity_login_password)
    EditText mPassword;
    @BindView(R.id.activity_login_loginbtn)
    Button mLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        listener();
    }

    private void listener() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                if(!CommonUtils.isPhoneNum(mPhoneNum.getText().toString())){
                    Toast.makeText(LoginActivity.this, "电话号码格式不符合", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(mPassword.getText().toString())){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                Retrofit retrofit = MyApplication.getRetrofit();
                RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
                Gson gson = new Gson();
                String loginStr = gson.toJson(new LoginRequest("86", mPhoneNum.getText().toString(), mPassword.getText().toString()));
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),loginStr);
                retrofitApi.login(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LoginResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(ConstantsUtil.TAG, "onError: 登录错误" );
                            }

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                hideProgressDialog();
                                if(loginResponse.getCode() == 200){
                                    String token = loginResponse.getResult().getToken();
                                    Log.e("111", "onNext: "+token );
                                    SharePreferenceUtil.getInstance(LoginActivity.this).putString("token",token);
                                    SharePreferenceUtil.getInstance(LoginActivity.this).putString("id",loginResponse.getResult().getId());
                                    SharePreferenceUtil.getInstance(LoginActivity.this).putString("phone",mPhoneNum.getText().toString().trim());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "登录错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
