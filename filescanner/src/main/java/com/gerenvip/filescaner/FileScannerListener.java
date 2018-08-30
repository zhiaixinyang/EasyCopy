package com.gerenvip.filescaner;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public interface FileScannerListener {
    /**
     * 扫描开始
     */
    @WorkerThread
    void onScanBegin();

    /**
     * 扫描结束
     */
    @WorkerThread
    void onScanEnd(@Nullable List<FileInfo> allFiles);

    /**
     * 扫描进行中
     *
     * @param dirPath  文件夹地址
     * @param progress 扫描进度
     */
    @WorkerThread
    void onScanning(String dirPath, int progress);

    /**
     * 扫描进行中，文件的更新,新增的文件和删除的文件
     *
     * @param info
     * @param type {@link FileScanner#SCANNER_TYPE_ADD}：添加；{@link FileScanner#SCANNER_TYPE_DEL}：删除
     */
    @WorkerThread
    void onScanningChangedFile(FileInfo info, @FileScanner.ScannerType int type);

    /**
     * 扫描被强行取消,当调用{@link FileScanner#stop()}后会里面回调。不保证立刻停止扫描任务。新的扫描任务也会排队等待前一个任务停止后才执行
     */
    @WorkerThread
    void onScanCancel();
}
