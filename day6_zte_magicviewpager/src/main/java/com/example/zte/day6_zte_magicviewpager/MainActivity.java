package com.example.zte.day6_zte_magicviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;

import com.zhy.magicviewpager.transformer.RotateYTransformer;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<String> urlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageMargin(40);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //让viewpager有几万个pager。但是viewpager只会显示最多3个页面。然后让每个页面循环的选择图片久ok了。
                return urlList.size() <= 1 ? urlList.size() : Short.MAX_VALUE;
               // return urlList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(MainActivity.this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(MainActivity.this).load(urlList.get(position % urlList.size())).into(imageView);
                //Picasso.with(MainActivity.this).load(urlList.get(position)).into(imageView);
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return imageView;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.setPageTransformer(true,new ScaleInTransformer(0.6f,new AlphaPageTransformer(0.2f,new RotateYTransformer())));
        viewPager.setCurrentItem(Short.MAX_VALUE / 2);

    }
    private void initData() {
        urlList.add("http://img.daoway.cn/img/2016/12/28/ae157d1f-61db-442a-8085-073438be2a22_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/07/11/0e2e4285-e01a-4217-afa3-f913a6bb0f5a_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/07/11/eba60eb7-5abd-4b87-9491-246087e20800_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/07/18/8b7c6270-80bf-4d0d-a3ab-4e1c57718ce1_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/07/18/165420fb-275e-427b-acae-7e86eab3ee7b_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/07/18/2f539d5f-c879-4383-a4ac-5732e9ef490f_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/09/03/398c0dca-617e-4048-af76-3b267c3225aa_thumb.jpg");
        urlList.add("http://img.daoway.cn/img/2016/09/03/a88bf4fe-26d2-478c-8789-2bd2da9c59dd_thumb.jpg");
    }
}
