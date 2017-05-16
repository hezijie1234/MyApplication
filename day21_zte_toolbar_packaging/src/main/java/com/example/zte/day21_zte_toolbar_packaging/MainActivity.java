package com.example.zte.day21_zte_toolbar_packaging;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setTitle("这是主activity");

    }

    public void intentTo(View view) {
        startActivity(new Intent(this,TextActivity.class));
    }

    public void bindFragment(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_frame,new TestFragment()).commit();
    }
}
