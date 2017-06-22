package com.example.zte.day28_zte_broadreceiver;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private Button button;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = (Button) findViewById(R.id.sp_jump_btn);
        imageView = (ImageView) findViewById(R.id.image);
        countDownTimer.start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                countDownTimer.cancel();
                finish();
            }
        });
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3200,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            button.setText("跳转(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            button.setText("跳转(" + 0 + "s)");
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    };
}
