package com.grechur.imdemo.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.grechur.imdemo.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.CropTransformation;

public class PicassoImageLoaderStrategy implements IBaseImageLoaderStrategy<PicassoImageConfigImpl>{
    @Override
    public void displayImage(Context contex, PicassoImageConfigImpl config) {
        if(contex !=null&&config!=null){
            displayImageDefault(contex, config);
        }
    }

    private void displayImageDefault(Context contex, PicassoImageConfigImpl config){
        Picasso.with(contex)
                .load(TextUtils.isEmpty(config.getUrl())?"":config.getUrl())
                .transform(config.getTransformation() == null?new CropTransformation(0,0,0,0):config.getTransformation())
                .placeholder(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                .error(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                .into(config.getImgView());
    }

    private void displayImageNoTransForm(Context contex, PicassoImageConfigImpl config){
        Picasso.with(contex)
                .load(TextUtils.isEmpty(config.getUrl())?"":config.getUrl())
                .placeholder(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                .error(config.getPlaceHolder() == 0? R.drawable.message_plus_guess_normal:config.getPlaceHolder())
                .into(config.getImgView());
    }
}
