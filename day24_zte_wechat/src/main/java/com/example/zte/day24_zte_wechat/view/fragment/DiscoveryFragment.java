package com.example.zte.day24_zte_wechat.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;

/**
 * Created by Administrator on 2017-06-01.
 */

public class DiscoveryFragment extends BaseFragment{
    private static DiscoveryFragment fragment;
    private Context context;
    public static DiscoveryFragment getInstance(){
        if(fragment == null){
            synchronized (MessageFragment.class){
                if(fragment == null){
                    fragment = new DiscoveryFragment();
                }
            }
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onCreateView: " );
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        return view;
    }

    @Override
    protected void setUserData(boolean b) {
        if(b){
            Log.e("he", "DiscoveryFragment-setUserData: 加载数据" );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onAttach: " );
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onCreate: " );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onActivityCreated: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onResume: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onPause: " );
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onStop: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onDestroyView: " );
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onDestroy: " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(ConstantsUtil.TAG, "DiscoveryFragment-onDetach: " );
    }
}
