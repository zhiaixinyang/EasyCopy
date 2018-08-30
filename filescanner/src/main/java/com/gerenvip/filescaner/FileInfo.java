package com.gerenvip.filescaner;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class FileInfo {
    private int count;
    private String filePath;
    private long fileSize;
    private long lastModifyTime;

    @FileType
    private int type;//1文件夹 ;0 file

    public static final int TYPE_FILE = 0;
    public static final int TYPE_DIR = 1;

    @IntDef(value = {TYPE_FILE, TYPE_DIR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {
    }

    public FileInfo() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @FileType
    public int getType() {
        return type;
    }

    public void setType(@FileType int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "count=" + count +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", lastModifyTime=" + lastModifyTime +
                ", type=" + type +
                '}';
    }
}
