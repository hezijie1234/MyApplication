package com.example.day3_zte_okhttp_demo.api;

import android.content.Intent;
import android.text.TextUtils;


import com.example.day3_zte_okhttp_demo.LogUtils;
import com.example.day3_zte_okhttp_demo.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser implements CallbackWrapper.OnHttpListener {

    private String flag;
    private OnResponseListener listener;

    public ResultParser(String flag, OnResponseListener listener) {
        this.flag = flag;
        this.listener = listener;
    }

    @Override
    public void onHttpFailure(int code, String error) {
        if (listener != null) {
            LogUtils.i(code + "-" + error);
            listener.onAPIError(flag, code, "网络请求失败");
        }
    }

    @Override
    public void onHttpSuccess(int code, String result) {
        if (listener != null) {
            try {
//                if(code == 200 && !TextUtils.isEmpty(result)){
//                    listener.onAPISuccess(flag, result);
//                }else{
//                    listener.onAPIError(flag, ErrorEvent.DATA_IS_EMPTY, "数据返回失败");
//                }
                if (TextUtils.isEmpty(result)) {
                    listener.onAPIError(flag, ErrorEvent.DATA_IS_EMPTY, "数据为空");
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                String state = jsonObject.optString("code");
                if (Integer.parseInt(state) == 200) {
                    listener.onAPISuccess(flag, jsonObject);
                } else if(Integer.parseInt(state) == 1001){
                    OkHttpManager.getInstance().deleteCookie();
                    //Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    MyApplication.getContext().startActivity(intent);
                } else {
                    String msg = jsonObject.optString("m");
                    listener.onAPIError(flag, Integer.parseInt(state), msg);
                }
            } catch (JSONException e) {
                listener.onAPIError(flag, ErrorEvent.PARSE_DATA_FAILURE, "数据解析失败");
                e.printStackTrace();
            }
        }

    }
}
