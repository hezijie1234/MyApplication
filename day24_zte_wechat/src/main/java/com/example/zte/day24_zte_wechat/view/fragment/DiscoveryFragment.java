package com.example.zte.day24_zte_wechat.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zte.day24_zte_wechat.R;

/**
 * Created by Administrator on 2017-06-01.
 */

public class DiscoveryFragment extends BaseFragment{
    private static DiscoveryFragment fragment;

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
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        return view;
    }
}
