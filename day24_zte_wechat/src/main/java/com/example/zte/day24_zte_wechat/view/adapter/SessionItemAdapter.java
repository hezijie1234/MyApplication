package com.example.zte.day24_zte_wechat.view.adapter;

import android.content.Context;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.TimeUtils;
import com.example.zte.greendao.DBManager;
import com.lqr.emoji.MoonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2017-06-19.
 */

public class SessionItemAdapter extends BaseAdapter {
    private Context context;
    private List<Message> mDataList;
    public SessionItemAdapter(List<Message> mDataList,Context context){
        this.context = context;
        this.mDataList = mDataList;
    }
    @Override
    public int getCount() {
        return null == mDataList ? 0 :mDataList.size();
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
        Message message = mDataList.get(position);
        long msgTime = 0;
        long preTime = 0;
        Message.MessageDirection messageDirection = message.getMessageDirection();
        ViewHolder holder = null;
        if(messageDirection == Message.MessageDirection.RECEIVE){
            msgTime = message.getReceivedTime();
            preTime = mDataList.get(position - 1).getReceivedTime();
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_text_receiver,parent,false);
                holder = new ViewHolder(convertView);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
        }else{
            msgTime = message.getSentTime();
            preTime = mDataList.get(position - 1).getSentTime();
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_text_send,parent,false);
                holder = new ViewHolder(convertView);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
        }
        UserInfo userInfo = message.getContent().getUserInfo();
        holder.mName.setText(userInfo.getName());
        if((msgTime - preTime) > 300000){
            holder.mTime.setVisibility(View.VISIBLE);
            holder.mTime.setText(TimeUtils.getMsgFormatTime(msgTime));
        }else {
            holder.mTime.setVisibility(View.GONE);
        }
        TextMessage textMessage = (TextMessage)message.getContent();
        //识别表情的文字信息
        MoonUtils.identifyFaceExpression(context,holder.mContent,textMessage.getContent(), ImageSpan.ALIGN_BOTTOM);
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.item_session_tvTime)
        TextView mTime;
        @BindView(R.id.item_session_ivAvatar)
        CircleImageView circleImage;
        @BindView(R.id.item_session_tvName)
        TextView mName;
        @BindView(R.id.item_session_tvText)
        TextView mContent;
        @BindView(R.id.item_session_pbSending)
        ProgressBar progressBar;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
            view.setTag(this);

        }
    }
}
