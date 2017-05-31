package com.example.zte.day24_zte_wechat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.activity_main_first_fl)
    FrameLayout mFirstFrame;
    @BindView(R.id.activity_main_first_f2)
    FrameLayout mSecondFrame;
    @BindView(R.id.activity_main_first_f3)
    FrameLayout mThirdFrame;
    @BindView(R.id.activity_main_first_f4)
    FrameLayout mForthFrame;
    @BindView(R.id.activity_main_first)
    RadioButton mFirstRadio;
    @BindView(R.id.activity_main_second)
    RadioButton mSecondRadio;
    @BindView(R.id.activity_main_third)
    RadioButton mThirdRadio;
    @BindView(R.id.activity_main_forth)
    RadioButton mForthRadio;
    @BindView(R.id.activity_main_first_tv)
    TextView mFirstTextView;
    @BindView(R.id.activity_main_second_tv)
    TextView mSecondTextView;
    @BindView(R.id.activity_main_third_tv)
    TextView mThirdTextView;
    @BindView(R.id.activity_main_forth_tv)
    TextView mForthTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        listener();
    }

    private void listener() {
        mFirstFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstRadio.setChecked(true);
                setTextColor();
                mFirstTextView.setTextColor(getResources().getColor(R.color.green0));
            }
        });
        mSecondFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSecondRadio.setChecked(true);
                setTextColor();
                mSecondTextView.setTextColor(getResources().getColor(R.color.green0));
            }
        });
        mThirdFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThirdRadio.setChecked(true);
                setTextColor();
                mThirdTextView.setTextColor(getResources().getColor(R.color.green0));
            }
        });
        mForthFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mForthRadio.setChecked(true);
                setTextColor();
                mForthTextView.setTextColor(getResources().getColor(R.color.green0));
            }
        });
    }

    private void setTextColor() {
        mFirstTextView.setTextColor(getResources().getColor(R.color.gray0));
        mSecondTextView.setTextColor(getResources().getColor(R.color.gray0));
        mThirdTextView.setTextColor(getResources().getColor(R.color.gray0));
        mForthTextView.setTextColor(getResources().getColor(R.color.gray0));
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
