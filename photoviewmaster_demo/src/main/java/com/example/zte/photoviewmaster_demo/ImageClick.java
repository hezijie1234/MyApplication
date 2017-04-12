package com.example.zte.photoviewmaster_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageClick extends AppCompatActivity {
    private PhotoView photoView1;
    private PhotoView photoView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_click);
        photoView1 = (PhotoView) findViewById(R.id.img1);
        photoView2 = (PhotoView) findViewById(R.id.img2);
        photoView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView1.setVisibility(View.GONE);
                photoView2.setVisibility(View.VISIBLE);
                Info info = photoView1.getInfo();
                photoView2.animaFrom(info);
            }
        });

        photoView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView2.setVisibility(View.GONE);
                photoView1.setVisibility(View.VISIBLE);
                Info info = photoView2.getInfo();
                photoView1.animaFrom(info);
            }
        });
        Picasso.with(this).load("http://pic.58pic.com/58pic/13/60/97/48Q58PIC92r_1024.jpg").into(photoView1);
        Picasso.with(this).load("http://pic.58pic.com/58pic/13/60/97/48Q58PIC92r_1024.jpg").into(photoView2);
    }

    @Override
    public void onBackPressed() {
        if(photoView2.getVisibility() == View.VISIBLE){
            Info info = photoView1.getInfo();
            photoView2.animaTo(info, new Runnable() {
                @Override
                public void run() {
                    photoView2.setVisibility(View.GONE);
                    photoView1.setVisibility(View.VISIBLE);
                }
            });
        }
        super.onBackPressed();
    }
}
