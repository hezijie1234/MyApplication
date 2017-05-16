package com.example.zte.day18_zte_utilmoretext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private TextView tv2;
    private String msg ="在Android开发中，有许多信息展示需要通过TextView来展现，如果只是普通的信息展现，使用TextView setText(CharSequence str)设置即可，但是当在TextView里的这段内容需要截取某一部分字段，可以被点击以及响应响应的操作，这时候就需要用到SpannableString了，下面通过一段简单的代码实现部分文字被点击响应，及富文本表情的实现";
    private int open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setTextSize(20);
        new UtilMoreText(tv2,msg);
        tv1.setText(getClickableSpan(0));
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private SpannableString getClickableSpan(int flag) {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText(getClickableSpan(open));
                Toast.makeText(MainActivity.this, "000", Toast.LENGTH_SHORT).show();
            }
        };
        SpannableString spanableInfo = new SpannableString(
                "This is a test, Click ...Me");
        int start = spanableInfo.length()-5;
        int end = spanableInfo.length();
        switch (flag){
            case 0:
                spanableInfo = new SpannableString(
                        "This is a test, Click ...Me");
                start = spanableInfo.length()-5;
                end = spanableInfo.length();
                spanableInfo.setSpan(new Clickable(l), start, end,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                open = 1;
                break;
            case 1:
                spanableInfo = new SpannableString(
                        "在Android开发中，有许多信息展示需要通过TextView来展现，如果只是普通的信息展现，使用TextView setText(CharSequence str)设置即可，但是当在TextView里的这段内容需要截取某一部分字段，可以被点击以及响应响应的操作，这时候就需要用到SpannableString了，下面通过一段简单的代码实现部分文字被点击响应，及富文本表情的实现 ...收起");
                start = spanableInfo.length()-5;
                end = spanableInfo.length();
                spanableInfo.setSpan(new Clickable(l), start, end,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                open = 0;
                break;
        }

        return spanableInfo;
    }
    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }
}
