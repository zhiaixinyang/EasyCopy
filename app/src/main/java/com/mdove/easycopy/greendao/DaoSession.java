package com.mdove.easycopy.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.mdove.easycopy.greendao.entity.CopyData;

import com.mdove.easycopy.greendao.CopyDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig copyDataDaoConfig;

    private final CopyDataDao copyDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        copyDataDaoConfig = daoConfigMap.get(CopyDataDao.class).clone();
        copyDataDaoConfig.initIdentityScope(type);

        copyDataDao = new CopyDataDao(copyDataDaoConfig, this);

        registerDao(CopyData.class, copyDataDao);
    }
    
    public void clear() {
        copyDataDaoConfig.clearIdentityScope();
    }

    public CopyDataDao getCopyDataDao() {
        return copyDataDao;
    }

}
