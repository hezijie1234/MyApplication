package com.example.zte.day22_zte_baseactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showBackView();
        showTitleView("这是主activity的测试");
    }

    public void loadClick(View view) {
        showProgressDialog();
    }

    public void unloadClick(View view) {
        hideProgressDialog();
    }
}
