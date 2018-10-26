package com.grechur.imdemo.utils;

import android.util.Log;

public class LogUtil  {

    private static final String LOG_FILE_NAME_PREFIX = "demo";

    public static void init(String logDir, int level) {


    }

    public static void ui(String msg) {
        Log.i("ui", msg);
    }

    public static void res(String msg) {
        Log.i("res", msg);
    }

    public static void audio(String msg) {
        Log.i("AudioRecorder", msg);
    }
}