package com.example.zte.day21_zte_toolbar_packaging;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-05-13.
 */

public class MyApplication extends Application {

    private static final String TAG = "111";

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                View view = activity.findViewById(R.id.title_bar_title);
                if(activity instanceof MainActivity){
                    if(view != null){
                        ((TextView)view).setText("这是主activity");
                    }
                }
                if(activity instanceof TextActivity){
                    if(view != null){
                        ((TextView)view).setText("这是测试的activity");
                    }
                }
                ImageButton button = (ImageButton) activity.findViewById(R.id.back);
                if(button != null){
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }
}
