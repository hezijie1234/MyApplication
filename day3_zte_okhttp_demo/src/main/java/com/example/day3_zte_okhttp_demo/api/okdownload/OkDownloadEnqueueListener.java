package com.example.day3_zte_okhttp_demo.api.okdownload;

/**
 * Created by succlz123 on 15/9/11.
 */
public interface OkDownloadEnqueueListener {

    void onStart(int id);

    void onProgress(int progress, long cacheSize, long totalSize);

    void onRestart();

    void onPause();

    void onFinish();

    void onCancel();

    void onError(OkDownloadError error);
}
