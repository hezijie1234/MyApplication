package com.example.zte.day24_zte_wechat.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.module.wechat.bean.IsPhoneNum;
import com.example.zte.day24_zte_wechat.module.wechat.bean.PhoneNumBean;
import com.example.zte.day24_zte_wechat.module.wechat.bean.SendCodeBean;
import com.example.zte.day24_zte_wechat.utils.CommonUtils;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.view.MyApplication;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "111";
    @BindView(R.id.activity_register_nick_name_et)
    EditText mNickName;
    @BindView(R.id.activity_register_phonenum_et)
    EditText mPhoneNum;
    @BindView(R.id.activity_register_password_et)
    EditText mPassword;
    @BindView(R.id.activity_register_showpassword_iv)
    ImageView mShowPasswordImage;
    @BindView(R.id.activity_register_authcode)
    EditText mAuthCode;
    @BindView(R.id.activity_register_sendcode_tv)
    TextView mSendAuthCode;
    @BindView(R.id.activity_register_signin_btn)
    Button mRegisterBtn;

    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        listener();
    }
    TextWatcher watcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mRegisterBtn.setEnabled(canRegister());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean canRegister(){
        int nickNameLength = mNickName.getText().toString().trim().length();
        int phoneNumLength = mPhoneNum.getText().toString().trim().length();
        int passwordLength = mPassword.getText().toString().trim().length();
        int authCodeLength = mAuthCode.getText().toString().trim().length();
        if(nickNameLength > 0 && phoneNumLength > 0 && passwordLength > 0 && authCodeLength > 0){
            return true;
        }
        return false;
    }
    private void listener() {
        mNickName.addTextChangedListener(watcher);
        mPhoneNum.addTextChangedListener(watcher);
        mPassword.addTextChangedListener(watcher);
        mAuthCode.addTextChangedListener(watcher);
        mNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        final Retrofit retrofit = MyApplication.getRetrofit();
        //点击发送然后获取验证码。
        mSendAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mPhoneNum.getText().toString())){
                    Log.e(TAG, "onClick: 手机号不能为空" );
                    return;
                }
                if(!CommonUtils.isPhoneNum(mPhoneNum.getText().toString())){
                    Log.e(TAG, "onClick: 请输入正确的手机号码" );
                    return;
                }
                showProgressDialog();
                final RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
                String s = new Gson().toJson(new PhoneNumBean("86",mPhoneNum.getText().toString()));
                Log.e(TAG, "onClick: "+s );
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
                retrofitApi.checkPhoneNum(requestBody)
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<IsPhoneNum, Observable<SendCodeBean>>() {
                            @Override
                            public Observable<SendCodeBean> call(IsPhoneNum isPhoneNum) {
                                int code = isPhoneNum.getCode();
                                if(code == 200){
                                    String s1 = new Gson().toJson(new PhoneNumBean("86",mPhoneNum.getText().toString()));
                                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),s1);
                                    return retrofitApi.sendCode(body);
                                }
                                Log.e(TAG, "call: 手机号码不可用" );
                                return Observable.error(new Exception("手机号不可用"));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<SendCodeBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                String localizedMessage = e.getLocalizedMessage();
                                Toast.makeText(RegisterActivity.this, localizedMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(SendCodeBean sendCodeBean) {
                                hideProgressDialog();
                                int code = sendCodeBean.getCode();
                                if(code == 200){
                                    changeSendCodeBtn();
                                }else {
                                    Toast.makeText(RegisterActivity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        mShowPasswordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
    private int time = 0;
    private Timer mTimer;
    private void changeSendCodeBtn() {
        mSubscribe = (Subscription) Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber< ?super Integer> subscriber) {
                time = 60;
                TimerTask mTask = new TimerTask() {
                    @Override
                    public void run() {
                        subscriber.onNext(--time);
                    }
                };
                mTimer = new Timer();
                mTimer.schedule(mTask, 0, 1000);//每一秒执行一次Task
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " );
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer > 0){
                            mSendAuthCode.setEnabled(false);
                            mSendAuthCode.setText(time+"");
                        }else{
                            mSendAuthCode.setEnabled(true);
                            mSendAuthCode.setText("发送验证码");
                        }
                    }
                });

    }
}
