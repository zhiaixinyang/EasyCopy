package com.gerenvip.filescaner;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class FileScannerAdapterListener  implements FileScannerListener{

    @Override
    public void onScanBegin() {

    }

    @Override
    public void onScanEnd(@Nullable List<FileInfo> allFiles) {

    }

    @Override
    public void onScanning(String dirPath, int progress) {

    }

    @Override
    public void onScanningChangedFile(FileInfo info, int type) {

    }

    @Override
    public void onScanCancel() {

    }
}
