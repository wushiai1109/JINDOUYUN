package com.jindouyun.db.domain;

import org.apache.ibatis.annotations.Delete;

/**
 * @className:
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 17:36
 */

public class StaffVO {

    private Integer id;
    private UserVo user;
    private Short todayStatus;
    private Byte workType;
    private Byte workStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public Short getTodayStatus() {
        return todayStatus;
    }

    public void setTodayStatus(Short todayStatus) {
        this.todayStatus = todayStatus;
    }

    public Byte getWorkType() {
        return workType;
    }

    public void setWorkType(Byte workType) {
        this.workType = workType;
    }

    public Byte getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Byte workStatus) {
        this.workStatus = workStatus;
    }
}
