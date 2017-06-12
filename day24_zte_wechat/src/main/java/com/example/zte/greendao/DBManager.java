package com.example.zte.greendao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.zte.day24_zte_wechat.module.wechat.bean.GetGroupInfoResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.GetGroupMemberResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.GetGroupResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.UserRelationshipResponse;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.NetUtil;
import com.example.zte.day24_zte_wechat.utils.PinyinUtils;
import com.example.zte.day24_zte_wechat.utils.RetrofitApi;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;
import com.example.zte.day24_zte_wechat.view.MyApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-06-06.
 */

public class DBManager {
    private Context context;
    private static DBManager manager;
    private MyOpenHelper openHelper;
    private boolean mHasFetchedFriends = false;
    private boolean mHasFetchedGroups = false;
    private boolean mHasFetchedGroupMembers = false;
    private LocalBroadcastManager mLocalManager;
    private DBManager(Context context ,String name){
        openHelper = new MyOpenHelper(context,name);
        this.context = context;
        mLocalManager = LocalBroadcastManager.getInstance(context);
    }
    public static DBManager getInstance(Context context ,String name){
        if(manager == null){
            synchronized (DBManager.class){
                if(manager == null){
                    manager = new DBManager(context,name);
                }
            }
        }
        return manager;
    }
    public WechatDaoSession getReadableDBSession(){
        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        WechatDaoMaster androidDaoMaster = new WechatDaoMaster(readableDatabase);
        return androidDaoMaster.newSession();
    }


    public WechatDaoSession getWriteableDBSession(){
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        WechatDaoMaster androidDaoMaster = new WechatDaoMaster(writableDatabase);
        return androidDaoMaster.newSession();
    }

    public FriendDao getFriendDao(){
        WechatDaoSession readableDBSession = getReadableDBSession();
        return readableDBSession.getFriendDao();
    }

    public GroupsDao getGroupDao(){
        WechatDaoSession readableDBSession = getReadableDBSession();
        return readableDBSession.getGroupsDao();
    }

    public GroupMemberDao getGroupMemberDao(){
        WechatDaoSession readableDBSession = getReadableDBSession();
        return readableDBSession.getGroupMemberDao();
    }
    public boolean isMyFriend(String userId){
        Friend friend = getFriendById(userId);
        if(friend != null){
            return true;
        }
        return false;
    }

    public void saveFriend(Friend friend){
        getFriendDao().insert(friend);
    }

