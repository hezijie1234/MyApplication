package com.example.zte.day26_zte_selfdefinition_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017-05-22.
 */

public class TextView extends View {

    private int mTextSize;
    private String mText;
    private Paint mPaint;

    public TextView(Context context) {
        this(context,null);
    }

    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.TextView);
        mText = ta.getString(R.styleable.TextView_android_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TextView_android_textSize,50);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = widthSize;
        int resultHeight = heightSize;
        int contentW = 0;
        int contentH = 0;
        if(widthMode == MeasureSpec.AT_MOST){
            if(!TextUtils.isEmpty(mText)){
                contentW = (int) mPaint.measureText(mText);
                contentW += getPaddingLeft() + getPaddingRight();
                resultWidth = contentW < widthSize ? contentW : widthSize;
            }
        }

        if(heightMode == MeasureSpec.AT_MOST){
            if(!TextUtils.isEmpty(mText)){
                contentH = (int) mPaint.measureText(mText);
                contentH += getPaddingBottom() + getPaddingTop();
                resultHeight = contentH < heightSize ? contentH : heightSize;
            }
        }
        setMeasuredDimension(resultWidth,resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        canvas.drawColor(Color.RED);
        if(TextUtils.isEmpty(mText)){
            return;
        }
        canvas.drawText(mText,x,y,mPaint);
    }
}
