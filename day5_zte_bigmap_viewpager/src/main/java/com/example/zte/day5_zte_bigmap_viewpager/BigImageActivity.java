package com.example.zte.day5_zte_bigmap_viewpager;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BigImageActivity extends AppCompatActivity {
    private ViewPager pager;
    private List<String> urlList = new ArrayList<>();
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        pager = (ViewPager) findViewById(R.id.viewpager);
        initData();
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urlList.size();
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(BigImageActivity.this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(BigImageActivity.this).load(urlList.get(position)).into(imageView);
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation in = AnimationUtils.loadAnimation(BigImageActivity.this, R.anim.in);
                        Animation out = AnimationUtils.loadAnimation(BigImageActivity.this, R.anim.out);
                        in.setDuration(200);
                        out.setDuration(200);
                        finish();
                        overridePendingTransition(R.anim.in,R.anim.out);
                    }
                });
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        pager.setCurrentItem(p);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        urlList = (List<String>) bundle.getSerializable("image");
        p = bundle.getInt("position");
    }
}
