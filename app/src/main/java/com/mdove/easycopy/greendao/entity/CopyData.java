package com.mdove.easycopy.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CopyData {
    @Id(autoincrement = true)
    public long id;
    public long copyTime;
    public String copyContent;

    @Generated(hash = 1716278734)
    public CopyData(long id, long copyTime, String copyContent) {
        this.id = id;
        this.copyTime = copyTime;
        this.copyContent = copyContent;
    }

    @Generated(hash = 457778240)
    public CopyData() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCopyTime() {
        return this.copyTime;
    }

    public void setCopyTime(long copyTime) {
        this.copyTime = copyTime;
    }

    public String getCopyContent() {
        return this.copyContent;
    }

    public void setCopyContent(String copyContent) {
        this.copyContent = copyContent;
    }

}
