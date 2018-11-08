package com.grechur.imdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LoadingView extends View{
    //轨迹的辅助类
    private PathMeasure mPathMeasure;
    //线的颜色
    private int mColor = Color.RED;
    //画笔
    private Paint mPaint;
    private Path mPath;

    private int mCenterX;

    private int mCenterY;
    //开始的位置
    private float mStartAngle = -105;
    //最大旋转角度
    private float mSweepAngle = -330;
    //弧形旋转的角度
    private float mCurrentAngle ;


    //画图动画
    private ValueAnimator mDrawArcAnimator;
    //旋转动画
    private ValueAnimator mRotateAnimator;
    //消失和进入动画
    private ValueAnimator mFadeAnimator;

    //轨迹动画的path
    private Path mDestPath;
    //圆弧形的长度
    private float mCircleLength;
    //弧形结束位置
    private float mEndD;
    //弧形开始位置
    private float mStartD;

    //不同的阶段
    private LoaingStatus mStatus = LoaingStatus.ARC;
    //梯形总长度
    private float mLength;

    public void reset() {
        if(mDrawArcAnimator!=null&&mFadeAnimator!=null&&mRotateAnimator!=null){
            mDrawArcAnimator.cancel();
            mDrawArcAnimator = null;
            mRotateAnimator.cancel();
            mRotateAnimator = null;
            mFadeAnimator.cancel();
            mFadeAnimator = null;
        }
        mPath.reset();
    }

    public enum LoaingStatus{
        ARC,//画弧形状态
        FADE,//弧形消失
        ENTRE,//开始画梯形
        TRAPEZOID,//开始画梯形里面的弧形
    }

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
        mDestPath = new Path();

        mPathMeasure = new PathMeasure();
//        drawArcAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = (getWidth() - getHeight() / 2) / 2;
        float y = getHeight() / 4;
        RectF oval = new RectF( x, y,
                getWidth() - x, getHeight() - y);
        switch (mStatus){
            case ARC:
                canvas.save();
                mPath.addArc(oval,mStartAngle,mCurrentAngle);
                canvas.drawPath(mPath,mPaint);
                canvas.restore();
                break;
            case FADE:
                mPath.reset();
                mPath.addArc(oval,-75,330);
                canvas.save();
                mPathMeasure.setPath(mPath, false);
                mCircleLength = mPathMeasure.getLength();
                mEndD = mCircleLength;
                mDestPath.reset();
                Log.i("Tag", "sd:" + mStartD + ",ed:" + mEndD);
                mPathMeasure.getSegment(mStartD, mEndD, mDestPath, true);
                canvas.drawPath(mDestPath, mPaint);
                canvas.restore();
                break;
            case ENTRE:
                mPath.reset();
                canvas.save();
//                RectF rectF = new RectF( x, y,
//                        getWidth() - x, getHeight() - y);
//                mPath.addRect(rectF,Path.Direction.CW);
                mPath.moveTo(getWidth()-x-8,y);
                mPath.lineTo(getWidth()-x,getHeight()-y);
                mPath.lineTo(x,getHeight() - y);
                mPath.lineTo(x+8,y);
                mPath.close();

                mPathMeasure.setPath(mPath,false);
                mLength = mPathMeasure.getLength();
                mPathMeasure.getSegment(0,mStartD,mDestPath,true);
                canvas.drawPath(mDestPath,mPaint);
                canvas.restore();
                break;
            case TRAPEZOID:
                canvas.save();
                mPath.moveTo(getWidth()-x-15,y+14);
                mPath.quadTo(getWidth()/2,getHeight()/2,x+15,y+14);

                canvas.drawPath(mPath,mPaint);
                canvas.restore();
                break;
        }

    }

    private void drawArcAnimation(){
        mDrawArcAnimator = ObjectAnimator.ofFloat(0,mSweepAngle);
        mDrawArcAnimator.setDuration(1000);
        mDrawArcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mDrawArcAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rotateAnimation();
            }
        });
        mDrawArcAnimator.start();
    }

    private void rotateAnimation(){
        mRotateAnimator = ObjectAnimator.ofFloat(LoadingView.this,"rotation", -720);
        mRotateAnimator.setDuration(1000);
        mRotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStatus = LoaingStatus.FADE;
                fadeAnimation(0);
            }
        });
        mRotateAnimator.start();
    }
    private void fadeAnimation(final int type){
        mFadeAnimator = ObjectAnimator.ofFloat(0,1);
        mFadeAnimator.setDuration(1000);
        mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(type == 0){
                    mStartD = (float) animation.getAnimatedValue()*mCircleLength;
                }else{
                    mStartD = (float) animation.getAnimatedValue()*mLength;
                }
                invalidate();
            }
        });
        mFadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(type == 0){
                    mStatus = LoaingStatus.ENTRE;
                    fadeAnimation(1);
                }else if(type == 1){
                    mStatus = LoaingStatus.TRAPEZOID;
                }

            }
        });
        mFadeAnimator.start();
    }

    public void startAnimal(){
        mStatus = LoaingStatus.ARC;
        drawArcAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mDrawArcAnimator!=null&&mFadeAnimator!=null&&mRotateAnimator!=null){
            mDrawArcAnimator.cancel();
            mDrawArcAnimator = null;
            mRotateAnimator.cancel();
            mRotateAnimator = null;
            mFadeAnimator.cancel();
            mFadeAnimator = null;
        }
    }
}
