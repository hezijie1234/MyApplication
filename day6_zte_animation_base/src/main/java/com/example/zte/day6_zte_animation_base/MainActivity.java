package com.example.zte.day6_zte_animation_base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_test);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pivotX这个属性是一动画执行者的坐标百分比位置作为中心轴。
                imageView.startAnimation(animation);
            }
        });

    }
}
