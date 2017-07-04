package com.example.zte.day29_zte_fileutils_test;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    private DatePicker datePicker;
    private TextView textView;
    private TextView getTextView;
    private String str = "hezijie|zijiehe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        datePicker = (DatePicker) findViewById(R.id.timePicker);
        textView = (TextView) findViewById(R.id.text);
        getTextView = (TextView) findViewById(R.id.text2);
        getTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = str.indexOf("|");
                Log.e("111", "onClick: "+str.length() );

                String s1 = str.substring(14,15);
                getTextView.setText(s1);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textView);
            }
        });
        datePicker.setCalendarViewShown(false);
        datePicker.setSpinnersShown(true);


//        datePicker.init(2017, 6, 20, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
//                Toast.makeText(Main2Activity.this, format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
//            }
//        });
        datePicker.init(2017, 6, 20, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
                Toast.makeText(Main2Activity.this, format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDatePickerDialog(final TextView result) {
        Calendar calendar = Calendar.getInstance();
        if (TextUtils.isEmpty(result.getText())) {
            calendar.setTime(new Date());
        } else {
            try {
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(result.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month < 10 ? "0" : "") + (month + 1) + "-" + (dayOfMonth < 10 ? "0" : "") + dayOfMonth;
                result.setText(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
