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
import android.telephony.TelephonyManager;

/**
 * Created by hezijie on 2017/3/27.
 * 这个类是用来提供网络状态的工具类
 */

public class NetworkStateUtils {
    /**
     * @param context
     * @return 返回是否有网络连接
     */
    public static boolean isHaveNetwork(Context context){
        if(context != null){
            boolean flag = false;
            ConnectivityManager systemService = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param context
     * @return 判断wifi是否开启
     */
    public static boolean isHaveWifi(Context context){
        if(context != null){
            ConnectivityManager systemService = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
        }
        return false;
    }

    /**
     * @param context
     * 当没有网络可用的时候可以调用此方法来设置wifi
     */
    public static void setNetwork(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        context.startActivity(intent);
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

    /**获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * @param context
     * @return 返回网络类型
     */
    public static int getNetworkState(Context context){
        ConnectivityManager systemService = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
