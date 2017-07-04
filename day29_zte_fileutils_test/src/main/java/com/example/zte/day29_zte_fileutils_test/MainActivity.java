package com.example.zte.day29_zte_fileutils_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button cacheBitmap;
    private Button timeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        timeBtn = (Button) findViewById(R.id.go_to_time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = FileUtil.getSDCardRootPath()+ File.separator + "Jingwutong";
                Log.e("111", "onClick: "+filePath );
                String folderSize = FileUtil.showSize(filePath);
                Log.e("111", "onClick: "+folderSize+"MB" );
            }
        });

//        cacheBitmap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView image = new ImageView(MainActivity.this);
//                image.setImageResource(R.mipmap.ic_launcher);
//
//
//
//            }
//        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }
}
