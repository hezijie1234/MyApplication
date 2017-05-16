package com.example.zte.day18_zte_algorithm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.main_et);
        textView = (TextView) findViewById(R.id.main_tv);
    }

    public void onClickNum(View view) {
        Editable text = editText.getText();
        if(null != text && text.length() > 0){
            textView.setText(""+(isMaoPu(Integer.parseInt(text.toString())) && judgeNum(Integer.parseInt(text.toString())) ));
        }
    }

    private boolean isMaoPu(int i){
        if(i < 10){
            return i == 2;
        }
        return i % 10 == 3 && isMaoPu(i / 10);
    }

    private boolean judgeNum(int num){
        boolean flag = true;
        if(num < 2){
            return false;
        }
        for (int i = 2; i < num / 2; i++) {
            if(num % i == 0){
                flag = false;
            }
        }
        return flag;
    }
}
