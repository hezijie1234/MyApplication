package com.example.zte.day7_zte_sortby_pingyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-04-14.
 */

public class SlideBar extends View {

    private LetterChangeListener letterChangeListener;
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    private float mSingleHeight;
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView mTextDialog;
    public static int dialogColor[] = { R.drawable.bg_sidebar_dialog_blue, R.drawable.bg_sidebar_dialog_green,
            R.drawable.bg_sidebar_dialog_orange, R.drawable.bg_sidebar_dialog_purple, R.drawable.bg_sidebar_dialog_red};
    public void setmTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**设置字母改变监听
     * @param letterChangeListener
     */
    public void setLetterChangeListener(LetterChangeListener letterChangeListener) {
        this.letterChangeListener = letterChangeListener;
    }

    public SlideBar(Context context) {
        this(context,null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface LetterChangeListener{
        void onLetterChange(String s);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int length = b.length;
        //获取每一个字母的高度。
        mSingleHeight = height * 1f /length;
        mSingleHeight = (height * 1f - mSingleHeight /2)/length;
        for (int i = 0; i <length ; i++) {
            paint.setColor(Color.parseColor("#2C72AE"));
            //设置字体样式，为默认黑。
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            //抗锯齿方法。
            paint.setAntiAlias(true);
            paint.setTextSize(35);
            if(i == choose){
                paint.setColor(Color.parseColor("#802C72AE"));
                //设定或清除标记FAKE_BOLD_TEXT_FLAG。true为设置。false为清除。
                paint.setFakeBoldText(true);
            }
            //x轴的左边等于中间-字符串宽度的一般。保证字符串在中间
            float xPos = width/2 - paint.measureText((b[i])) / 2;
            float yPos = mSingleHeight*(i+1);
            canvas.drawText(b[i],xPos,yPos,paint);
            //重置画笔。画下一个
            paint.reset();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float eventY = event.getY();
        int oldChoose = choose;
        final LetterChangeListener listener = letterChangeListener;
        final int c = (int) (eventY / getHeight() * b.length);
        switch(action){
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if(mTextDialog != null){
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.bg_sidebar);
                if(oldChoose != c){
                    if(c > 0 && c < b.length){
                        if(listener != null){
                            listener.onLetterChange(b[c]);
                        }
                        if(mTextDialog != null){
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(VISIBLE);
                            int right = mTextDialog.getRight();
                            mTextDialog.setX(right*2/3);
                            if(c > 24){
                                mTextDialog.setY(mSingleHeight * 24);
                            }else {
                                mTextDialog.setY(mSingleHeight * c);
                            }
                            mTextDialog.setBackground(getContext().getResources().getDrawable(dialogColor[c / 6]));
                        }
                    }
                }
                break;
        }
        return true;
    }
}
