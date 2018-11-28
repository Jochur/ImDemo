package com.grechur.imdemo.utils.glide;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grechur.imdemo.R;
import com.squareup.picasso.Picasso;

public class PicassoImageLoaderStrategy implements IBaseImageLoaderStrategy<ImageConfigImpl>{
    @Override
    public void displayImage(Context contex, ImageConfigImpl config) {
        if(contex !=null&&config!=null){
            Picasso.with(contex)
                    .load(TextUtils.isEmpty(config.getUrl())?"":config.getUrl())
                    .placeholder(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                    .error(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                    .into(config.getImgView());
        }
    }
}
