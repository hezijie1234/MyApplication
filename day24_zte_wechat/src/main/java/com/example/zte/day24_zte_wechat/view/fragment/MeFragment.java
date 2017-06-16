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

public class MeFragment extends BaseFragment{
    private static MeFragment fragment;
    private Context context;
    public static MeFragment getInstance(){
        if(fragment == null){
            synchronized (MessageFragment.class){
                if(fragment == null){
                    fragment = new MeFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    protected void setUserData(boolean b) {
        if(b){
            Log.e("he", "MeFragment-setUserData: 加载数据" );

        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(ConstantsUtil.TAG, "MeFragment-onCreateView: " );
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(ConstantsUtil.TAG, "MeFragment-onAttach: " );
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "MeFragment-onCreate: " );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "MeFragment-onActivityCreated: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(ConstantsUtil.TAG, "MeFragment-onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(ConstantsUtil.TAG, "MeFragment-onResume: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(ConstantsUtil.TAG, "MeFragment-onPause: " );
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e(ConstantsUtil.TAG, "MeFragment-onStop: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(ConstantsUtil.TAG, "MeFragment-onDestroyView: " );
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(ConstantsUtil.TAG, "MeFragment-onDestroy: " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(ConstantsUtil.TAG, "MeFragment-onDetach: " );
    }
}
