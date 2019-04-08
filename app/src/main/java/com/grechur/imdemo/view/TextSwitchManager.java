package com.grechur.imdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.grechur.imdemo.MainActivity;
import com.grechur.imdemo.R;

import java.util.ArrayList;
import java.util.List;

public class TextSwitchManager {
    private MyTextSwitcher mTextSwitcher;
    private Context mContext;
    //文字垂直滚动
    private int index = 0;//textview上下滚动下标
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();

    private Handler mHandler;
    public TextSwitchManager(Context context,MyTextSwitcher textSwitcher,Handler handler) {
        this.mContext = context;
        this.mTextSwitcher = textSwitcher;
        this.mHandler = new Handler(mContext.getMainLooper());
        setTextSwitcher();
    }

    private void setTextSwitcher() {
        mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(mContext);
                textView.setSingleLine();
                textView.setTextSize(10);//字号
                textView.setTextColor(Color.parseColor("#FB090A"));
                textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                textView.setSingleLine();
                textView.setGravity(Gravity.CENTER);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                textView.setLayoutParams(params);
                return textView;
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isFlipping) {
                return;
            }
            index++;
            int size = index%2==0?10:12;
            mTextSwitcher.setTextAndSize(mWarningTextList.get(index % mWarningTextList.size()),size);
            if (index == mWarningTextList.size()) {
                index = 0;
            }
            startFlipping();
        }
    };

    //开启信息轮播
    public void startFlipping() {
        if (mWarningTextList.size() > 1) {
            mHandler.removeCallbacks(runnable);
            isFlipping = true;
            mHandler.postDelayed(runnable, 3000);
        }
    }

    //关闭信息轮播
    public void stopFlipping() {
        if (mWarningTextList.size() > 1) {
            isFlipping = false;
            mHandler.removeCallbacks(runnable);
        }
    }

    //设置数据
    public void setData(List<String> list) {
        if(list!=null&&list.size()>0) {
            mWarningTextList.clear();
            mWarningTextList.addAll(list);
            if (mWarningTextList.size() == 1) {
                mTextSwitcher.setTextAndSize(mWarningTextList.get(0),10);
                index = 0;
            }
            if (mWarningTextList.size() > 1) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String text= mWarningTextList.get(0);
                        if(!TextUtils.isEmpty(text))mTextSwitcher.setTextAndSize(text,10);
                        index = 0;
                    }
                }, 1500);
                mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
                mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
                startFlipping();
            }
        }
    }


}
