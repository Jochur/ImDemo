package com.netease.nim.uikit.business.session.module;

import java.io.Serializable;

public class DisPlay implements Serializable{
    public int id;
    public String funcationName;
    public boolean isShow;

    @Override
    public String toString() {
        return "DisPlay{" +
                "id=" + id +
                ", funcationName='" + funcationName + '\'' +
                ", isShow=" + isShow +
                '}';
    }
}
