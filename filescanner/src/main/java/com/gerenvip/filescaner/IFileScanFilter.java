package com.gerenvip.filescaner;

import android.support.annotation.WorkerThread;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public interface IFileScanFilter {

    /**
     * 是否拦截指定的目录，如果返回true 不会扫描该目录下的任何文件和子目录。但该文件夹还是会计入数据库
     */

    @WorkerThread
    boolean filterDir(String dirPath);

    /**
     * 是否拦截指定的文件，如果返回true,不会将该文件计入数据库
     */

    @WorkerThread
    boolean filterFile(String filePath);
}
