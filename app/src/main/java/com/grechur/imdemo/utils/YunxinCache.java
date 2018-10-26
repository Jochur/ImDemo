package com.grechur.imdemo.utils;

import android.content.Context;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

public class YunxinCache {
    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        YunxinCache.account = account;
        NimUIKit.setAccount(account);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        YunxinCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        YunxinCache.context = context.getApplicationContext();

    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        YunxinCache.mainTaskLaunching = mainTaskLaunching;
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }
}
