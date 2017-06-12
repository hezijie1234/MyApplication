package com.example.zte.day24_zte_wechat.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.greendao.Friend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-06-12.
 */

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<Friend> mDataList = new ArrayList<>();

    public ContactsAdapter(Context context, List<Friend> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return null == mDataList ? 0 : mDataList.size();
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
        Friend friend = mDataList.get(position);
        Log.e(ConstantsUtil.TAG, "getView: "+friend.toString());
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
            holder = new ViewHolder(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(friend.getDisplayName());
        if(!TextUtils.isEmpty(friend.getPortraitUri())){
            Picasso.with(context).load(friend.getPortraitUri())
                    .placeholder(R.mipmap.default_header)
                    .error(R.mipmap.default_header)
                    .into(holder.mProtraitIv);
        }
        String currentLetter = friend.getDisplayNameSpelling().charAt(0) + "";
        holder.mIndex.setVisibility(View.GONE);
        if(position == 0){
            holder.mIndex.setText(currentLetter);
            holder.mIndex.setVisibility(View.VISIBLE);
        }else{
            String lastLetter = mDataList.get(position - 1).getDisplayNameSpelling().charAt(0) + "";
            if(!currentLetter.equals(lastLetter)){
                holder.mIndex.setVisibility(View.VISIBLE);
                holder.mIndex.setText(currentLetter);
            }
        }
        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.item_contact_index)
        TextView mIndex;
        @BindView(R.id.item_contact_portrait)
        ImageView mProtraitIv;
        @BindView(R.id.item_contact_name)
        TextView mName;
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
            view.setTag(this);
        }
    }
}
