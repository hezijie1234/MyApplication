package com.example.zte.day4_zte_jiecaovideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {
private JCVideoPlayerStandard videoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoPlayer = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
        videoPlayer.setUp("http://mvvideo1.meitudata.com/57bfff5c69171354.mp4",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"这是一个测试视频");

    }

    @Override
    public void onBackPressed() {
        if(JCVideoPlayer.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    public void startAc(View view) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.test");
        startActivity(intent);
    }
}
