package com.example.zte.photoviewmaster_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void singleImageClick(View view) {
        startActivity(new Intent(this,ImageActivity.class));
    }

    public void imageClick(View view) {
        startActivity(new Intent(this,ImageClick.class));
    }

    public void normalPager(View view) {
        startActivity(new Intent(this,ViewPagerActivity.class));
    }

    public void browsePhoto(View view) {
        startActivity(new Intent(this,PhotoBrowse.class));
    }

    public void imageview(View view) {
        startActivity(new Intent(this,ImageViewActivity.class));
    }
}
