package com.example.zte.day8_zte_search_listview;

import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.zte.day8_zte_search_listview.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-04-17.
 */

public class CityListAdapter extends CommAdapter<CityBean> implements SectionIndexer {
    private List<CityBean> mList;
    private List<CityBean> mSortList;

    /**
     * @param mLayoutId  listview的item布局id
     * @param mDataList   listview的数据。
     * @param context
     */
    public CityListAdapter(int mLayoutId, List<CityBean> mDataList, Context context) {
        super(mLayoutId, mDataList, context);
        this.mList = mDataList;
        mSortList = new ArrayList<>();
    }

    /**
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        int size = mDataList.size();
        for (int i = 0; i < size; i++) {
            CityBean cityBean = mDataList.get(i);
            if(cityBean.getFirstLetter() == (sectionIndex+'A')){
                return i;
            }
        }
        return -1;
    }

    /**
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        char firstLetter = mDataList.get(position).getFirstLetter();
        if(firstLetter >= 'A' && firstLetter <= 'Z'){
            return firstLetter - 'A';
        }
        return 26;
    }

    @Override
    public void convert(ViewHolder holder, CityBean item, int position) {
        TextView headView = holder.getView(R.id.list_item_head);
        TextView contentView = holder.getView(R.id.list_item_content);
        int i = getSectionForPosition(position);
        if(position == 0 && getPositionForSection(i) == -1){
            headView.setVisibility(View.VISIBLE);
            headView.setText("#");
        }else if (position == getPositionForSection(i)){
            headView.setVisibility(View.VISIBLE);
            char c = (char)('A'+i);
            headView.setText(String.valueOf(c));
        }else{
            headView.setVisibility(View.GONE);
        }
        contentView.setText(item.getName());
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    public void queryData(CharSequence s) {
        for(CityBean cityBean : mList){
            if(cityBean.getName().contains(s)){
                mSortList.add(cityBean);
            }
        }
        mDataList = mSortList;
        notifyDataSetChanged();
    }

    public void resetData(){
        mDataList = mList;
        notifyDataSetChanged();
        mSortList.clear();
    }
}
