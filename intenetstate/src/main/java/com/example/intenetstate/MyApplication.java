package com.example.intenetstate;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



/**
 * Created by hezijie on 2017/3/27.
 * 在使用小米手机测试时，按退出键，程序不会退出，而是在后台保留，再次点击进入的时候，这个初始化方法不会再次执行。
 */

public class MyApplication extends Application {

    private static final String TAG = "111";
    private NetWorkReceiver mBroadReceiver;
    public static boolean isConnected;
    public static String netWorkState;
    @Override
    public void onCreate() {
        super.onCreate();
        netWorkReceiver();
        Log.e(TAG, "onCreate: "+"MyApplication执行了" );
    }

    private void netWorkReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mBroadReceiver = new NetWorkReceiver(mNetWorkHandler);
        registerReceiver(mBroadReceiver,filter);
    }
    private Handler mNetWorkHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsUtil.NetWork.IS_WIFI:
                    netWorkState = ConstantsUtil.NetWork.WIFI;
                    Log.e(TAG, "handleMessage: "+"当前有wifi" );
                    isConnected = true;
                    break;
                case ConstantsUtil.NetWork.IS_MOBILE:
                    netWorkState = ConstantsUtil.NetWork.MOBLIE;
                    Log.e(TAG, "handleMessage: "+"当前有移动网络" );
                    isConnected = true;
                    break;
                case ConstantsUtil.NetWork.NO_CONNECTION:
                    Log.e(TAG, "handleMessage: "+"当前没有任何网络" );
                    isConnected = false;
                    break;
            }
            return false;
        }
    });
}
