package com.grechur.imdemo.utils;

import android.content.Context;

public class UiUtils {
    /**
     * dip转换px
     */
    public static int dip2px(int dip, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
