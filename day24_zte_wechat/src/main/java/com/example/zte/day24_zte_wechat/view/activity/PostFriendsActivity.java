package com.example.zte.day24_zte_wechat.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.module.wechat.bean.FriendInvitationRequest;
import com.example.zte.day24_zte_wechat.module.wechat.bean.FriendInvitationResponse;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.view.MyApplication;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostFriendsActivity extends AppCompatActivity {

    @BindView(R.id.title_bar_send_btn)
    Button mSendBtn;
    @BindView(R.id.activity_post_friends_et)
    EditText mEditText;
    @BindView(R.id.activity_post_friends_ib)
    ImageButton mDeleteBtn;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_friends);
        ButterKnife.bind(this);
        mSendBtn.setVisibility(View.VISIBLE);
        initData();
        listener();
    }

    private void listener() {
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
                Gson gson = new Gson();
                String requestMsg = gson.toJson(new FriendInvitationRequest(mUserId, mEditText.getText().toString()));
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=urf-8"),requestMsg);
                retrofitApi.sendFriendInvitation(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<FriendInvitationResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(ConstantsUtil.TAG, "onError: 好友添加请求发送失败" );
                            }

                            @Override
                            public void onNext(FriendInvitationResponse friendInvitationResponse) {
                                if(friendInvitationResponse.getCode() == 200){
                                    Toast.makeText(PostFriendsActivity.this, "请求发送成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PostFriendsActivity.this, "请求发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void initData() {
        mUserId = getIntent().getStringExtra("userId");
        if(TextUtils.isEmpty(mUserId)){
            finish();
        }
    }
}
