package com.example.zte.day8_zte_search_listview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017-04-17.
 */

public abstract class CommAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater mInflater;
    protected List<T> mDataList;
    protected final int mLayoutId;

    public CommAdapter(int mLayoutId, List<T> mDataList,  Context context) {
        this.mLayoutId = mLayoutId;
        this.mDataList = mDataList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 :mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(mLayoutId,parent,false);
            holder = new ViewHolder(convertView);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        convert(holder,getItem(position),position);
        return convertView;
    }
    public abstract void convert(ViewHolder holder,T item,int position);

     class ViewHolder{

         private final SparseArray<View> mViews;
         private View convertView;

         public ViewHolder(View convertView){
             this.convertView = convertView;
             convertView.setTag(this);
             mViews = new SparseArray<>();
        }
         @SuppressWarnings("unchecked")
         public  <T extends View> T getView(int viewId) {
             View view = mViews.get(viewId);
             if (view == null) {
                 view = convertView.findViewById(viewId);
                 mViews.put(viewId, view);
             }
             return (T) view;
         }
    }
}
