package com.grechur.imdemo.utils.glide;

import android.content.Context;

public interface IBaseImageLoaderStrategy<T extends ImageConfig> {
    //定义供外部调用的显示图片的方法
    void displayImage(Context contex, T config);
}
