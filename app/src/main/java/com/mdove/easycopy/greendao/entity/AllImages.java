package com.mdove.easycopy.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AllImages {
    @Id(autoincrement = true)
    public Long mId;
    public String mPath;
    public int mWidth;
    public int mHeight;
    @Generated(hash = 840864215)
    public AllImages(Long mId, String mPath, int mWidth, int mHeight) {
        this.mId = mId;
        this.mPath = mPath;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }
    @Generated(hash = 1371111867)
    public AllImages() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getMPath() {
        return this.mPath;
    }
    public void setMPath(String mPath) {
        this.mPath = mPath;
    }
    public int getMWidth() {
        return this.mWidth;
    }
    public void setMWidth(int mWidth) {
        this.mWidth = mWidth;
    }
    public int getMHeight() {
        return this.mHeight;
    }
    public void setMHeight(int mHeight) {
        this.mHeight = mHeight;
    }
}
