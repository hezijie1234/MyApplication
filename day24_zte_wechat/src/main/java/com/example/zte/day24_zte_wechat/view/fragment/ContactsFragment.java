package com.example.zte.day24_zte_wechat.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.CommonUtils;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.view.activity.UserInfoActivity;
import com.example.zte.day24_zte_wechat.view.adapter.ContactsAdapter;
import com.example.zte.day24_zte_wechat.widget.QuickIndexBar;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2017-06-01.
 */

public class ContactsFragment extends BaseFragment{
    private static ContactsFragment fragment;
    private Context context;
    @BindView(R.id.fragment_contacts_lv)
    ListView mContactsList;
    @BindView(R.id.fragment_contacts_bar)
    QuickIndexBar mBar;
    @BindView(R.id.fragment_contacts_tvLetter)
    TextView mText;
    private ContactsAdapter mAdapter;

    private TextView mFootText;
    private View mHeadView;
    private View mFootView;
    private List<Friend> mDataList = new ArrayList<>();

    public static ContactsFragment getInstance(){
        if(fragment == null){
            synchronized (MessageFragment.class){
                if(fragment == null){
                    fragment = new ContactsFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this,view);
        initView();
        initData();
        listener();
        return view;
    }

    private void listener() {
        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //因为有头部视图，所以需要-1
                Friend friend = mDataList.get(position - 1);
                String name = friend.getName();
                if(!TextUtils.isEmpty(friend.getDisplayName())){
                    name = friend.getDisplayName();
                }
                UserInfo info = new UserInfo(friend.getUserId(),name, Uri.parse(friend.getPortraitUri()));
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userInfo",info);
                context.startActivity(intent);
            }
        });
    }

    private void initData() {
        List<Friend> friends = DBManager.getInstance(context, "wechat").getFriend();
        if(friends != null && friends.size() > 0){
            mDataList.clear();
            mDataList.addAll(friends);
            Log.e(ConstantsUtil.TAG, "initData: "+mDataList.get(0).toString() );
            mFootText.setText(mDataList.size() + "位联系人");
            CommonUtils.sortContacts(mDataList);
            if(mAdapter != null){
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_contacts_head,null);
        mFootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts_foot,null);
        mFootText = (TextView) mFootView.findViewById(R.id.contacts_foot_tv);
        mAdapter = new ContactsAdapter(context,mDataList);
        mContactsList.addHeaderView(mHeadView);
        mContactsList.addFooterView(mFootView);
        mContactsList.setAdapter(mAdapter);
    }
}
