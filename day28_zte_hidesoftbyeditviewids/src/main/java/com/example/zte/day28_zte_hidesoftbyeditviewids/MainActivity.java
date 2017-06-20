package com.example.zte.day28_zte_hidesoftbyeditviewids;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText1 = (EditText) findViewById(R.id.edit1);
        editText2 = (EditText) findViewById(R.id.edit2);
        editText3 = (EditText) findViewById(R.id.edit3);
        editText4 = (EditText) findViewById(R.id.edit4);
        button = (Button) findViewById(R.id.button);
    }

    /**
     * @return
     */
    public int[] hideSoftByEditViewIds(){
        return new int[]{R.id.edit1,R.id.edit2,R.id.edit3,R.id.edit4};
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            //过滤这个按钮，当这个按钮被点击时不会隐藏软键盘
            if(filterView(button,ev)){
                return super.dispatchTouchEvent(ev);
            }
            //
            if(hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0){
                return super.dispatchTouchEvent(ev);
            }
            View view = getCurrentFocus();
            if(isFocusEditText(view,hideSoftByEditViewIds())){
                hideInputForce(this);
                clearFocus(view,hideSoftByEditViewIds());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**判断获取焦点的view是否是需要处理的view集合中的一个
     * @param view
     * @param ids
     * @return
     */
    public boolean isFocusEditText(View view,int[] ids){
        for (int i = 0; i < ids.length; i++) {
            if(view.getId() == ids[i]){
                return true;
            }
        }
        return false;
    }

    public void clearFocus(View view,int[] ids){
        for (int i = 0; i < ids.length; i++) {
            if(view.getId() == ids[i]){
                EditText editText = (EditText) findViewById(ids[i]);
                editText.clearFocus();
                editText1.clearFocus();
            }
        }
    }
    public boolean filterView(View view,MotionEvent ev){
        if(view == null){
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);//获得的位置是相对于屏幕的
        //view.getLocationOnWindow 在弹出窗口中使用这种方法获取位置是相对于弹出窗的。在飞弹出窗中则获取的位置是相对于屏幕的。
        //view.getX()是获取的相对于父容器的坐标
        float x1 = view.getX();
        //相对于容器的坐标等于0，有一个父容器RelativeLayout.
        float y1 = view.getY();
        int x = location[0];
        int y = location[1];
        Log.e("111", x1+"----"+x );
        Log.e("111", y1+"----"+y );
        //getRawY是获取相对于屏幕的坐标。getX是相对于容器(不一定是父容器)的坐标。
        Log.e("111", "event: "+ev.getY()+"---"+ev.getRawY() );
        if(ev.getX() > x && ev.getX() < (x+ view.getWidth()) && ev.getY() > y && ev.getY() < (y + view.getHeight())){
            return true;
        }
        return false;
    }

    public void hideInputForce(Activity activity){
        if(null == activity || activity.getCurrentFocus() == null){
            return;
        }
        ((InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoft(Context context, View view){
        InputMethodManager methodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if(methodManager != null){
            view.requestFocus();
            methodManager.showSoftInput(view,0);
        }
    }
}
