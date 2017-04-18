package com.example.zte.day6_zte_animation_two;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DecimalFormat format = new DecimalFormat("0.00");
        String s = format.format(100.984025902);
        textView = (TextView) findViewById(R.id.text);
        textView.setText(s);
    }
}
