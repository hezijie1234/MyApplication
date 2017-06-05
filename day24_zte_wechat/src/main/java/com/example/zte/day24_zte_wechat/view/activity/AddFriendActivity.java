package com.example.zte.day24_zte_wechat.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zte.day24_zte_wechat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendActivity extends BaseActivity{
    @BindView(R.id.activity_add_search)
    LinearLayout mLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initData();
        listener();
    }

    private void listener() {
        mLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFriendActivity.this,SearchActivity.class));
            }
        });

    }

    private void initData() {
        showBackView();
        showTitleView("添加朋友");
    }
}
