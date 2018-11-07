package com.grechur.imdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SearchView extends View {
    private Matrix mMatrix;
    private Paint mPaint;
    private Path mPath;
    private int mCircleX;
    private int mCircleY;
    private int mRadius = 50;//圆的半径
    private PathMeasure mPathMeasure;
    private float circleLength;
    private float startD;
    private float endD;
    private ValueAnimator mvalueAnimator_rotate;
    private ValueAnimator mvalueAnimator_path;
    private String Tag = "SearchView";
    private Path dstCircle;
    private int lineD = 0;
    private static final int Status_Normal = 1;//正常状态
    private static final int Status_PathAni = 2;//正在进行轨迹动画
    private static final int Status_RotateAni = 3;//正在旋转动画
    private int Status = Status_Normal;
    private int rotateNum;

    public SearchView(Context context) {
        this(context, null);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mMatrix = new Matrix();
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPathMeasure = new PathMeasure();

        dstCircle = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mCircleX = getWidth() / 2;
        mCircleY = getHeight() / 2;
        switch (Status) {
            case Status_Normal:
                drawNormal(canvas);
                break;
            case Status_PathAni:
                drawPathAni(canvas);
                break;
            case Status_RotateAni:
                drawRotateAni(canvas);
                break;
        }

    }



    private void drawNormal(Canvas canvas) {
        mPaint.setColor(Color.GREEN);
        mPath.addCircle(mCircleX, mCircleY, mRadius, Path.Direction.CW);
        canvas.save();
        canvas.drawPath(mPath, mPaint);
        canvas.rotate(45, mCircleX, mCircleY);
        canvas.drawLine(mCircleX + mRadius, mCircleY, mCircleX + mRadius * 2, mCircleY, mPaint);
        canvas.restore();
    }

    private void drawPathAni(Canvas canvas) {
        mPath.addCircle(mCircleX, mCircleY, mRadius, Path.Direction.CW);
        canvas.save();
        canvas.rotate(45, mCircleX, mCircleY);
        mPathMeasure.setPath(mPath, false);
        circleLength = mPathMeasure.getLength();
        endD = circleLength;
        dstCircle.reset();
        Log.i(Tag, "sd:" + startD + ",ed:" + endD);
        mPathMeasure.getSegment(startD, endD, dstCircle, true);
//        mPaint.setColor(Color.RED);
        canvas.drawPath(dstCircle, mPaint);//圆的动画

        canvas.drawLine(mCircleX + mRadius, mCircleY, mCircleX + mRadius * 2 - lineD, mCircleY, mPaint);
        canvas.restore();

    }
    private void drawRotateAni(Canvas canvas) {
        mPath.addCircle(mCircleX, mCircleY, mRadius, Path.Direction.CW);
        canvas.save();
        canvas.rotate(45+rotateNum, mCircleX, mCircleY);
        mPathMeasure.setPath(mPath, false);
        circleLength = mPathMeasure.getLength();
        dstCircle.reset();
        mPathMeasure.getSegment(0, 20, dstCircle, true);
//        mPaint.setColor(Color.RED);
        canvas.drawPath(dstCircle, mPaint);//圆的动画
    }

    private void startPathAni() {
//        if(null==mvalueAnimator){
        if (null != mvalueAnimator_path) {
            mvalueAnimator_path.cancel();
        }
        lineD = 0;
        mvalueAnimator_path = ValueAnimator.ofFloat(0, 1);
        mvalueAnimator_path.setDuration(15000);
        mvalueAnimator_path.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //0-0.8,分割圆，0.8到1 分割手柄
                float fraction = animation.getAnimatedFraction();
                if (fraction <= 0.8f) {
                    startD = circleLength * fraction / 0.7f;

                } else {
                    startD = circleLength;

                    lineD = (int) ((fraction - 0.8) * mRadius / 0.2f);
                }
                if(fraction==1f){
                    lineD = mRadius;
                    Status = Status_RotateAni;
                    postInvalidate();
                    startRotateAni();
                }
                postInvalidate();
            }


        });

        mvalueAnimator_path.start();
//        }
    }
    public void startRotateAni(){
        if (null != mvalueAnimator_rotate) {
            mvalueAnimator_rotate.cancel();
        }
        mvalueAnimator_rotate = ValueAnimator.ofInt(0, 360,0);
        mvalueAnimator_rotate.setDuration(3000);
        mvalueAnimator_rotate.setRepeatMode(ValueAnimator.RESTART);
        mvalueAnimator_rotate.setRepeatCount(-1);
        mvalueAnimator_rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateNum = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        mvalueAnimator_rotate.start();
    }

    public void startSearch() {
        Status = Status_PathAni;
        startPathAni();
        postInvalidate();
    }

    public void stopSearch() {
        Status = Status_Normal;
        if (null != mvalueAnimator_rotate) {
            mvalueAnimator_rotate.cancel();
        }
        if (null != mvalueAnimator_path) {
            mvalueAnimator_path.cancel();
        }
        postInvalidate();
    }

}
