package com.example.zte.day5_zte_bigmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (PhotoView) findViewById(R.id.image);
        ImageView image = new ImageView(this);
        imageView.setAnimaDuring(1);
        imageView.setMaxScale(5f);
        imageView.enable();
//        Info info = imageView.getInfo();
        Info info = PhotoView.getImageViewInfo(image);
        imageView.animaFrom(info);
        imageView.animaTo(info, new Runnable() {
            @Override
            public void run() {

            }
        });
        Picasso.with(this).load("http://pic.58pic.com/58pic/13/60/97/48Q58PIC92r_1024.jpg")
                .into(image);
    }
}
