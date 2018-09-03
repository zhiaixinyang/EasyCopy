package com.mdove.easycopy.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mdove.easycopy.greendao.entity.ResultOCR;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RESULT_OCR".
*/
public class ResultOCRDao extends AbstractDao<ResultOCR, Long> {

    public static final String TABLENAME = "RESULT_OCR";

    /**
     * Properties of entity ResultOCR.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MResultOCRTime = new Property(1, long.class, "mResultOCRTime", false, "M_RESULT_OCRTIME");
        public final static Property MResultOCR = new Property(2, String.class, "mResultOCR", false, "M_RESULT_OCR");
        public final static Property MPath = new Property(3, String.class, "mPath", false, "M_PATH");
        public final static Property MPaths = new Property(4, String.class, "mPaths", false, "M_PATHS");
    }


    public ResultOCRDao(DaoConfig config) {
        super(config);
    }
    
    public ResultOCRDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RESULT_OCR\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"M_RESULT_OCRTIME\" INTEGER NOT NULL ," + // 1: mResultOCRTime
                "\"M_RESULT_OCR\" TEXT," + // 2: mResultOCR
                "\"M_PATH\" TEXT," + // 3: mPath
                "\"M_PATHS\" TEXT);"); // 4: mPaths
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESULT_OCR\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ResultOCR entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getMResultOCRTime());
 
        String mResultOCR = entity.getMResultOCR();
        if (mResultOCR != null) {
            stmt.bindString(3, mResultOCR);
        }
 
        String mPath = entity.getMPath();
        if (mPath != null) {
            stmt.bindString(4, mPath);
        }
 
        String mPaths = entity.getMPaths();
        if (mPaths != null) {
            stmt.bindString(5, mPaths);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ResultOCR entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getMResultOCRTime());
 
        String mResultOCR = entity.getMResultOCR();
        if (mResultOCR != null) {
            stmt.bindString(3, mResultOCR);
        }
 
        String mPath = entity.getMPath();
        if (mPath != null) {
            stmt.bindString(4, mPath);
        }
 
        String mPaths = entity.getMPaths();
        if (mPaths != null) {
            stmt.bindString(5, mPaths);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ResultOCR readEntity(Cursor cursor, int offset) {
        ResultOCR entity = new ResultOCR( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // mResultOCRTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // mResultOCR
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // mPath
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // mPaths
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ResultOCR entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMResultOCRTime(cursor.getLong(offset + 1));
        entity.setMResultOCR(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMPaths(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ResultOCR entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ResultOCR entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ResultOCR entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
