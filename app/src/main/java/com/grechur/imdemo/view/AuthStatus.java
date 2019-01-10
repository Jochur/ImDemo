package com.grechur.imdemo.view;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 授权状态
 */
public class AuthStatus {
    public int status; // 用户该模块的授权状态 0：立即授权（未授权）  1：已授权    2：授权中
    public String dictCode;
    public String dictName;
    public int dictValue; // 若为0，该模块关闭
    public String gotoUrl;
    public String authNoticeInfo;
    public String authingImg; // 授权中图片地址
    public String authSuccessImg; // 授权成功图片地址
    public String noAuthImg;  // 未授权图片地址


}
