package com.example.zte.day10_qq_longin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private Button login, logout;
    private ImageView img;
    private TextView nickName;
    private String name, imgUrl;

    public static  String APPID = "222222";
    public static Tencent mTencent;
    private QQLoginListener mListener;
    private UserInfo userInfo;
    private GetInfoListener mInfoListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTencent = Tencent.createInstance(APPID,getApplicationContext());
        init();
    }

    private void init() {
        img = (ImageView) findViewById(R.id.iv_img);
        nickName = (TextView) findViewById(R.id.tv_nickname);
        login = (Button) findViewById(R.id.btn_login);
        logout = (Button) findViewById(R.id.btn_logout);
        //初始化Tencent对象
        if (mTencent == null) {
            mTencent = Tencent.createInstance(APPID, this);
        }
        //初始化登陆回调Listener
        if (mListener == null) {
            mListener = new QQLoginListener();
        }
        //登陆按钮点击事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQLogin();
            }
        });
        //退出（登出）按钮点击事件
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQLogout();
            }
        });
    }

    /**
     * 登录
     */
    private void QQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", mListener);
        }
    }

    /**
     * 退出（登出）
     */
    private void QQLogout() {
        if (mTencent.isSessionValid()) {
            mTencent.logout(this);
            //修改UI
            img.setImageResource(R.mipmap.ic_launcher);
            nickName.setText("未登录");
        }
    }

    public void share(View view) {
        startActivity(new Intent(this,QQShareActivity.class));
    }

    /**
     * 登陆结果回调
     */
    private class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) { //登录成功
            parseResult(o);
            setUserInfo();
        }

        @Override
        public void onError(UiError uiError) { //登录失败

        }

        @Override
        public void onCancel() { //取消登陆

        }
    }
    /**
     * 解析返回的JsonparseResult串
     */
    private void parseResult(Object o) {
        JSONObject jsonObject = (JSONObject) o;
        try {
            String openID = jsonObject.getString("openid"); //用户标识
            Log.e(TAG, "parseResult: "+openID );
            String access_token = jsonObject.getString("access_token"); //登录信息
            Log.e(TAG, "parseResult: "+access_token );
            String expires = jsonObject.getString("expires_in"); //token有效期
            Log.e(TAG, "parseResult: "+expires );
            //配置token
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 用户信息获取与展示
     */
    private void setUserInfo() {
        QQToken qqToken = mTencent.getQQToken();
        userInfo = new UserInfo(this, qqToken);
        if (mInfoListener == null) {
            mInfoListener = new GetInfoListener();
        }
        userInfo.getUserInfo(mInfoListener);
    }

    /**
     * 获取用户信息回调
     */
    private class GetInfoListener implements IUiListener {

        @Override
        public void onComplete(Object o) { //获取成功，开始展示
            JSONObject jsonObject = (JSONObject) o;
            try {
                Log.e(TAG, "onComplete: "+o.toString() );
                name = jsonObject.getString("nickname");
                imgUrl = jsonObject.getString("figureurl_qq_2");  //头像url（100*100像素）
                nickName.setText(name);
                Picasso.with(MainActivity.this).load(imgUrl).into(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) { //获取失败

        }

        @Override
        public void onCancel() {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }
}
