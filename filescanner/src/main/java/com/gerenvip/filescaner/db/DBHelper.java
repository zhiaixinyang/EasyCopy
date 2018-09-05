package com.gerenvip.filescaner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";
    public static final String DB_NAME = "file_scanner";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBFolderHelper.deleteTableSql());
        db.execSQL(DBFolderHelper.createTableSql());

        DBFilesHelper.deleteTable(db);
        DBFilesHelper.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}