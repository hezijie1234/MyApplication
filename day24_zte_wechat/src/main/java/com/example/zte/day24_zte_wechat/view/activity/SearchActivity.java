package com.example.zte.day24_zte_wechat.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.module.wechat.bean.FriSearchResponse;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.view.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.activity_search_input_et)
    EditText mEdit;
    @BindView(R.id.activity_search_searchmsg)
    LinearLayout mLinear;
    @BindView(R.id.activity_search_input_tv)
    TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        listener();
        //自动弹出软键盘
        showKeyBoard();
    }

    private void showKeyBoard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEdit,0);
            }
        },500);

    }

    private void listener() {
        //给输入框添加文字变更监听，
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = mEdit.getText().toString().trim();
                if(input.length() > 0){
                    mLinear.setVisibility(View.VISIBLE);
                    mText.setText(input);
                }else {
                    mLinear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //点击这条数据久进行网络请求搜索输入内容
        mLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                //只实现了电话号码查询
                RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
                retrofitApi.searchFriend("86",mEdit.getText().toString().trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<FriSearchResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(ConstantsUtil.TAG, "onError: 查找出现错误" );
                                hideProgressDialog();
                            }

                            @Override
                            public void onNext(FriSearchResponse friSearchResponse) {
                                hideProgressDialog();
                                if(friSearchResponse.getCode() == 200){
                                    FriSearchResponse.ResultEntity result = friSearchResponse.getResult();
                                    UserInfo info = new UserInfo(result.getId(),result.getNickname(), Uri.parse(result.getPortraitUri()));
                                    //跳转到用户信息界面
                                    Intent intent = new Intent(SearchActivity.this,UserInfoActivity.class);
                                    intent.putExtra("userInfo",info);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    public void finishClick(View view) {
        finish();
    }
}
