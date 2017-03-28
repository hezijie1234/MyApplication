package com.example.retrofit_rxjava_progressbar;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private List<SelectBean.DataBean.ItemsBean> list;
    private TextView textView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_text);
        dialog = new ProgressDialog(this);
        dialog.setTitle("加载信息");
        dialog.setMessage("加载中，请稍后！");
        dialog.show();
        BaseApplication baseApplication = (BaseApplication) getApplicationContext();
        Retrofit retrofit = baseApplication.getRetrofit();
        HttpService httpService = retrofit.create(HttpService.class);
        httpService.querySelectData(101,2,1,2,20,0)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<SelectBean>() {
                    @Override
                    public void call(SelectBean selectBean) {
                        Log.e(TAG, "call: "+Thread.currentThread().getName() );
                        if(selectBean != null){
                            Message message = handler.obtainMessage();
                            message.obj = selectBean;
                            message.sendToTarget();
                        }
                    }
                });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            SelectBean obj = (SelectBean) msg.obj;
            textView.setText(obj.toString());
            Log.e(TAG, "handleMessage: "+obj.getData().getItems().get(1).getImage_url() );
        }
    };
}
