package com.example.zte.day25_zte_gc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用来测试引用计数算法，在循环引用时是无法回收内存的，
        _2MB_Data d1 = new _2MB_Data();
        _2MB_Data d2 = new _2MB_Data();
        d1.instance = d2;//1
        d2.instance = d1;//2
        d1 = null;
        d2 = null;
        System.gc();
    }
    class _2MB_Data {
        public Object instance = null;
        private byte[] data = new byte[2 * 1024 * 1024];//用来占内存，测试垃圾回收
    }
}
