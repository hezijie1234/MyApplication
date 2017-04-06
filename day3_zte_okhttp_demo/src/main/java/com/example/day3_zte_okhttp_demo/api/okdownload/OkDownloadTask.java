package com.example.day3_zte_okhttp_demo.api.okdownload;

import android.content.Context;
import android.util.Log;


import com.example.day3_zte_okhttp_demo.api.OkHttpManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by succlz123 on 15/9/11.
 */
public class OkDownloadTask {
    private static final String TAG = "OkDownloadTask";

    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private OkDownloadRequest myRequest;
    private OkDownloadEnqueueListener enqueueListener;

    public OkDownloadTask(Context context, OkHttpClient okHttpClient) {
        if (context != null) {
            mContext = context;
        }
        if (okHttpClient != null) {
            mOkHttpClient = okHttpClient;
        } else {
            mOkHttpClient = OkHttpManager.getInstance().getHttpClient();
        }
    }


    public void start(OkDownloadRequest okDownloadRequest, OkDownloadEnqueueListener listener) {
        if (okDownloadRequest == null) {
            return;
        }

        myRequest = okDownloadRequest;
        enqueueListener = listener;

        final String url = okDownloadRequest.getUrl();
        final String filePath = okDownloadRequest.getFilePath();

        // get to write to the local file length
        // if the length is equals 0 , no such cached file
        // if the length is greater than 0 is already cached file size
        final File file = new File(filePath);
        //if file exist,delete it
//        if (file.exists()){
//            file.delete();
//        }
        final long range = file.length();//本地已经下载文件大小

        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .tag(url)
                .addHeader("User-Agent", "OkDownload")
                .addHeader("Connection", "Keep-Alive");

        if (range > 0) {
            builder.addHeader("Range", "bytes=" + range + "-");
        }

        Request okHttpRequest = builder.build();
        Call call = mOkHttpClient.newCall(okHttpRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getMessage().equals("Canceled")){
                    enqueueListener.onCancel();
                }else{
                    enqueueListener.onError(new OkDownloadError(OkDownloadError.OKHTTP_ONFAILURE));
                }
                Log.v("DownloadTask","===Callback==onFailure====" + e.toString());
                removeFromTaskList();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                // code 2xx
                boolean isSuccessful = response.isSuccessful();
                // code 3xx is url redirect
                boolean isRedirect = response.isRedirect();

                Log.w(TAG, "OkDownload : http status code: " + response.code());
                if (response.code() == 416){//416 线上没有可以下载的部分，即：已经下载完成
                    enqueueListener.onFinish();
                    removeFromTaskList();
                    return;
                }

                if (!isSuccessful && !isRedirect) {
                    enqueueListener.onError(new OkDownloadError(OkDownloadError.OKHTTP_ONRESPONSE_FAIL));
                    removeFromTaskList();
                    return;
                }

                InputStream in = null;
                RandomAccessFile out = null;
                long fileLength = myRequest.getFileSize();

                if (fileLength == 0) {
                    myRequest.setStatus(OkDownloadStatus.START);
                    myRequest.setStartTime(System.currentTimeMillis());

                    if (response.header("Content-Length") != null) {
                        fileLength = Long.valueOf(response.header("Content-Length"));
                        myRequest.setFileSize(fileLength);
                    }
                    enqueueListener.onStart(myRequest.getId());

                } else {
                    switch (myRequest.getStatus()) {
                        case OkDownloadStatus.START:
                            myRequest.setStatus(OkDownloadStatus.PAUSE);
                            break;
                        case OkDownloadStatus.PAUSE:
                            myRequest.setStatus(OkDownloadStatus.START);
                            enqueueListener.onRestart();
                            break;
                        default:
                            break;
                    }
                }

                if (filePath.startsWith("/data/data/")) {
                    if (OkDownloadManager.getAvailableInternalMemorySize() - fileLength < 100 * 1024 * 1024) {
                        enqueueListener.onError(new OkDownloadError(OkDownloadError.ANDROID_MEMORY_SIZE_IS_TOO_LOW));
                        removeFromTaskList();
                        return;
                    }
                } else {
                    if (OkDownloadManager.getAvailableExternalMemorySize() - fileLength < 100 * 1024 * 1024) {
                        enqueueListener.onError(new OkDownloadError(OkDownloadError.ANDROID_MEMORY_SIZE_IS_TOO_LOW));
                        removeFromTaskList();
                        return;
                    }
                }

                byte[] bytes = new byte[2048];
                int len = 0;
                long curSize = 0;

                try {
                    in = new BufferedInputStream(response.body().byteStream());
                    out = new RandomAccessFile(filePath, "rwd");
                    out.seek(range);
                    while ((len = in.read(bytes)) != -1) {
                        out.write(bytes, 0, len);
                        curSize += len;
                        if (fileLength != 0) {
                            long cacheSize = file.length();
                            int progress = (int) (cacheSize * 100 / (fileLength+range));

                            enqueueListener.onProgress(progress, cacheSize, fileLength);
                        }
                    }
                    if (fileLength != 0 && curSize == fileLength) {
                        long finishTime = System.currentTimeMillis();
                        myRequest.setFinishTime(finishTime);
                        myRequest.setFileSize(fileLength);
                        myRequest.setStatus(OkDownloadStatus.FINISH);

                        enqueueListener.onFinish();
                        removeFromTaskList();
                    }
                } catch (IOException e) {

                } finally {
                    try {
                        if (in != null) in.close();
                    } catch (IOException e) {

                    }
                    try {
                        if (out != null) out.close();
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    public void pause() {
//        mOkHttpClient.cancel(myRequest.getUrl());
        OkHttpManager.getInstance().cancelCallsWithTag(myRequest.getTag());

        myRequest.setStatus(OkDownloadStatus.PAUSE);

        enqueueListener.onPause();
    }

    public void cancel() {
        removeFromTaskList();
//        mOkHttpClient.cancel(myRequest.getUrl());
        OkHttpManager.getInstance().cancelCallsWithTag(myRequest.getTag());
        deleteFile(myRequest.getFilePath());
        enqueueListener.onCancel();
    }



    private void deleteFile(String url) {
        File file = new File(url);
        if (file.delete()) {
            Log.w(file.getName(), " is deleted!");
        } else {
            Log.w(file.getName(), " delete operation is failed!");
        }
    }

    public OkDownloadRequest getMyRequest() {
        return myRequest;
    }

    /**
     * 从队列中删除该进程
     * @return boolean
     */
    public boolean removeFromTaskList(){
        return OkDownloadManager.getInstance(mContext).deleteTastByRequest(myRequest);
    }
}