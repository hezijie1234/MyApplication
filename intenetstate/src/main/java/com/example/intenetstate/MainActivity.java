package com.example.intenetstate;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "111";
    private static ConnectivityManager systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void loadBtn(View view) {
        //测试下面的方法
//        checkNetworkState();
//        int networkState = getNetworkState(this);
//        Log.e(TAG, "loadBtn: "+networkState );
        //测试工具类
//        boolean haveNetwork = NetworkStateUtils.isHaveNetwork(this);
//        if(haveNetwork){
//            Log.e(TAG, "loadBtn: "+"有网" );
//        }else{
//            Log.e(TAG, "loadBtn: "+"没网" );
//            NetworkStateUtils.setNetwork(this);
//        }
//        boolean haveWifi = NetworkStateUtils.isHaveWifi(this);
//        if(haveWifi){
//            Log.e(TAG, "loadBtn: "+"目前有wifi" );
//        }else{
//            Log.e(TAG, "loadBtn: "+"目前没有wifi" );
//            NetworkStateUtils.setNetwork(this);
//        }
//        Log.e(TAG, "loadBtn: "+"目前的网络类别"+NetworkStateUtils.getNetworkState(this) );
    }

    /**这个方法是用来判断是否有网络连接
     * @return 返回是否有网络连接
     */
    private boolean checkNetworkState() {
        boolean flag = false;
        systemService = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService.getActiveNetworkInfo() != null) {
            flag = systemService.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            Log.e(TAG, "checkNetworkState: "+"没有网" );
            setNetwork();
        } else {
            Log.e(TAG, "checkNetworkState: "+"有网" );
            isWhichNetwork();
        }
        return flag;
    }

    /**
     * 当网络不可用的时候久设置网络
     */
    private void setNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("网络提示信息")
                .setMessage("网络不可用，如果继续请先设置网络")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //手机APi>10的时候是3.0以上系统，他们设置网络的方式不同。
                        Intent intent = null;
                        if(Build.VERSION.SDK_INT > 10){
                            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        }else{
                            intent = new Intent();
                            ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                            intent.setComponent(componentName);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    /**
     * 一下2中方式都可以判断网络类别，但是当wifi和移动网络都开启的时候，只会检测到wifi开启了，而不会检测移动网络。检测后发现2中方式实际效果没有差别
     */
    public void isWhichNetwork() {
        //方法一
//        NetworkInfo.State gprs = systemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
//        NetworkInfo.State wifi = systemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//        if(gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING){
//            Log.e(TAG, "isWhichNetwork: "+"移动网络已开启" );
//        }
//        if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
//            Log.e(TAG, "isWhichNetwork: "+"wifi已开启" );
//        }
//方法二
        NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            Log.e(TAG, "isWhichNetwork: "+"wifi已开启" );
        }
        if(activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            Log.e(TAG, "isWhichNetwork: "+"移动网络已开启" );
        }
    }

    public static int getNetworkState(Context context){
        int type = 0;
        NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();
        if(activeNetworkInfo == null){
            return type;
        }

        int netType = activeNetworkInfo.getType();
        if(netType == ConnectivityManager.TYPE_WIFI){
            type = 1;
        }else if(netType == ConnectivityManager.TYPE_MOBILE){
            int subtype = activeNetworkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(subtype == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming()){
                type = 4;
            }else if (subtype == TelephonyManager.NETWORK_TYPE_UMTS
                    || subtype == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subtype == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()){
                type = 3;
            }else if (subtype == TelephonyManager.NETWORK_TYPE_GPRS
                            || subtype == TelephonyManager.NETWORK_TYPE_EDGE
                            || subtype == TelephonyManager.NETWORK_TYPE_CDMA
                            && !telephonyManager.isNetworkRoaming()){
                type = 2;
            }else{
                type = 2;
            }
        }
        return type;
    }
}
