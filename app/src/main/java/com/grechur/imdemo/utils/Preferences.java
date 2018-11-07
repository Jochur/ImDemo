package com.grechur.imdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static  SharedPreferences mSharedPreferences;

    public static void init(Context context){
        mSharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public static void setUserAccount(String account){
        mSharedPreferences.edit().putString("userAccount",account);
    }

    public static String getUserAccount() {
        return mSharedPreferences.getString("userAccount",null);
    }

    public static void setUserToken(String token){
        mSharedPreferences.edit().putString("userToken",token);
    }

    public static String getUserToken() {
        return mSharedPreferences.getString("userToken",null);
    }

    public static void saveUserToken(String s) {
        mSharedPreferences.edit().putString("userToken",s);
    }

    public static void saveCookies(String url,String cookie){
        mSharedPreferences.edit().putString(url,cookie).commit();
    }
    public static String getCookie(String url){
        return mSharedPreferences.getString(url,"");
    }
}
