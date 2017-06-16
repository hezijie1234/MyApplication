package com.example.zte.day24_zte_wechat.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.TimeUtils;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Groups;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by Administrator on 2017-06-13.
 */

public class SessionAdapter extends BaseAdapter {
    private Context context;
    private List<Conversation> mDataList;

    public SessionAdapter(Context context, List<Conversation> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 :mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation conversation = mDataList.get(position);
        ViewHolder holder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recent_message,parent,false);
            holder = new ViewHolder(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if((conversation.getUnreadMessageCount()-7) > 0){
            Log.e("he", "getView: "+conversation.getUnreadMessageCount() +"---"+conversation.getMentionedCount() );
            holder.mDot.setVisibility(View.VISIBLE);
            String count = conversation.getUnreadMessageCount() - 7 + "";
            holder.mDot.setText(count);
        }else{
            holder.mDot.setVisibility(View.GONE);
        }
        if(conversation.getConversationType() == Conversation.ConversationType.PRIVATE){
            UserInfo userInfo = DBManager.getInstance(context, "wechat").getUserInfo(conversation.getTargetId());
            holder.mName.setText(userInfo.getName());
        }else {
            Groups groups = DBManager.getInstance(context, "wechat").getGroupsById(conversation.getTargetId());
            String name = null;
            if(groups != null){
                name = groups.getName();
                if(!TextUtils.isEmpty(groups.getDisplayName())){
                    name = groups.getDisplayName();
                }
                if(!TextUtils.isEmpty(conversation.getDraft())){
                    holder.mDraft.setVisibility(View.VISIBLE);
                    name = conversation.getDraft();
                }else {
                    holder.mDraft.setVisibility(View.GONE);
                }
            }
            holder.mName.setText(name);
        }

        MessageContent messageContent = conversation.getLatestMessage();
        if(messageContent instanceof TextMessage){
            holder.mContent.setText(((TextMessage) messageContent).getContent());
        }else if (messageContent instanceof ImageMessage){
            holder.mContent.setText("[图片]");
        }else if (messageContent instanceof VoiceMessage){
            holder.mContent.setText("[语音]");
        }else if (messageContent instanceof FileMessage){
            holder.mContent.setText("[文件]");
        }else if (messageContent instanceof LocationMessage){
            holder.mContent.setText("[位置]");
        }else if (messageContent instanceof GroupNotificationMessage){

        }
        holder.mTime.setText(TimeUtils.getMsgFormatTime(conversation.getReceivedTime()));
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.item_recent_hotdot)
        TextView mDot;
        @BindView(R.id.item_recent_name)
        TextView mName;
        @BindView(R.id.item_recent_time)
        TextView mTime;
        @BindView(R.id.item_recent_draft)
        TextView mDraft;
        @BindView(R.id.item_recent_content)
        TextView mContent;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
            view.setTag(this);
        }
    }
}
