package com.example.coordinator_two;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        view.setY(view.getY()+10);
    }

    public void click2(View view) {
        ViewCompat.offsetLeftAndRight(view,2);
    }
}
