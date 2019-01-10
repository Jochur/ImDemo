package com.grechur.imdemo.utils.glide;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ImageLoader<T extends ImageConfig> {
    private static IBaseImageLoaderStrategy mStrategy;

    private static ImageLoader mImageLoader;
    private ImageLoader(){

    }


    public static final ImageLoader getInstance() {
        if(mImageLoader == null){
            synchronized (ImageLoader.class){
                if(mImageLoader == null){
                    mImageLoader = new ImageLoader();
                }
            }
        }

        return mImageLoader;
    }

    public ImageLoader<T> getService(Class<? extends IBaseImageLoaderStrategy> clazz){
        try {
            mStrategy = (IBaseImageLoaderStrategy) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void loadImage(Context context,T config){
        if(this.mStrategy==null){
            mStrategy = new GlideImageLoaderStrategy();
        }
        this.mStrategy.displayImage(context,config);
    }
}
