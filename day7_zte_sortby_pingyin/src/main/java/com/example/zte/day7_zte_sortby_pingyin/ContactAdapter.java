package com.example.zte.day7_zte_sortby_pingyin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017-04-14.
 */

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<Person> list;

    public ContactAdapter(Context context,List<Person> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
