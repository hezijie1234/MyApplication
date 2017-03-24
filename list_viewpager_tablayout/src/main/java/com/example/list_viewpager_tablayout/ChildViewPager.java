package com.example.list_viewpager_tablayout;

import android.content.Context;
import android.icu.util.Measure;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by hezijie on 2017/3/21.
 */

public class ChildViewPager extends ViewPager {

    private static final String TAG = "111";
    private int current;
    private HashMap<Integer,Integer> maps = new LinkedHashMap<>();
    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getCurrent() {
        return current;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            //Log.e(TAG, "onMeasure: "+getChildCount() );
            //在为当前控件测量高度的时候，为每一个子控件设置一个不受限制的高度
            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            //Log.e(TAG, "onMeasure:child.getMeasuredHeight "+h );
            maps.put(i,h);
        }
        if(getChildCount() > 0){
            //当子控件的数量大于0的时候就获取这个显示的pager的高度，并将其设置为控件的高度
            height =  getChildAt(current).getMeasuredHeight();
            //Log.e(TAG, "onMeasure: viewpager的高度"+height );
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        this.current = current;
        //Log.e(TAG, "resetHeight: "+maps.size() );
        if (current < maps.size()) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current));
            }else{
                layoutParams.height = maps.get(current);
            }
            setLayoutParams(layoutParams);
        }
    }
}
