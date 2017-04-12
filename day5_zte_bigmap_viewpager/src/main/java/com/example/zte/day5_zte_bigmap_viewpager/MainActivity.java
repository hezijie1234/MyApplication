package com.example.zte.day5_zte_bigmap_viewpager;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> urlList = new ArrayList<>();
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return urlList == null ? 0 :urlList.size();
            }

            @Override
            public Object getItem(int position) {
                return urlList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(MainActivity.this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                Picasso.with(MainActivity.this).load(urlList.get(position)).into(imageView);
                return imageView;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,BigImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("image", (Serializable) urlList);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });
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