    public Friend getFriendById(String userId){
        if(userId != null){
            FriendDao friendDao = getFriendDao();
            QueryBuilder<Friend> builder = friendDao.queryBuilder();
            List<Friend> list = builder.where(FriendDao.Properties.UserId.eq(userId)).list();
            if(list != null && list.size() > 0){
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 获取所有的联系人和群组信息
     */
    public void getAllUserInfo() {
        if(!NetUtil.isNetworkAvailable(context)){
            return;
        }
        fetchFriend();
        fetchGroup();
    }

    /**获取好友信息
     *
     */
    public void fetchFriend(){
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
                            Log.e(ConstantsUtil.TAG, "从服务器获取数据 "+result.get(0).toString() );
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
    }

    public void fetchGroup(){
        final RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
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

    public void getGroupMember(final String groupId){
        RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
        //群组没有从服务器同步过
        if(!mHasFetchedGroupMembers){
            deleteGroupMember();
            fetchGroupMembers(retrofitApi);
            //群组已经从服务器通不过，只能添加一条群组新消息
        }else{
            retrofitApi.getGroupMember(groupId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GetGroupMemberResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(GetGroupMemberResponse getGroupMemberResponse) {
                            if(getGroupMemberResponse.getCode() == 200){
                                List<GetGroupMemberResponse.ResultEntity> result = getGroupMemberResponse.getResult();
                                if(result != null && result.size() > 0){
                                    deleteGroupMemberById(groupId);
                                    saveGroupMember(result,groupId);
                                }
                            }
                        }
                    });
        }
    }

    /**根据groupid来删除这个群组里面的成员信息
     * @param groupId
     */
    public void deleteGroupMemberById(String groupId) {
        GroupMemberDao groupMemberDao = getGroupMemberDao();
        QueryBuilder<GroupMember> builder = groupMemberDao.queryBuilder();
        List<GroupMember> list = builder.where(GroupMemberDao.Properties.GroupId.eq(groupId)).list();
        int size = list.size();
        for (int i = 0; i <size; i++) {
            groupMemberDao.delete(list.get(size));
        }
    }

    /**
     * 根据groupId和userId删除一个群中的某个人
     */
    public synchronized void deleteByTwoId(String groupId,String userId){
        GroupMemberDao groupMemberDao = getGroupMemberDao();
        QueryBuilder<GroupMember> builder = groupMemberDao.queryBuilder();
        List<GroupMember> list = builder.where(GroupMemberDao.Properties.GroupId.eq(groupId), GroupMemberDao.Properties.UserId.eq(userId)).list();
        for(GroupMember member : list){
            groupMemberDao.delete(member);
        }
    }

    public void getGroups(final String groupId) {
        if (!mHasFetchedGroups) {
            fetchGroup();
        } else {
            RetrofitApi retrofitApi = MyApplication.getRetrofit().create(RetrofitApi.class);
            retrofitApi.getGroupInfo(groupId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GetGroupInfoResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(ConstantsUtil.TAG, "onError: 同步新建的群组失败" );
                        }

                        @Override
                        public void onNext(GetGroupInfoResponse getGroupInfoResponse) {
                            if(getGroupInfoResponse.getCode() == 200){
                                GetGroupInfoResponse.ResultEntity result = getGroupInfoResponse.getResult();
                                if(result != null){
                                    String role = result.getCreatorId().equals(SharePreferenceUtil.getInstance(context).getString("id","")) ? "0" : "1";
                                    Groups groups = new Groups(groupId, result.getName(), result.getPortraitUri(), role);
                                    GroupsDao groupDao = getGroupDao();
                                    groupDao.insert(groups);
                                }
                            }
                        }
                    });
        }
    }

    public void fetchGroupMembers(final RetrofitApi retrofitApi) {
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

    public synchronized void deleteGroupById(String groupId){
        GroupsDao groupDao = getGroupDao();
        groupDao.delete(new Groups(groupId));
    }

    public synchronized void saveGroupMember(List<GetGroupMemberResponse.ResultEntity> result, String groupId) {
        WechatDaoSession readableDBSession = getReadableDBSession();
        GroupMemberDao groupMemberDao = readableDBSession.getGroupMemberDao();
        List<GroupMember> groupMembers = setCreatedToTop(result, groupId);
        if(null != groupMembers && groupMembers.size() > 0){
            for (int i = 0; i < groupMembers.size(); i++) {
                groupMemberDao.insert(groupMembers.get(i));
            }
        }
    }
    public List<GroupMember> setCreatedToTop(List<GetGroupMemberResponse.ResultEntity> groupMember, String groupId) {
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

    public synchronized Groups getGroupsById(String groupId) {
        WechatDaoSession readableDBSession = getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        List<Groups> groupses = groupsDao.queryRaw("groupid=?", groupId);
        if(null != groupses && groupses.size() > 0){
            return groupses.get(0);
        }
        return null;
    }

    public synchronized void deleteGroupMember() {
        WechatDaoSession readableDBSession = getReadableDBSession();
        GroupMemberDao groupMemberDao = readableDBSession.getGroupMemberDao();
        groupMemberDao.deleteAll();
    }

    public synchronized List<Groups> getGroups() {
        WechatDaoSession readableDBSession = getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        return groupsDao.loadAll();

    }

    public synchronized void saveGroups(List<GetGroupResponse.ResultEntity> list) {
        WechatDaoSession readableDBSession = getReadableDBSession();
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

    public synchronized void deleteGroups() {
        WechatDaoSession readableDBSession = getReadableDBSession();
        GroupsDao groupsDao = readableDBSession.getGroupsDao();
        groupsDao.deleteAll();
    }

    public void checkFetchComplete() {
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

    public synchronized void saveFriend(List<UserRelationshipResponse.ResultEntity> list) {
        WechatDaoSession readableDBSession = getReadableDBSession();
        FriendDao friendDao = readableDBSession.getFriendDao();
        int size = list.size();
        for(UserRelationshipResponse.ResultEntity entity : list){
            //等于20表示是好友
            if(entity.getStatus() == 20){
                Friend friend = new Friend(entity.getUser().getId(),
                        entity.getUser().getNickname(),
                        entity.getUser().getPortraitUri(),
                        //从网络获取备注名，如果为空，就使用昵称代替。
                        TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName(),
                        null, null, null, null,
                        PinyinUtils.getPinyin(entity.getUser().getNickname()),
                        PinyinUtils.getPinyin(TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName()));
                friendDao.insert(friend);
            }
        }

    }

    public synchronized void deleteFriends() {
        WechatDaoSession readableDBSession = getReadableDBSession();
        FriendDao friendDao = readableDBSession.getFriendDao();
        friendDao.deleteAll();
    }

    /**给群组重命名
     * @param groupId
     * @param newName
     */
    public synchronized  void renameGroup(String groupId,String newName){
        Groups groupsById = getGroupsById(groupId);
        GroupsDao groupDao = getGroupDao();
        if(groupsById != null){
            groupsById.setName(newName);
            groupDao.update(groupsById);
        }
    }

    public synchronized  List<Friend> getFriend(){
        FriendDao friendDao = getFriendDao();
        return friendDao.loadAll();
    }

}
