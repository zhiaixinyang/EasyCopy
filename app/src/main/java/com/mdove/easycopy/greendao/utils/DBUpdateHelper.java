package com.mdove.easycopy.greendao.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.mdove.easycopy.greendao.CopyDataDao;
import com.mdove.easycopy.greendao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by MDove on 2018/2/10.
 */

public class DBUpdateHelper extends DaoMaster.OpenHelper {
    public DBUpdateHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, CopyDataDao.class);
    }
}
