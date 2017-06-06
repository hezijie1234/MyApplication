package com.example.zte.day24_zte_wechat.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zte.day24_zte_wechat.R;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 *
 */
public class MessageFragment extends BaseFragment {

    private static MessageFragment fragment;
    private List<Conversation> mDataList = new ArrayList<>();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initData();

        return view;
    }

    private void initData() {

        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if(mDataList != null && conversations != null){
                    mDataList.clear();
                    mDataList.addAll(conversations);
                    filter(mDataList);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**建对话列表过滤一下。
     * @param mDataList
     */
    private void filter(List<Conversation> mDataList) {
        int size = mDataList.size();
        for (int i = 0; i < size; i++) {
            Conversation conversation = mDataList.get(i);
            //过滤掉系统消息。
            if(!(conversation.getConversationType() == Conversation.ConversationType.PRIVATE || conversation.getConversationType() == Conversation.ConversationType.GROUP)){
                i--;
                return;
            }
            if(conversation.getConversationType() == Conversation.ConversationType.GROUP){

            }

        }
    }
}
