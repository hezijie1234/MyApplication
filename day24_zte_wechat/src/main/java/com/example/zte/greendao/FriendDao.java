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
 * DAO for table "FRIEND".
*/
public class FriendDao extends AbstractDao<Friend, Void> {

    public static final String TABLENAME = "FRIEND";

    /**
     * Properties of entity Friend.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", false, "USER_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property PortraitUri = new Property(2, String.class, "portraitUri", false, "PORTRAIT_URI");
        public final static Property DisplayName = new Property(3, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property Region = new Property(4, String.class, "region", false, "REGION");
        public final static Property PhoneNumber = new Property(5, String.class, "phoneNumber", false, "PHONE_NUMBER");
        public final static Property Status = new Property(6, String.class, "status", false, "STATUS");
        public final static Property Timestamp = new Property(7, String.class, "timestamp", false, "TIMESTAMP");
        public final static Property NameSpelling = new Property(8, String.class, "nameSpelling", false, "NAME_SPELLING");
        public final static Property DisplayNameSpelling = new Property(9, String.class, "displayNameSpelling", false, "DISPLAY_NAME_SPELLING");
    }


    public FriendDao(DaoConfig config) {
        super(config);
    }
    
    public FriendDao(DaoConfig config, WechatDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FRIEND\" (" + //
                "\"USER_ID\" TEXT," + // 0: userId
                "\"NAME\" TEXT," + // 1: name
                "\"PORTRAIT_URI\" TEXT," + // 2: portraitUri
                "\"DISPLAY_NAME\" TEXT," + // 3: displayName
                "\"REGION\" TEXT," + // 4: region
                "\"PHONE_NUMBER\" TEXT," + // 5: phoneNumber
                "\"STATUS\" TEXT," + // 6: status
                "\"TIMESTAMP\" TEXT," + // 7: timestamp
                "\"NAME_SPELLING\" TEXT," + // 8: nameSpelling
                "\"DISPLAY_NAME_SPELLING\" TEXT);"); // 9: displayNameSpelling
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FRIEND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Friend entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
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
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(5, region);
        }
 
        String phoneNumber = entity.getPhoneNumber();
        if (phoneNumber != null) {
            stmt.bindString(6, phoneNumber);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(7, status);
        }
 
        String timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindString(8, timestamp);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(9, nameSpelling);
        }
 
        String displayNameSpelling = entity.getDisplayNameSpelling();
        if (displayNameSpelling != null) {
            stmt.bindString(10, displayNameSpelling);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Friend entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
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
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(5, region);
        }
 
        String phoneNumber = entity.getPhoneNumber();
        if (phoneNumber != null) {
            stmt.bindString(6, phoneNumber);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(7, status);
        }
 
        String timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindString(8, timestamp);
        }
 
        String nameSpelling = entity.getNameSpelling();
        if (nameSpelling != null) {
            stmt.bindString(9, nameSpelling);
        }
 
        String displayNameSpelling = entity.getDisplayNameSpelling();
        if (displayNameSpelling != null) {
            stmt.bindString(10, displayNameSpelling);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Friend readEntity(Cursor cursor, int offset) {
        Friend entity = new Friend( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // portraitUri
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // displayName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // region
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // phoneNumber
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // status
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // timestamp
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // nameSpelling
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // displayNameSpelling
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Friend entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPortraitUri(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDisplayName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRegion(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPhoneNumber(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setStatus(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTimestamp(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setNameSpelling(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDisplayNameSpelling(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Friend entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Friend entity) {
        return null;
    }

    @Override
    public boolean hasKey(Friend entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
