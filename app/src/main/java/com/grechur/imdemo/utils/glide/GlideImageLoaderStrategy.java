package com.grechur.imdemo.utils.glide;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grechur.imdemo.R;

public class GlideImageLoaderStrategy implements IBaseImageLoaderStrategy<ImageConfigImpl>{
    @Override
    public void displayImage(Context contex, ImageConfigImpl config) {
        if(contex !=null&&config!=null){
            Glide.with(contex)
                    .load(TextUtils.isEmpty(config.getUrl())?"":config.getUrl())
                    .crossFade()
                    .bitmapTransform(config.getTransformation())
                    .skipMemoryCache(!config.isHasCache())
                    .diskCacheStrategy(config.isHasCache()? DiskCacheStrategy.SOURCE:DiskCacheStrategy.NONE)
                    .placeholder(config.getPlaceHolder())
                    .error(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                    .into(config.getImgView());
        }
    }
}
