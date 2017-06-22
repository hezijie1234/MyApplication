package com.example.zte.day24_zte_wechat.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.view.adapter.SessionItemAdapter;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Groups;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 表情控件使用方法参考与：http://blog.csdn.net/csdn_lqr/article/details/69668745
 */
public class SessionActivity extends BaseActivity {
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

    private LocalBroadcastManager mLocalManager;
    private List<Message> mDataList = new ArrayList<>();

    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;
    private BroadcastReceiver mCurrentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message result = intent.getParcelableExtra("result");
            if(result != null){
                //更新数据以及适配器。
            }
        }
    };
    private String sessionId;
    private SessionItemAdapter sessionItemAdapter;

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
        mLocalManager = LocalBroadcastManager.getInstance(this);
        mEmotionLayout.attachEditText(mEditInput);
        mEmotionKeyBoard = EmotionKeyboard.with(this);
        mEmotionKeyBoard.bindToEditText(mEditInput);
        mEmotionKeyBoard.bindToContent(mLinear);
        mEmotionKeyBoard.setEmotionLayout(mEmotionLayout);
        mEmotionKeyBoard.bindToEmotionButton(mEmotionImage);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sessionItemAdapter = new SessionItemAdapter(mDataList,this);
        mListView.setAdapter(sessionItemAdapter);
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

        //设置下拉加载更多
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getLocalHistoryMessagee();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("sessionId");
        int sessionType = intent.getIntExtra("sessionType",SESSION_TYPE_PRIVATE);
        if(sessionType == SESSION_TYPE_PRIVATE){
            mConversationType = Conversation.ConversationType.PRIVATE;

        }else if(sessionType == SESSION_TYPE_GROUP){
            mConversationType = Conversation.ConversationType.GROUP;
        }

        if(mConversationType == Conversation.ConversationType.PRIVATE){
            UserInfo userInfo = DBManager.getInstance(this, "wechat").getUserInfo(sessionId);
            if(null != userInfo){
                showTitleView(userInfo.getName());
            }
        }else if (mConversationType == Conversation.ConversationType.GROUP){
            Groups groups = DBManager.getInstance(this, "wechat").getGroupsById(sessionId);
            if(null != groups){
                showTitleView(groups.getName());
            }
        }
        //设置会话已读
        RongIMClient.getInstance().clearMessagesUnreadStatus(mConversationType, sessionId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.e("he", "onSuccess:设置回话已读 "+aBoolean );
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("he", "onError: 设置回话已读"+errorCode );
            }
        });
        //注册广播，当在聊天时，有人发送信息过来，及时更新聊天界面
        mLocalManager.registerReceiver(mCurrentReceiver,new IntentFilter(ConstantsUtil.UPDATE_CURRENT_SESSION));
        //加载会话信息。
        getLocalHistoryMessagee();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalManager.unregisterReceiver(mCurrentReceiver);
    }

    public void getLocalHistoryMessagee(){
        int messageId = -1;
        if(mDataList.size() > 0){
            messageId = mDataList.get(0).getMessageId();
        }
        RongIMClient.getInstance().getHistoryMessages(mConversationType, sessionId, messageId, 8, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                //如果刷新列表时复用此代码，可以在此停止刷新
                if(null == messages || messages.size() == 0){
                    getRemoteHistoryMessage();
                }else {
                    mListView.onRefreshComplete();
                    saveMessage(messages);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void saveMessage(List<Message> messages) {
        for(Message message :messages){
            mDataList.add(0,message);
        }
        ListView listView = mListView.getRefreshableView();
        if(mDataList.size() >= messages.size()){
            listView.smoothScrollToPosition(messages.size() - 1);
            sessionItemAdapter.notifyDataSetChanged();
        }
    }

    private void getRemoteHistoryMessage() {
        long dateTime = 0;
        if(mDataList.size() > 0){
            dateTime = mDataList.get(0).getSentTime();
        }
        RongIMClient.getInstance().getRemoteHistoryMessages(mConversationType, sessionId, dateTime, 8, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if(null != messages && messages.size() > 0){
                    mListView.onRefreshComplete();
                    saveMessage(messages);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    ;
}
