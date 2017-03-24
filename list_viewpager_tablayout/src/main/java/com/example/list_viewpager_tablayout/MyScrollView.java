package com.example.list_viewpager_tablayout;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;


/**
 * Created by Administrator on 2017/3/21.
 */

public class MyScrollView extends NestedScrollView {

    private static final String TAG = "111";
    private int type;
    private OnScrollViewToBottomListener onScrollViewToBottom;
    private ScrollViewToBottomListener scrollViewToBottomListener;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollViewToBottomListener(OnScrollViewToBottomListener scrollViewListener){
        this.onScrollViewToBottom = scrollViewListener;
    }

    public void setScrollViewToBottomListener(ScrollViewToBottomListener scrollViewToBottomListener) {
        this.scrollViewToBottomListener = scrollViewToBottomListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View childAt = (View) getChildAt(getChildCount() - 1);
        //Log.e(TAG, "getChildCount: " +getChildCount());
        int bottom = childAt.getBottom();
        //getBottom子控件的底部位置，
        //Log.e(TAG, "getBottom: "+childAt.getBottom() );
        //getheight 是获取控件的高度
       // Log.e(TAG, "getHeight: "+getHeight() );
        //getScrollY 是控件在y轴上滑动的距离
        bottom = bottom - (getHeight()+getScrollY());
        //Log.e(TAG, "getScrollY: "+getScrollY() );
        if(bottom == 0){
            //bottom==0就表示scrollview最后一个子控件显示在屏幕的底部。
            if(onScrollViewToBottom != null){
                onScrollViewToBottom.onScrollViewToBottomListener(type);
            }
        }else{
            if(scrollViewToBottomListener != null){
                scrollViewToBottomListener.scrollViewToBottomListener(this,l,t,oldl,oldt);
            }
        }
    }

    interface OnScrollViewToBottomListener{
        void onScrollViewToBottomListener(int type);
    }

    interface ScrollViewToBottomListener{
        void scrollViewToBottomListener(MyScrollView scrollView,int x,int y,int oldx,int oldy);
    }

}
