package com.example.zte.day28_zte_broadreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2017-06-21.
 */

public class StaticBroadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("111", "onReceive: "+intent.getAction().toString() );
    }
}
