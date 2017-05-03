package com.example.zte.day12_zte_checkboxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private ListView mList;
    private TextView mDataText;
    private CheckBox checkBox;
    private List<String> list = new ArrayList<>();
    Map<Integer,Boolean> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 30; i++) {
            list.add("数据"+i);
            //初始化时默认没有点击全是false，即没有被勾选。
            map.put(i,false);
        }
        mList = (ListView) findViewById(R.id.main_listview);
        mList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_item,parent,false);
                mDataText = (TextView) convertView.findViewById(R.id.item_text);
                checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                Log.e(TAG, "getView: "+position );
                checkBox.setChecked(map.get(position));
                mDataText.setText(list.get(position));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            //被勾选后将所有的数据设置成未被勾选
                            for (int i = 0; i < list.size(); i++) {
                                map.put(i,false);
                            }
                            //在将被勾选的设置成true;
                            map.put(position,true);
                            //必须要及时刷新数据，否者无法实现ui界面上的单选。
                            notifyDataSetChanged();
                        }else{
                            map.put(position,false);
                        }
                    }
                });
                return convertView;
            }
        });
    }

}
