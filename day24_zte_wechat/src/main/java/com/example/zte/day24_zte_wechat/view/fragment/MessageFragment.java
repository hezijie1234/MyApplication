package com.example.zte.day24_zte_wechat.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.view.activity.SessionActivity;
import com.example.zte.day24_zte_wechat.view.adapter.SessionAdapter;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.GroupMember;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 *
 */
public class MessageFragment extends BaseFragment {
    private SessionAdapter mSessionAdapter;
    @BindView(R.id.fragment_message_lv)
    PullToRefreshListView mSessionList;
    private LocalBroadcastManager mBroadManager;
    private static MessageFragment fragment;
    private Context context;
    private List<Conversation> mDataList = new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("he", "onReceive: 接收到更新对话信息广播" );
            initData();
        }
    };


    public static MessageFragment getInstance(){
        if(fragment == null){
            synchronized (MessageFragment.class){
                if(fragment == null){
                    fragment = new MessageFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(ConstantsUtil.TAG, "MessageFragment-onAttach: " );
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(ConstantsUtil.TAG, "MessageFragment-onCreateView: " );
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this,view);
        mSessionAdapter = new SessionAdapter(context,mDataList);
        mSessionList.setAdapter(mSessionAdapter);
        mBroadManager = LocalBroadcastManager.getInstance(context);
        mBroadManager.registerReceiver(receiver,new IntentFilter(ConstantsUtil.UPDATE_CONVERSATIONS));
        mSessionList.setMode(PullToRefreshBase.Mode.BOTH);
        listener();
        return view;
    }

    private void listener() {
        mSessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("he", "onItemClick: "+position+"--"+mDataList.size() );
                Conversation conversation = mDataList.get(position - 1);
                Intent intent = new Intent(context, SessionActivity.class);
                intent.putExtra("sessionId",conversation.getTargetId());
                if(conversation.getConversationType() == Conversation.ConversationType.PRIVATE){
                    intent.putExtra("sessionType",SessionActivity.SESSION_TYPE_PRIVATE);
                }else if (conversation.getConversationType() == Conversation.ConversationType.GROUP){
                    intent.putExtra("sessionType",SessionActivity.SESSION_TYPE_GROUP);
                }
                context.startActivity(intent);
            }
        });
    }

    private void initData() {

        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if(mDataList != null && conversations != null){
                    mDataList.clear();
                    mDataList.addAll(conversations);
                    Log.e(ConstantsUtil.TAG, "onSuccess: "+mDataList.size() );
                    filter(mDataList);
                    Log.e(ConstantsUtil.TAG, "onSuccess: "+mDataList.size() );
                    if(mSessionAdapter != null){
                        mSessionAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onDestroy: " );
        mBroadManager.unregisterReceiver(receiver);
    }

    /**建对话列表过滤一下。
     * @param mDataList
     */
    private void filter(List<Conversation> mDataList) {

        if(mDataList.size() > 0){
            for (int i = 0; i < mDataList.size(); i++) {
                Conversation conversation = mDataList.get(i);
                //过滤掉系统消息。
                if(!(conversation.getConversationType() == Conversation.ConversationType.PRIVATE || conversation.getConversationType() == Conversation.ConversationType.GROUP)){
                    mDataList.remove(i);
                    i--;
                    continue;
                }
                //删除没有群成员的群
                if(conversation.getConversationType() == Conversation.ConversationType.GROUP){
//                    List<GroupMember> members = DBManager.getInstance(context, "wechat").getGroupMemberById(conversation.getTargetId());
//                    if(members == null || members.size() == 0){
//                        DBManager.getInstance(context,"wechat").deleteGroupById(conversation.getTargetId());
//                        mDataList.remove(i);
//                        i--;
//                    }
                }else if (conversation.getConversationType() == Conversation.ConversationType.PRIVATE){
                    //如果回话信息是陌生人的，就删除
                    if(!DBManager.getInstance(context,"wechat").isMyFriend(conversation.getTargetId())){
                        mDataList.remove(i);
                        i--;
                    }
                }
          }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "MessageFragment-onCreate: " );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(ConstantsUtil.TAG, "MessageFragment-onActivityCreated: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        //在这里更新数据，是fragment重新加载的时候不会执行oncreateView方法。所以将数据获取放到onResume里面。
        initData();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onResume: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onPause: " );
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onStop: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onDestroyView: " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(ConstantsUtil.TAG, "MessageFragment-onDetach: " );
    }
}
