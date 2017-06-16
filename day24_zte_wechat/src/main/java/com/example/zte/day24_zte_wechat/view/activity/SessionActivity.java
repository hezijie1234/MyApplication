package com.example.zte.day24_zte_wechat.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zte.day24_zte_wechat.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;

/**
 * 表情控件使用方法参考与：http://blog.csdn.net/csdn_lqr/article/details/69668745
 */
public class SessionActivity extends AppCompatActivity {
    public final static int SESSION_TYPE_PRIVATE = 1;
    public final static int SESSION_TYPE_GROUP = 2;
    @BindView(R.id.activity_session_refresh_list)
    PullToRefreshListView mListView;
    @BindView(R.id.activity_session_ivAudio)
    ImageView mAudioImage;
    @BindView(R.id.activity_session_btnAudio)
    Button mAudioBtn;
    @BindView(R.id.activity_session_etContent)
    EditText mEditInput;
    @BindView(R.id.activity_session_ivEmo)
    ImageView mEmotionImage;
    @BindView(R.id.activity_session_ivMore)
    ImageView mAddImage;
    @BindView(R.id.activity_session_emotion)
    EmotionLayout mEmotionLayout;
    @BindView(R.id.activity_session_linear)
    LinearLayout mLinear;
    private EmotionKeyboard mEmotionKeyBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);
        //设置会话已读
//        RongIMClient.getInstance().clearMessagesUnreadStatus(mConversationType, mSessionId);
        initView();
        initData();
        listener();
    }

    private void initView() {
        mEmotionLayout.attachEditText(mEditInput);
        mEmotionKeyBoard = EmotionKeyboard.with(this);
        mEmotionKeyBoard.bindToEditText(mEditInput);
        mEmotionKeyBoard.bindToContent(mLinear);
        mEmotionKeyBoard.setEmotionLayout(mEmotionLayout);
        mEmotionKeyBoard.bindToEmotionButton(mEmotionImage);
    }

    private void listener() {
        //添加表情框下的添加按钮
        mEmotionLayout.setEmotionAddVisiable(true);
        //添加表情框下的设置按钮
        mEmotionLayout.setEmotionSettingVisiable(true);
        //添加按钮和设置按钮的监听。
        mEmotionLayout.setEmotionExtClickListener(new IEmotionExtClickListener() {
            @Override
            public void onEmotionAddClick(View view) {

            }

            @Override
            public void onEmotionSettingClick(View view) {

            }
        });

        mEmotionLayout.setEmotionSelectedListener(new IEmotionSelectedListener() {
            @Override
            public void onEmojiSelected(String key) {

            }

            @Override
            public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {

            }
        });
        mAudioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditInput.isShown()){
                    mEditInput.setVisibility(View.VISIBLE);
                    mEditInput.requestFocus();
                    mAudioBtn.setVisibility(View.GONE);
                    mAudioImage.setImageResource(R.mipmap.ic_cheat_voice);
                }else {
                    mEditInput.setVisibility(View.GONE);
                    mAudioBtn.setVisibility(View.VISIBLE);
                    mAudioImage.setImageResource(R.mipmap.ic_cheat_keyboard);
                }
            }
        });
        mEmotionKeyBoard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                if(mEmotionLayout.isShown()){
                    mEmotionImage.setImageResource(R.mipmap.ic_cheat_emo);
                }else{
                    mEmotionImage.setImageResource(R.mipmap.ic_cheat_keyboard);
                }
                //返回值false正常切换输入法，返回值true拦截切换输入法。
                return false;
            }
        });
    }

    private void initData() {

    }
}
