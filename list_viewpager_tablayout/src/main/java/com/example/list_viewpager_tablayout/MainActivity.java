package com.example.list_viewpager_tablayout;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTablayout;
    private ChildViewPager mViewPager;
    private List<MyListViewFragment> mList;
    private MyScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();
        listener();
    }

    private void listener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //这个监听滑动到底部的时候就加载更多数据
        mScrollView.setOnScrollViewToBottomListener(new MyScrollView.OnScrollViewToBottomListener() {
            @Override
            public void onScrollViewToBottomListener(int type) {
                mList.get(mViewPager.getCurrent()).loadData();
            }
        });

    }

    private void initViews() {
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ChildViewPager)findViewById(R.id.fragment_viewpager);
        mScrollView = (MyScrollView)findViewById(R.id.activity_main_scroll);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);
        //设配tableyout的预缓存页,如果不设置会报错
        mViewPager.setOffscreenPageLimit(3);
        //设置viewpager拦截焦点,可以防止listview滑动到顶部
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    private void initDatas() {
        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MyListViewFragment fragment = new MyListViewFragment();
            mList.add(fragment);
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "标题"+position;
        }
    }
}
