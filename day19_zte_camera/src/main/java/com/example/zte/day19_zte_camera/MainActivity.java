package com.example.zte.day19_zte_camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            getFragmentManager().beginTransaction().add(R.id.container,Camera2BasicFragment.newInstance()).commit();
        }
    }
}
