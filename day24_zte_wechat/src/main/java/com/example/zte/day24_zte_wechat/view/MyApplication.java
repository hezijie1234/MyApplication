package com.example.zte.day24_zte_wechat.view;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;

import com.example.zte.day24_zte_wechat.cookie.OkhttpClientSetting;
import com.example.zte.day24_zte_wechat.module.wechat.bean.ContactNotificationData;
import com.example.zte.day24_zte_wechat.module.wechat.bean.GroupNotificationMessageData;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.PinyinUtils;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;
import com.example.zte.day24_zte_wechat.view.activity.LoginActivity;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Friend;
import com.google.gson.Gson;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.GroupNotificationMessage;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.path;
import static com.example.zte.day24_zte_wechat.utils.ConstantsUtil.UPDATE_CONVERSATIONS;
import static com.example.zte.day24_zte_wechat.utils.ConstantsUtil.UPDATE_CURRENT_SESSION;
import static com.example.zte.day24_zte_wechat.utils.ConstantsUtil.UPDATE_CURRENT_SESSION_NAME;
import static com.example.zte.day24_zte_wechat.utils.ConstantsUtil.UPDATE_FRIEND;
import static com.example.zte.day24_zte_wechat.utils.ConstantsUtil.UPDATE_RED_DOT;

/**
 * Created by Administrator on 2017-05-25.
 */

public class MyApplication extends Application implements RongIMClient.OnReceiveMessageListener{
    private static Context context;
    public static Retrofit retrofit;
    private LocalBroadcastManager mBroadManager;
    public static Context getContext() {
        return context;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadManager = LocalBroadcastManager.getInstance(this);
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }
        //监听接收到的消息
        RongIMClient.setOnReceiveMessageListener(this);
        context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkhttpClientSetting.getInstance().getmClient())
                .build();

        //初始化表情控件。
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Picasso.with(context).load(path).centerCrop().into(imageView);
            }
        });
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        Log.e("he", "onReceived:有新消息收到了 " );
        MessageContent content = message.getContent();
        if(content instanceof ContactNotificationMessage){
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) content;
            if(contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)){
                //有好友请求发送过来，更新界面
                sendBroadcast(UPDATE_RED_DOT);
            }else {
                //对方同意我的好友请求。
                ContactNotificationData data = null;
                Gson gson = new Gson();
                data = gson.fromJson(contactNotificationMessage.getExtra(),ContactNotificationData.class);
                if(data != null){
                    //如果对方已经是好友，就不用再次添加
                    if(DBManager.getInstance(this,"wechat").isMyFriend(contactNotificationMessage.getSourceUserId()))
                        return false;
                    //对方还不是我的好友，现在添加到数据库中
                    DBManager.getInstance(this,"wechat").saveFriend(new Friend(contactNotificationMessage.getSourceUserId(),
                            data.getSourceUserNickname(),
                            null,data.getSourceUserNickname(),null,null,null,null,
                            PinyinUtils.getPinyin(data.getSourceUserNickname()),
                            PinyinUtils.getPinyin(data.getSourceUserNickname())));
                    sendBroadcast(UPDATE_FRIEND);
                    sendBroadcast(UPDATE_RED_DOT);
                }

            }
            //群组信息
        }else if (content instanceof GroupNotificationMessage){
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) content;
            String groupId = message.getTargetId();
            GroupNotificationMessageData data = null;
            String selfId = SharePreferenceUtil.getInstance(this).getString("id", "");
            data = new Gson().fromJson(groupNotificationMessage.getData(),GroupNotificationMessageData.class);
            if(groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_CREATE)){
                //接收到创建群组的消息，需要同步
                DBManager.getInstance(this,"wechat").getGroups(groupId);
                DBManager.getInstance(this,"wechat").getGroupMember(groupId);
            }else if(groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_DISMISS)){
                //自己退出的群组消息同步
                handleGroupDismiss(groupId);
            }else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_KICKED)){
                //群组有人被踢消息提醒（包括自己）。
                if(data != null){
                    List<String> targetUserIds = data.getTargetUserIds();
                    if(targetUserIds != null){
                        for (String userid : targetUserIds) {
                            //如果被踢的人是自己
                            if(userid.equals(SharePreferenceUtil.getInstance(getApplicationContext()).getString("id",""))){
                                RongIMClient.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        Log.e(ConstantsUtil.TAG, "onSuccess: 删除一个群组" );
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            }
                        }
                        for (String userId : targetUserIds){
                            //如果被踢的人是别人,就根据groupId和userId删除
                            DBManager.getInstance(this,"wechat").deleteByTwoId(groupId,userId);
                        }
                    }
                }
                //有人被添加进入群组
            }else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_ADD)){
                DBManager.getInstance(this,"wechat").getGroups(groupId);
                DBManager.getInstance(this,"wechat").getGroupMember(groupId);
                //有人离开群组
            }else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_QUIT)){
                if(data != null){
                    List<String> targetUserIds = data.getTargetUserIds();
                    for (String userId : targetUserIds){
                        DBManager.getInstance(this,"wechat").deleteByTwoId(groupId,userId);
                    }
                }
            }else if(groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_RENAME)){
                if(data != null){
                    String targetGroupName = data.getTargetGroupName();
                    //更新群组的名称
                    DBManager.getInstance(this,"wechat").renameGroup(groupId,targetGroupName);
                    sendBroadcast(UPDATE_CURRENT_SESSION_NAME);
                    sendBroadcast(UPDATE_CONVERSATIONS);
                }
            }
            sendBroadcast(ConstantsUtil.UPDATE_CONVERSATIONS);
        }else {
            sendBroadcast(UPDATE_CONVERSATIONS);
            sendBroadcast(ConstantsUtil.UPDATE_CURRENT_SESSION,message);
        }
        return false;
    }

    private void handleGroupDismiss(final String groupId) {
        RongIMClient.getInstance().getConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIMClient.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIMClient.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                DBManager.getInstance(getApplicationContext(),"wechat").deleteGroupById(groupId);
                                DBManager.getInstance(getApplicationContext(),"wechat").deleteGroupMemberById(groupId);
                                sendBroadcast(ConstantsUtil.UPDATE_CONVERSATIONS);
                                sendBroadcast(ConstantsUtil.GROUP_LIST_UPDATE);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }
    private void sendBroadcast(String flag){
        Intent intent = new Intent();
        intent.setAction(flag);
        mBroadManager.sendBroadcast(intent);
    }
    private void sendBroadcast(String flag,Parcelable obj){
        Intent intent = new Intent();
        intent.putExtra("result",obj);
        intent.setAction(flag);
        mBroadManager.sendBroadcast(intent);
    }

}
