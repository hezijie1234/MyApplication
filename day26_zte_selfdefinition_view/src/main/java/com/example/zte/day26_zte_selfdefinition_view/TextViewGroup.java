package com.example.zte.day26_zte_selfdefinition_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017-05-22.
 */

public class TextViewGroup extends ViewGroup {
    public TextViewGroup(Context context) {
        this(context,null);
    }

    public TextViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultW = widthSize;
        int resultH = heightSize;
        int contentW = getPaddingLeft() + getPaddingRight();
        int contentH = getPaddingBottom() + getPaddingTop();
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        MarginLayoutParams layoutParams = null;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            layoutParams = (MarginLayoutParams) view.getLayoutParams();
            if(view.getVisibility() == View.GONE){
                continue;
            }
            contentW += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            contentH += view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        }

        if(widthMode == MeasureSpec.AT_MOST){
            resultW = contentW < widthSize ? contentW : widthSize;
        }
        if(heightMode == MeasureSpec.AT_MOST){
            resultH = contentH < heightSize ? contentH : heightSize;
        }
        setMeasuredDimension(resultW,resultH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topStart = getPaddingTop();
        int leftStart = getPaddingLeft();
        int childH = 0;
        int childW = 0;
        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            if(child.getVisibility() == View.GONE){
                continue;
            }
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();
            topStart += layoutParams.topMargin;
            leftStart += layoutParams.leftMargin;
            child.layout(leftStart,topStart,leftStart + childW,topStart + childH);
            leftStart += childW + layoutParams.rightMargin;
            topStart += childH + layoutParams.bottomMargin;
        }
    }
}
