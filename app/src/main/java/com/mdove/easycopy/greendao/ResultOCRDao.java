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
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property MResultOCRTime = new Property(1, long.class, "mResultOCRTime", false, "M_RESULT_OCRTIME");
        public final static Property MResultOCR = new Property(2, String.class, "mResultOCR", false, "M_RESULT_OCR");
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
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"M_RESULT_OCRTIME\" INTEGER NOT NULL ," + // 1: mResultOCRTime
                "\"M_RESULT_OCR\" TEXT);"); // 2: mResultOCR
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESULT_OCR\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ResultOCR entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getMResultOCRTime());
 
        String mResultOCR = entity.getMResultOCR();
        if (mResultOCR != null) {
            stmt.bindString(3, mResultOCR);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ResultOCR entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getMResultOCRTime());
 
        String mResultOCR = entity.getMResultOCR();
        if (mResultOCR != null) {
            stmt.bindString(3, mResultOCR);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public ResultOCR readEntity(Cursor cursor, int offset) {
        ResultOCR entity = new ResultOCR( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // mResultOCRTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // mResultOCR
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ResultOCR entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setMResultOCRTime(cursor.getLong(offset + 1));
        entity.setMResultOCR(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
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
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
