package com.example.zte.day4_zte_vitamio_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private VideoView mVitamioView;
    private MediaController mMediaControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: "+"2" );
        mVitamioView = (VideoView) findViewById(R.id.vitamio);
        mVitamioView.setVideoPath("http://mvvideo1.meitudata.com/57bfff5c69171354.mp4");
//        mMediaControl = new MediaController(this);
//        mMediaControl.show(3000);
//        mVitamioView.setMediaController(mMediaControl);
//        mVitamioView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
//        mVitamioView.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVitamioView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVitamioView.pause();
    }
}
