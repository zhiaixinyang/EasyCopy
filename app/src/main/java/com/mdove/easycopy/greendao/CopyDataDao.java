package com.mdove.easycopy.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mdove.easycopy.greendao.entity.CopyData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COPY_DATA".
*/
public class CopyDataDao extends AbstractDao<CopyData, Long> {

    public static final String TABLENAME = "COPY_DATA";

    /**
     * Properties of entity CopyData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CopyTime = new Property(1, long.class, "copyTime", false, "COPY_TIME");
        public final static Property CopyContent = new Property(2, String.class, "copyContent", false, "COPY_CONTENT");
    }


    public CopyDataDao(DaoConfig config) {
        super(config);
    }
    
    public CopyDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COPY_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"COPY_TIME\" INTEGER NOT NULL ," + // 1: copyTime
                "\"COPY_CONTENT\" TEXT);"); // 2: copyContent
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COPY_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CopyData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getCopyTime());
 
        String copyContent = entity.getCopyContent();
        if (copyContent != null) {
            stmt.bindString(3, copyContent);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CopyData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getCopyTime());
 
        String copyContent = entity.getCopyContent();
        if (copyContent != null) {
            stmt.bindString(3, copyContent);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CopyData readEntity(Cursor cursor, int offset) {
        CopyData entity = new CopyData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // copyTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // copyContent
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CopyData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCopyTime(cursor.getLong(offset + 1));
        entity.setCopyContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CopyData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CopyData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CopyData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
