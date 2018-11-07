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

    private final PathMeasure mPathMeasure;
    //圆的半径
    private int mRadiua;
    //线的颜色
    private int mColor = Color.RED;
    //画笔
    private Paint mPaint;
    //
    private Path mPath;

    private int mCenterX;

    private int mCenterY;

    private float mStartAngle = -105;

    private float mSweepAngle = -330;

    private float mCurrentAngle ;


    //画图动画
    private ValueAnimator mDrawAnimator;

    private ValueAnimator mRotateAnimator;

    private ValueAnimator mFadeAnimator;

    private Path mDestPath;
    private float circleLength;
    private float endD;
    private float startD;

    private LoaingStatus mStatus = LoaingStatus.DRAW;

    public enum LoaingStatus{
        DRAW,
        FADE,
        END,
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
        drawAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mCenterX = width/2;
        mCenterY = height/2;
        mRadiua = width/16;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = (getWidth() - getHeight() / 2) / 2;
        float y = getHeight() / 4;
        RectF oval = new RectF( x, y,
                getWidth() - x, getHeight() - y);


        switch (mStatus){
            case DRAW:
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
                circleLength = mPathMeasure.getLength();
                endD = circleLength;
                mDestPath.reset();
                Log.i("Tag", "sd:" + startD + ",ed:" + endD);
                mPathMeasure.getSegment(startD, endD, mDestPath, true);
                canvas.drawPath(mDestPath, mPaint);
                canvas.restore();
                break;
            case END:
                mPath.reset();
                canvas.save();
//                RectF rectF = new RectF( x, y,
//                        getWidth() - x, getHeight() - y);
//                mPath.addRect(rectF,Path.Direction.CW);
                mPath.moveTo(getWidth()-x-8,y);
                mPath.rLineTo();
                mPath.lineTo(getWidth()-x,getHeight()-y);
                mPath.lineTo(x,getHeight() - y);
                mPath.lineTo(x+8,y);
                mPath.close();
                mPath.moveTo(getWidth()-x-15,y+14);
                mPath.quadTo(getWidth()/2,getHeight()/2,x+15,y+14);
                canvas.drawPath(mPath,mPaint);
                canvas.restore();
                break;
        }

    }

    private void drawAnimation(){
        mDrawAnimator = ObjectAnimator.ofFloat(0,mSweepAngle);
        mDrawAnimator.setDuration(1000);
        mDrawAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mDrawAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rotateAnimation();
            }
        });
        mDrawAnimator.start();
    }

    private void rotateAnimation(){
        mRotateAnimator = ObjectAnimator.ofFloat(this,"rotation", -720);
        mRotateAnimator.setDuration(1000);
        mRotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeAnimation();
                mStatus = LoaingStatus.FADE;
            }
        });
        mRotateAnimator.start();
    }
    private void fadeAnimation(){
        mFadeAnimator = ObjectAnimator.ofFloat(0,1);
        mFadeAnimator.setDuration(5000);
        mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startD = (float) animation.getAnimatedValue()*circleLength;
                invalidate();
            }
        });
        mFadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStatus = LoaingStatus.END;
            }
        });
        mFadeAnimator.start();
    }
}
