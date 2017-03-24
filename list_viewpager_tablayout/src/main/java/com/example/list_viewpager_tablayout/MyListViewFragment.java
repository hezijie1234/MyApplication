package com.example.list_viewpager_tablayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/3/22.
 */

public class MyListViewFragment extends Fragment {

    private MyListView mListView;
    private List<String> mList;
    private Context context;
    private ArrayAdapter<String> adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        initViews(view);
        listener();
        return view;
    }

    private void listener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    MainActivity mainActivity = (MainActivity)activity;
                    Intent intent = new Intent(mainActivity,SecondActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initViews(View view) {
        mListView = (MyListView) view.findViewById(R.id.fragment_listview);
        mList = new ArrayList<>();
        for (int i = 0; i < 30 ;i++){
            mList.add("数据"+i);
        }
        adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(adapter);
    }

    /**
     * 加载数据后刷新适配器
     */
    public void loadData(){
        mList.add("数据，添加的");
        adapter.notifyDataSetChanged();
    }
}
