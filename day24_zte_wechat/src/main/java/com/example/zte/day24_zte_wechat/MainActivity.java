package com.example.zte.day24_zte_wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zte.day24_zte_wechat.module.wechat.bean.GetGroupMemberResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.GetGroupResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.UserRelationshipResponse;
import com.example.zte.day24_zte_wechat.utils.CommonUtils;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.NetUtil;
import com.example.zte.day24_zte_wechat.utils.PinyinUtils;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;
import com.example.zte.day24_zte_wechat.view.MyApplication;
import com.example.zte.day24_zte_wechat.view.activity.AddFriendActivity;
import com.example.zte.day24_zte_wechat.view.activity.BaseActivity;
import com.example.zte.day24_zte_wechat.view.fragment.ContactsFragment;
import com.example.zte.day24_zte_wechat.view.fragment.DiscoveryFragment;
import com.example.zte.day24_zte_wechat.view.fragment.MeFragment;
import com.example.zte.day24_zte_wechat.view.fragment.MessageFragment;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Friend;
import com.example.zte.greendao.FriendDao;
import com.example.zte.greendao.GroupMember;
import com.example.zte.greendao.GroupMemberDao;
import com.example.zte.greendao.Groups;
import com.example.zte.greendao.GroupsDao;
import com.example.zte.greendao.WechatDaoSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    @BindView(R.id.activity_main_first_fl)
    FrameLayout mFirstFrame;
    @BindView(R.id.activity_main_first_f2)
    FrameLayout mSecondFrame;
    @BindView(R.id.activity_main_first_f3)
    FrameLayout mThirdFrame;
    @BindView(R.id.activity_main_first_f4)
    FrameLayout mForthFrame;
    @BindView(R.id.activity_main_first)
    RadioButton mFirstRadio;
    @BindView(R.id.activity_main_second)
    RadioButton mSecondRadio;
    @BindView(R.id.activity_main_third)
    RadioButton mThirdRadio;
    @BindView(R.id.activity_main_forth)
    RadioButton mForthRadio;
    @BindView(R.id.activity_main_first_tv)
    TextView mFirstTextView;
    @BindView(R.id.activity_main_second_tv)
    TextView mSecondTextView;
    @BindView(R.id.activity_main_third_tv)
    TextView mThirdTextView;
    @BindView(R.id.activity_main_forth_tv)
    TextView mForthTextView;
    @BindView(R.id.activity_main_viewpager)
    ViewPager mViewPager;
    private List<Fragment> list;
    private ViewPagerAdapter mViewPagerAdapter;
    private ImageView mRightImageView;
    private boolean mHasFetchedFriends = false;
    private boolean mHasFetchedGroups = false;
    private boolean mHasFetchedGroupMembers = false;
    private LocalBroadcastManager mLocalManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgressDialog();
            Toast.makeText(context, "接收到消息，回话信息加载完毕", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //等待全局数据加载完。
        showProgressDialog();
        //广播注册
        mLocalManager = LocalBroadcastManager.getInstance(this);
        mLocalManager.registerReceiver(receiver,new IntentFilter(ConstantsUtil.FETCH_COMPLETE));
        //登录时同步好友信息到本地
        getAllUserInfo();
        initData();
        listener();
        setTextColor();
        mFirstTextView.setTextColor(getResources().getColor(R.color.green0));
        Log.e("111", "onCreate: "+SharePreferenceUtil.getInstance(this).getString("token","") );
        connect(SharePreferenceUtil.getInstance(this).getString("token",""));
    }

    private void getAllUserInfo() {
        if(!NetUtil.isNetworkAvailable(this)){
            return;
        }
        mHasFetchedFriends = false;
        final RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
        retrofitApi.getAllUserRelationship()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserRelationshipResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ConstantsUtil.TAG, "onError: 获取好友信息失败" );
                    }

                    @Override
                    public void onNext(UserRelationshipResponse userRelationshipResponse) {
                        if(userRelationshipResponse != null && userRelationshipResponse.getCode() == 200){
                            List<UserRelationshipResponse.ResultEntity> result = userRelationshipResponse.getResult();
                            if(result != null && result.size() > 0){
                                //每次获取服务器数据后先删除数据库中的类容
                                deleteFriends();
                                //然后将最新数据插入数据库。
                                saveFriend(result);
                                mHasFetchedFriends = true;
                                checkFetchComplete();
                            }
                        }else{
                            mHasFetchedFriends = true;
                            checkFetchComplete();
                        }
                    }
                });

        //获取群组信息
        mHasFetchedGroups = false;
        retrofitApi.getGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetGroupResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ConstantsUtil.TAG, "onError: 获取群组信息失败" );
                    }

                    @Override
                    public void onNext(GetGroupResponse getGroupResponse) {
                        if(getGroupResponse != null && getGroupResponse.getCode() == 200){
                            List<GetGroupResponse.ResultEntity> result = getGroupResponse.getResult();
                            if(result != null && result.size() > 0){
                                //清空数据库中的群组信息
                                deleteGroups();
                                //将最新群组信息同步到数据库
                                saveGroups(result);
                                //同步群组成员信息
                                fetchGroupMembers(retrofitApi);
                            }
                        }
                        mHasFetchedGroupMembers = true;
                        mHasFetchedGroups = true;
                        checkFetchComplete();
                    }
                });

    }

    private void fetchGroupMembers(final RetrofitApi retrofitApi) {
        Observable.from(getGroups())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Groups>() {
                    @Override
                    public void call(final Groups groups) {
                        retrofitApi.getGroupMember(groups.getGroupId())
                                .subscribe(new Observer<GetGroupMemberResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(ConstantsUtil.TAG, "onError: 群组成员信息加载失败" );
                                    }

                                    @Override
                                    public void onNext(GetGroupMemberResponse getGroupMemberResponse) {
                                        if(null != getGroupMemberResponse && getGroupMemberResponse.getCode() == 200){
                                            List<GetGroupMemberResponse.ResultEntity> result = getGroupMemberResponse.getResult();
                                            if(null != result && result.size() > 0){
                                                deleteGroupMember();
                                                saveGroupMember(result,groups.getGroupId());
                                            }
                                        }
                                        mHasFetchedGroupMembers = true;
                                        checkFetchComplete();
                                    }
                                });
                    }
                });

    }

    private void saveGroupMember(List<GetGroupMemberResponse.ResultEntity> result, String groupId) {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupMemberDao groupMemberDao = readableDBSession.getGroupMemberDao();
        List<GroupMember> groupMembers = setCreatedToTop(result, groupId);
        if(null != groupMembers && groupMembers.size() > 0){
            for (int i = 0; i < groupMembers.size(); i++) {
                groupMemberDao.insert(groupMembers.get(i));
            }
        }
    }
    private List<GroupMember> setCreatedToTop(List<GetGroupMemberResponse.ResultEntity> groupMember, String groupId) {
        List<GroupMember> newList = new ArrayList<>();
        GroupMember created = null;
        for (GetGroupMemberResponse.ResultEntity group : groupMember) {
            String groupName = null;
            String groupPortraitUri = null;
            Groups groups = getGroupsById(groupId);
            if (groups != null) {
                groupName = groups.getName();
                groupPortraitUri = groups.getPortraitUri();
            }
            GroupMember newMember = new GroupMember(groupId,
                    group.getUser().getId(),
                    group.getUser().getNickname(),
                    group.getUser().getPortraitUri(),
                    group.getDisplayName(),
                    PinyinUtils.getPinyin(group.getUser().getNickname()),
                    PinyinUtils.getPinyin(group.getDisplayName()),
                    groupName,
                    PinyinUtils.getPinyin(groupName),
                    groupPortraitUri);
            if (group.getRole() == 0) {
                created = newMember;
            } else {
                newList.add(newMember);
            }
        }
        if (created != null) {
            newList.add(created);
        }
        Collections.reverse(newList);
        return newList;
    }

    private Groups getGroupsById(String groupId) {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        List<Groups> groupses = groupsDao.queryRaw("groupid=?", groupId);
        if(null != groupses && groupses.size() > 0){
            return groupses.get(0);
        }
        return null;

    }

    private void deleteGroupMember() {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupMemberDao groupMemberDao = readableDBSession.getGroupMemberDao();
        groupMemberDao.deleteAll();
    }

    private List<Groups> getGroups() {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        return groupsDao.loadAll();

    }

    private void saveGroups(List<GetGroupResponse.ResultEntity> list) {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        for(GetGroupResponse.ResultEntity groups : list){
            String portrait = groups.getGroup().getPortraitUri();
            //如果群组的头像是空的，则使用默认头像
            if (TextUtils.isEmpty(portrait)) {
                portrait = null;
            }
            Groups group = new Groups(groups.getGroup().getId(), groups.getGroup().getName(), portrait, String.valueOf(groups.getRole()));
            groupsDao.insert(group);
        }
    }

    private void deleteGroups() {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        groupsDao.deleteAll();
    }

    private void checkFetchComplete() {
        if (mHasFetchedFriends && mHasFetchedGroups && mHasFetchedGroupMembers) {
            sendBroadcast(ConstantsUtil.FETCH_COMPLETE,"");
            sendBroadcast(ConstantsUtil.UPDATE_FRIEND,"");
            sendBroadcast(ConstantsUtil.UPDATE_GROUP,"");
            sendBroadcast(ConstantsUtil.UPDATE_CONVERSATIONS,"");
        }
    }

    public void sendBroadcast(String action, String s) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("String", s);
        mLocalManager.sendBroadcast(intent);
    }

    private synchronized void saveFriend(List<UserRelationshipResponse.ResultEntity> list) {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        FriendDao friendDao = readableDBSession.getFriendDao();
        int size = list.size();
        for(UserRelationshipResponse.ResultEntity entity : list){
            //等于20表示是好友
            if(entity.getStatus() == 20){
                Friend friend = new Friend(entity.getUser().getId(),
                        entity.getUser().getNickname(),
                        entity.getUser().getPortraitUri(),
                        TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName(),
                        null, null, null, null,
                        PinyinUtils.getPinyin(entity.getUser().getNickname()),
                        PinyinUtils.getPinyin(TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName()));
                friendDao.insert(friend);
            }
        }

    }

    private synchronized void deleteFriends() {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        FriendDao friendDao = readableDBSession.getFriendDao();
        friendDao.deleteAll();
    }

    private void initData() {
        mRightImageView = showRightImageView();
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String appName = getResources().getString(labelRes);

            showTitleView(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        list = new ArrayList<>();
        list.add(MessageFragment.getInstance());
        list.add(DiscoveryFragment.getInstance());
        list.add(ContactsFragment.getInstance());
        list.add(MeFragment.getInstance());
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
    }
    private void connect(String token) {
        Log.e("111", "connect: "+getApplicationInfo().packageName );
        if(getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))){
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.e("111", "--onSuccess" + userid);

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("111", "onError: "+errorCode );
                }
            });
        }
    }

    private void listener() {
        mFirstFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstRadio.setChecked(true);
                setTextColor();
                mFirstTextView.setTextColor(getResources().getColor(R.color.green0));
                mViewPager.setCurrentItem(0);
            }
        });
        mSecondFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSecondRadio.setChecked(true);
                setTextColor();
                mSecondTextView.setTextColor(getResources().getColor(R.color.green0));
                mViewPager.setCurrentItem(1);
            }
        });
        mThirdFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThirdRadio.setChecked(true);
                setTextColor();
                mThirdTextView.setTextColor(getResources().getColor(R.color.green0));
                mViewPager.setCurrentItem(2);
            }
        });
        mForthFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mForthRadio.setChecked(true);
                setTextColor();
                mForthTextView.setTextColor(getResources().getColor(R.color.green0));
                mViewPager.setCurrentItem(3);
            }
        });
        final View view = View.inflate(MainActivity.this, R.layout.menu_main, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //给右边的加号按钮设置点击
        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, CommonUtils.dip2Px(5),CommonUtils.dip2Px(60) + 30);
                //点击发起群聊
                view.findViewById(R.id.tvCreateGroup).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                //点击帮助与反馈
                view.findViewById(R.id.tvHelpFeedback).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                //点击添加朋友
                view.findViewById(R.id.tvAddFriend).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AddFriendActivity.class));
                        popupWindow.dismiss();
                    }
                });
                //点击扫一扫
                view.findViewById(R.id.tvScan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    private void setTextColor() {
        mFirstTextView.setTextColor(getResources().getColor(R.color.gray0));
        mSecondTextView.setTextColor(getResources().getColor(R.color.gray0));
        mThirdTextView.setTextColor(getResources().getColor(R.color.gray0));
        mForthTextView.setTextColor(getResources().getColor(R.color.gray0));
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
