package com.example.intenetstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;


/**需要在Application中注册广播，一般采用动态注册
 * Created by hezijie on 2017/3/27.
 */

public class NetWorkReceiver extends BroadcastReceiver {

    private static final String TAG = "111";
    private Handler mHandler;

    public NetWorkReceiver(){

    }
    public NetWorkReceiver(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        isOpenWifi(intent,mHandler);
//        isConnectionWifi(intent,mHandler);
        isConnection(intent,mHandler,context);
    }

    /**
     * @param intent
     * @param mHandler
     * @param context
     * 监听网络连接的设置，包括wifi和移动数据的打开和关闭。(推荐)
     * 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
     *
     * wifi重新开启的时候会比较慢，广播会发送多次。
     * wifi关闭的时候会先显示没有网络可用，这是在进行网络的转换。然后会显示gprs网络是可用的，
     */
    private void isConnection(Intent intent, Handler mHandler, Context context) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null){
                if(networkInfo.isConnected()){
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        mHandler.sendEmptyMessage(ConstantsUtil.NetWork.IS_WIFI);
                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                        mHandler.sendEmptyMessage(ConstantsUtil.NetWork.IS_MOBILE);
                    }
                }else {
                    Log.e(TAG, "isConnection: "+"当前没有网络连接，请确保你的网络已经打开" );
                    mHandler.sendEmptyMessage(ConstantsUtil.NetWork.NO_CONNECTION);
                }
                Log.e(TAG, "Typename: "+networkInfo.getTypeName() );
                Log.e(TAG, "Type: "+networkInfo.getType() );
                Log.e(TAG, "SubtypeName: "+networkInfo.getSubtypeName() );
                Log.e(TAG, "state: "+networkInfo.getState() );
                Log.e(TAG, "isConnection: "+networkInfo.getDetailedState().name() );

            }else{
                Log.e(TAG, "isConnection: "+"当前没有网络连接，请确保你的网络已经打开" );
                mHandler.sendEmptyMessage(ConstantsUtil.NetWork.NO_CONNECTION);
            }
        }
    }


    /**连接有用的wifi（有效无线路由）
     * WifiManager.WIFI_STATE_DISABLING与WIFI_STATE_DISABLED的时候，根本不会接到这个广播
     * @param intent
     * @param mHandler
     */
    private void isConnectionWifi(Intent intent, Handler mHandler) {
        if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(parcelableExtra != null){
                NetworkInfo networkInfo = (NetworkInfo)parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if(isConnected){
                    mHandler.sendEmptyMessage(ConstantsUtil.NetWork.IS_WIFI);
                }
            }
        }
    }

    /**此方法用来监听wifi的打开与关闭
     * @param intent
     * @param mHandler
     */
    private void isOpenWifi(Intent intent, Handler mHandler) {
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
            int intExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (intExtra){
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.e(TAG, "isOpenWifi: "+"wifi关闭" );
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.e(TAG, "isOpenWifi: "+"wifi打开" );
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    }



}
