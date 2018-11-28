package com.grechur.imdemo.utils.glide;

import android.content.Context;

public class ImageLoader<T extends ImageConfig> {
    private static IBaseImageLoaderStrategy mStrategy;

    private static ImageLoader mImageLoader;
    private ImageLoader(){

    }


    public static final ImageLoader getInstance(Class<? extends IBaseImageLoaderStrategy> clazz) {
        if(mImageLoader == null){
            synchronized (ImageLoader.class){
                if(mImageLoader == null){
                    mImageLoader = new ImageLoader();
                }
            }
        }
        try {
            mStrategy = (IBaseImageLoaderStrategy) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mImageLoader;
    }

    public void LoadImage(Context context,T config){
        if(this.mStrategy==null){
            mStrategy = new GlideImageLoaderStrategy();
        }
        this.mStrategy.displayImage(context,config);
    }
}
