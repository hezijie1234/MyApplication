package com.example.zte.day24_zte_websocket_notification;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.port;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "111";
    private EditText mIPEdt, mPortEdt, mSocketIDEdt, mMessageEdt;
    private static TextView mConsoleTxt;

    private static StringBuffer mConsoleStr = new StringBuffer();
    private Socket mSocket;
    private SocketHandler mHandler;
    private boolean isStartRecieveMsg;
    protected BufferedReader mReader;//BufferedWriter 用于推送消息
    protected BufferedWriter mWriter;//BufferedReader 用于接收消息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSocket();
    }
    private void initView() {
        mIPEdt = (EditText) findViewById(R.id.ip_edt);
        mPortEdt = (EditText) findViewById(R.id.port_edt);
        mSocketIDEdt = (EditText) findViewById(R.id.socket_id_edt);
        mMessageEdt = (EditText) findViewById(R.id.msg_edt);
        mConsoleTxt = (TextView) findViewById(R.id.console_txt);
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.send_btn).setOnClickListener(this);
        findViewById(R.id.clear_btn).setOnClickListener(this);
        mHandler = new SocketHandler();
    }
    private void initSocket() {
        //新建一个线程，用于初始化socket和检测是否有接收到新的消息
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket("192.168.1.254", 2000);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    while(isStartRecieveMsg) {
                        if(mReader.ready()) {
                            /*读取一行字符串，读取的内容来自于客户机
                            reader.readLine()方法是一个阻塞方法，
                            从调用这个方法开始，该线程会一直处于阻塞状态，
                            直到接收到新的消息，代码才会往下走*/
                            String data = mReader.readLine();
                            //handler发送消息，在handleMessage()方法中接收
                            Log.e(TAG, "run: "+data );
                        }
                        Thread.sleep(200);
                    }
                    mWriter.close();
                    mReader.close();
                    mSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                send();
                break;
            case R.id.clear_btn:
                mConsoleStr.delete(0, mConsoleStr.length());
                mConsoleTxt.setText(mConsoleStr.toString());
                break;
            case R.id.start_btn:
                if(!isStartRecieveMsg) {
                    initSocket();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送
     */
    private void send() {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                sendMsg();
                return null;
            }
        }.execute();
    }
    /**
     * 发送消息
     */
    protected void sendMsg() {
        try {
            String socketID = mSocketIDEdt.getText().toString().trim();
            String msg = mMessageEdt.getText().toString().trim();
            JSONObject json = new JSONObject();
            json.put("to", socketID);
            json.put("msg", msg);
            mWriter.write(json.toString()+"\n");
            mWriter.flush();
            mConsoleStr.append("我:" +msg+"   "+getTime(System.currentTimeMillis())+"\n");
            mConsoleTxt.setText(mConsoleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class SocketHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        //将handler中发送过来的消息创建json对象
                        JSONObject json = new JSONObject((String)msg.obj);
                        mConsoleStr.append(json.getString("from")+":" +json.getString("msg")+"   "+getTime(System.currentTimeMillis())+"\n");
                        //将json数据显示在TextView中
                        mConsoleTxt.setText(mConsoleStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartRecieveMsg = false;
    }

    private static String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
