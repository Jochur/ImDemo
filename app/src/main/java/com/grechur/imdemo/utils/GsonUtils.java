package com.grechur.imdemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtils {

    public GsonUtils() {
    }

    public static String createGsonString(Object object) {
        Gson gson = new Gson();
        String gsonString = null;
        try {
            gsonString = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gsonString;
    }

    public static <T> String createArrayToString(List<T> list) {
        Gson gson = new Gson();
        String gsonString = null;
        try {
            gsonString = gson.toJson(list, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gsonString;
    }

    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(gsonString, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     *
     * @param gsonString
     * @param <T> T必须实现serilizable接口
     * @return
     */
    public static <T> T changeGsonToBean(String gsonString) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, new TypeToken<T>() {
        }.getType());
        return t;
    }

    public static <T> List<T> changeGsonToList(String gsonString) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    public static <T> List<T> changeGsonToList(String gsonString,Type type) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, type);
        return list;
    }


    public static <T> List<Map<String, T>> changeGsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list;
        Gson gson = new Gson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
        Map<String, T> map;
        Gson gson = new Gson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

}
