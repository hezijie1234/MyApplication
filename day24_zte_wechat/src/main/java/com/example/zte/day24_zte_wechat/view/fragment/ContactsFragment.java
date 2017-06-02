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

public class ContactsFragment extends BaseFragment{
    private static ContactsFragment fragment;

    public static ContactsFragment getInstance(){
        if(fragment == null){
            synchronized (MessageFragment.class){
                if(fragment == null){
                    fragment = new ContactsFragment();
                }
            }
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        return view;
    }
}
