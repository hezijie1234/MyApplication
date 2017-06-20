package com.example.zte.day24_zte_wechat.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.Message;

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
        return null;
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

        ProgressBar progressBar;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
            view.setTag(this);

        }
    }
}
