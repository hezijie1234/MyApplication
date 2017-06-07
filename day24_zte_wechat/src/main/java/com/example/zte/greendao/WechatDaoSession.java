package com.example.zte.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.zte.greendao.Friend;
import com.example.zte.greendao.Groups;
import com.example.zte.greendao.GroupMember;

import com.example.zte.greendao.FriendDao;
import com.example.zte.greendao.GroupsDao;
import com.example.zte.greendao.GroupMemberDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class WechatDaoSession extends AbstractDaoSession {

    private final DaoConfig friendDaoConfig;
    private final DaoConfig groupsDaoConfig;
    private final DaoConfig groupMemberDaoConfig;

    private final FriendDao friendDao;
    private final GroupsDao groupsDao;
    private final GroupMemberDao groupMemberDao;

    public WechatDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        groupsDaoConfig = daoConfigMap.get(GroupsDao.class).clone();
        groupsDaoConfig.initIdentityScope(type);

        groupMemberDaoConfig = daoConfigMap.get(GroupMemberDao.class).clone();
        groupMemberDaoConfig.initIdentityScope(type);

        friendDao = new FriendDao(friendDaoConfig, this);
        groupsDao = new GroupsDao(groupsDaoConfig, this);
        groupMemberDao = new GroupMemberDao(groupMemberDaoConfig, this);

        registerDao(Friend.class, friendDao);
        registerDao(Groups.class, groupsDao);
        registerDao(GroupMember.class, groupMemberDao);
    }
    
    public void clear() {
        friendDaoConfig.clearIdentityScope();
        groupsDaoConfig.clearIdentityScope();
        groupMemberDaoConfig.clearIdentityScope();
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public GroupsDao getGroupsDao() {
        return groupsDao;
    }

    public GroupMemberDao getGroupMemberDao() {
        return groupMemberDao;
    }

}
