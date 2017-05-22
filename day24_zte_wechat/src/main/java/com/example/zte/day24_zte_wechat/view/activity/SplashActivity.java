package com.example.zte.day24_zte_wechat.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.zte.day24_zte_wechat.MainActivity;
import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionGen;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.activity_splash_login_btn)
    Button mLoginBtn;
    @BindView(R.id.activity_splash_register_btn)
    Button mRegisterBtn;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化butterKnife
        ButterKnife.bind(this);
        PermissionGen.with(this).addRequestCode(100).permissions(
                //电话通讯录
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_PHONE_STATE,
                //位置
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                //相机、麦克风
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                //存储空间
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS
        ).request();
        if(!TextUtils.isEmpty(SharePreferenceUtil.getInstance(this).getString(ConstantsUtil.TOKEN,""))){
            mLoginBtn.setVisibility(View.INVISIBLE);
            mRegisterBtn.setVisibility(View.INVISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            },3000);
        }else{
            setListener();
        }

    }

    private void setListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,RegisterActivity.class));
                finish();
            }
        });

    }
}
