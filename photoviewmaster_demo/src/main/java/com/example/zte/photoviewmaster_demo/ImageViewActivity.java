package com.example.zte.photoviewmaster_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;

public class ImageViewActivity extends AppCompatActivity {
    ImageView img;
    PhotoView photoView;

    Info mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        img = (ImageView) findViewById(R.id.img);
        photoView = (PhotoView) findViewById(R.id.photoview);
        photoView.enable();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里将普通的图片资源转换成图片信息。
                mInfo = PhotoView.getImageViewInfo(img);
                img.setVisibility(View.GONE);
                photoView.setVisibility(View.VISIBLE);
                photoView.animaFrom(mInfo);
            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        photoView.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (photoView.getVisibility() == View.VISIBLE) {
            photoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    photoView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
