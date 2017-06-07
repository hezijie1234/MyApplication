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
 * DAO for table "GROUPS".
*/
public class GroupsDao extends AbstractDao<Groups, Void> {

    public static final String TABLENAME = "GROUPS";

    /**
     * Properties of entity Groups.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property GroupId = new Property(0, String.class, "groupId", false, "GROUP_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property PortraitUri = new Property(2, String.class, "portraitUri", false, "PORTRAIT_URI");
        public final static Property DisplayName = new Property(3, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property Role = new Property(4, String.class, "role", false, "ROLE");
        public final static Property Bulletin = new Property(5, String.class, "bulletin", false, "BULLETIN");
        public final static Property Timestamp = new Property(6, String.class, "timestamp", false, "TIMESTAMP");
        public final static Property NameSpelling = new Property(7, String.class, "nameSpelling", false, "NAME_SPELLING");
    }


    public GroupsDao(DaoConfig config) {
        super(config);
    }
    
    public GroupsDao(DaoConfig config, WechatDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUPS\" (" + //
                "\"GROUP_ID\" TEXT," + // 0: groupId
                "\"NAME\" TEXT," + // 1: name
                "\"PORTRAIT_URI\" TEXT," + // 2: portraitUri
                "\"DISPLAY_NAME\" TEXT," + // 3: displayName
                "\"ROLE\" TEXT," + // 4: role
                "\"BULLETIN\" TEXT," + // 5: bulletin
                "\"TIMESTAMP\" TEXT," + // 6: timestamp
                "\"NAME_SPELLING\" TEXT);"); // 7: nameSpelling
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUPS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Groups entity) {
        stmt.clearBindings();
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(1, groupId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String portraitUri = entity.getPortraitUri();
        if (portraitUri != null) {
            stmt.bindString(3, portraitUri);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(4, displayName);
        }
 
        String role = entity.getRole();
        if (role != null) {
            stmt.bindString(5, role);
        }
 
        String bulletin = entity.getBulletin();
        if (bulletin != null) {
            stmt.bindString(6, bulletin);
        }
 
        String timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindString(7, timestamp);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(8, nameSpelling);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Groups entity) {
        stmt.clearBindings();
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(1, groupId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String portraitUri = entity.getPortraitUri();
        if (portraitUri != null) {
            stmt.bindString(3, portraitUri);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(4, displayName);
        }
 
        String role = entity.getRole();
        if (role != null) {
            stmt.bindString(5, role);
        }
 
        String bulletin = entity.getBulletin();
        if (bulletin != null) {
            stmt.bindString(6, bulletin);
        }
 
        String timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindString(7, timestamp);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(8, nameSpelling);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Groups readEntity(Cursor cursor, int offset) {
        Groups entity = new Groups( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // groupId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // portraitUri
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // displayName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // role
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // bulletin
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // timestamp
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // nameSpelling
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Groups entity, int offset) {
        entity.setGroupId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPortraitUri(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDisplayName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRole(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBulletin(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTimestamp(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setNameSpelling(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Groups entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Groups entity) {
        return null;
    }

    @Override
    public boolean hasKey(Groups entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
