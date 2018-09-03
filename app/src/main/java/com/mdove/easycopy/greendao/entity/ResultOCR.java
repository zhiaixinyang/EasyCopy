package com.mdove.easycopy.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ResultOCR {
    @Id(autoincrement = true)
    public Long id;
    public long mResultOCRTime;
    public String mResultOCR;
    public String mPath;
    public String mPaths;
    @Generated(hash = 1198589439)
    public ResultOCR(Long id, long mResultOCRTime, String mResultOCR, String mPath,
            String mPaths) {
        this.id = id;
        this.mResultOCRTime = mResultOCRTime;
        this.mResultOCR = mResultOCR;
        this.mPath = mPath;
        this.mPaths = mPaths;
    }
    @Generated(hash = 1472249724)
    public ResultOCR() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getMResultOCRTime() {
        return this.mResultOCRTime;
    }
    public void setMResultOCRTime(long mResultOCRTime) {
        this.mResultOCRTime = mResultOCRTime;
    }
    public String getMResultOCR() {
        return this.mResultOCR;
    }
    public void setMResultOCR(String mResultOCR) {
        this.mResultOCR = mResultOCR;
    }
    public String getMPath() {
        return this.mPath;
    }
    public void setMPath(String mPath) {
        this.mPath = mPath;
    }
    public String getMPaths() {
        return this.mPaths;
    }
    public void setMPaths(String mPaths) {
        this.mPaths = mPaths;
    }

}
