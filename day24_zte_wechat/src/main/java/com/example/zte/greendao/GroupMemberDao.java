package com.example.zte.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GROUP_MEMBER".
*/
public class GroupMemberDao extends AbstractDao<GroupMember, Long> {

    public static final String TABLENAME = "GROUP_MEMBER";

    /**
     * Properties of entity GroupMember.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property PortraitUri = new Property(3, String.class, "portraitUri", false, "PORTRAIT_URI");
        public final static Property GroupId = new Property(4, String.class, "groupId", false, "GROUP_ID");
        public final static Property DisplayName = new Property(5, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property NameSpelling = new Property(6, String.class, "nameSpelling", false, "NAME_SPELLING");
        public final static Property DisplayNameSpelling = new Property(7, String.class, "displayNameSpelling", false, "DISPLAY_NAME_SPELLING");
        public final static Property GroupName = new Property(8, String.class, "groupName", false, "GROUP_NAME");
        public final static Property GroupNameSpelling = new Property(9, String.class, "groupNameSpelling", false, "GROUP_NAME_SPELLING");
        public final static Property GroupPortrait = new Property(10, String.class, "groupPortrait", false, "GROUP_PORTRAIT");
    }


    public GroupMemberDao(DaoConfig config) {
        super(config);
    }
    
    public GroupMemberDao(DaoConfig config, WechatDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUP_MEMBER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"NAME\" TEXT," + // 2: name
                "\"PORTRAIT_URI\" TEXT," + // 3: portraitUri
                "\"GROUP_ID\" TEXT," + // 4: groupId
                "\"DISPLAY_NAME\" TEXT," + // 5: displayName
                "\"NAME_SPELLING\" TEXT," + // 6: nameSpelling
                "\"DISPLAY_NAME_SPELLING\" TEXT," + // 7: displayNameSpelling
                "\"GROUP_NAME\" TEXT," + // 8: groupName
                "\"GROUP_NAME_SPELLING\" TEXT," + // 9: groupNameSpelling
                "\"GROUP_PORTRAIT\" TEXT);"); // 10: groupPortrait
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUP_MEMBER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GroupMember entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String portraitUri = entity.getPortraitUri();
        if (portraitUri != null) {
            stmt.bindString(4, portraitUri);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(5, groupId);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(6, displayName);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(7, nameSpelling);
        }
 
        String displayNameSpelling = entity.getDisplayNameSpelling();
        if (displayNameSpelling != null) {
            stmt.bindString(8, displayNameSpelling);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(9, groupName);
        }
 
        String groupNameSpelling = entity.getGroupNameSpelling();
        if (groupNameSpelling != null) {
            stmt.bindString(10, groupNameSpelling);
        }
 
        String groupPortrait = entity.getGroupPortrait();
        if (groupPortrait != null) {
            stmt.bindString(11, groupPortrait);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GroupMember entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String portraitUri = entity.getPortraitUri();
        if (portraitUri != null) {
            stmt.bindString(4, portraitUri);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(5, groupId);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(6, displayName);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(7, nameSpelling);
        }
 
        String displayNameSpelling = entity.getDisplayNameSpelling();
        if (displayNameSpelling != null) {
            stmt.bindString(8, displayNameSpelling);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(9, groupName);
        }
 
        String groupNameSpelling = entity.getGroupNameSpelling();
        if (groupNameSpelling != null) {
            stmt.bindString(10, groupNameSpelling);
        }
 
        String groupPortrait = entity.getGroupPortrait();
        if (groupPortrait != null) {
            stmt.bindString(11, groupPortrait);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GroupMember readEntity(Cursor cursor, int offset) {
        GroupMember entity = new GroupMember( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // portraitUri
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // groupId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // displayName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // nameSpelling
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // displayNameSpelling
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // groupName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // groupNameSpelling
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // groupPortrait
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GroupMember entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPortraitUri(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGroupId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDisplayName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNameSpelling(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDisplayNameSpelling(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGroupName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGroupNameSpelling(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGroupPortrait(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GroupMember entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GroupMember entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GroupMember entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}