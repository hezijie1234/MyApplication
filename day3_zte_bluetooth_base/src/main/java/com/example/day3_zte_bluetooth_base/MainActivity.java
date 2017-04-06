package com.example.day3_zte_bluetooth_base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private BluetoothAdapter bluetoothAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int checkBTAdapter(){
        if(null == bluetoothAdapter){
            return -1;
        }
        if(!bluetoothAdapter.isEnabled()){
            return -2;
        }
        return 0;
    }

    public void openBluetooth(View view) {
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);

        }
    }

    public int searchBTD(){
        int i = checkBTAdapter();
        if(i !=0 ){
            return i;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.setPriority(Integer.MAX_VALUE);
        intent = registerReceiver(receiver,filter);
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        Log.e(TAG, "searchBTD: " );
        bluetoothAdapter.startDiscovery();
        return 0;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //如果没有发现蓝牙设备，就不会接收广播。
            Log.e(TAG, "onReceive: "+"接收" );
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG, "onReceive: "+device.getName() );
            }
        }
    };

    public int cancleSearchDev(){
        if(null != bluetoothAdapter && bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        if(intent != null){
            unregisterReceiver(receiver);
        }
        return 0;
    }

    public void searchBTD(View view) {
        searchBTD();
    }
}
