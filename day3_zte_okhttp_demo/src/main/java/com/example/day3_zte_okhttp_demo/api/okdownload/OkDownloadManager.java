package com.example.day3_zte_okhttp_demo.api.okdownload;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by succlz123 on 15/9/11.
 */
public class OkDownloadManager {

    private static Context sContext;

    private OkHttpClient mOkHttpClient;
//    private OkDownloadRequest mOkDownloadRequest;
//    private OkDownloadEnqueueListener mOkDownloadEnqueueListener;
//    private OkDownloadTask mOkDownloadTask;
    private List<OkDownloadTask> tasks;

    private OkDownloadManager() {
    }

    public static OkDownloadManager getInstance(Context context) {
        if (context == null) {
            return null;
        }
        OkDownloadManager instance = HelpHolder.INSTANCE;

        if (sContext == null) {
            sContext = context.getApplicationContext();
        }

        return instance;
    }

    private static class HelpHolder {
        private static final OkDownloadManager INSTANCE = new OkDownloadManager();
    }

    public void enqueue(OkDownloadRequest okDownloadRequest, OkDownloadEnqueueListener okDownloadEnqueueListener) {
        if (okDownloadRequest == null || okDownloadEnqueueListener == null) {
            return;
        }
        if (okDownloadRequest.getOkHttpClient() != null) {
            mOkHttpClient = okDownloadRequest.getOkHttpClient();
        }

        if (!isRequestValid(okDownloadRequest,okDownloadEnqueueListener)) {
            return;
        }

        OkDownloadTask task = onStart(okDownloadRequest, okDownloadEnqueueListener);

        switch (okDownloadRequest.getStatus()) {
            case OkDownloadStatus.START:
                task.pause();
                break;
            case OkDownloadStatus.PAUSE:
                task.start(okDownloadRequest, okDownloadEnqueueListener);
                break;
            case OkDownloadStatus.FINISH:
                okDownloadEnqueueListener.onError(new OkDownloadError(OkDownloadError.DOWNLOAD_REQUEST_IS_COMPLETE));
                break;
            default:
                break;
        }
    }

    public OkDownloadTask onStart(OkDownloadRequest okDownloadRequest, OkDownloadEnqueueListener listener) {
        if (!isUrlValid(okDownloadRequest.getUrl())) {
            listener.onError(new OkDownloadError(OkDownloadError.DOWNLOAD_URL_OR_FILEPATH_IS_NOT_VALID));
            return null;
        }

        if (tasks == null){
            tasks = new ArrayList<>();
        }

        OkDownloadTask mOkDownloadTask = getDownLoadTaskByRequest(okDownloadRequest);

        if (mOkDownloadTask == null) {
            mOkDownloadTask = new OkDownloadTask(sContext, mOkHttpClient);
            tasks.add(mOkDownloadTask);
        }

        mOkDownloadTask.start(okDownloadRequest, listener);
        return mOkDownloadTask;
    }

    private boolean isRequestValid(OkDownloadRequest okDownloadRequest, OkDownloadEnqueueListener listener) {
        String url = okDownloadRequest.getUrl();
        String filePath = okDownloadRequest.getFilePath();

        if (!isRequestComplete(url, filePath) || !isUrlValid(url)) {
            listener.onError(new OkDownloadError(OkDownloadError.DOWNLOAD_URL_OR_FILEPATH_IS_NOT_VALID));
            return false;
        }
        return true;
    }

    private boolean isRequestComplete(String url, String filePath) {
        return !TextUtils.isEmpty(url) && !TextUtils.isEmpty(filePath);
    }

    private boolean isUrlValid(String url) {
        return URLUtil.isNetworkUrl(url);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableExternalMemorySize() {
        if (hasSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (hasSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    public OkDownloadTask getDownLoadTaskByRequest(OkDownloadRequest request){
        if (tasks!=null && tasks.size()!=0){
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getMyRequest().equals(request)){
                    return tasks.get(i);
                }
            }
        }
        return null;
    }

    public boolean deleteTastByRequest(OkDownloadRequest request){
        if (tasks!=null && tasks.size()!=0){
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getMyRequest().equals(request)){
                    tasks.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

}
