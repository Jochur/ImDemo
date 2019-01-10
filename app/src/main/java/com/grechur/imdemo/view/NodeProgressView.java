package com.grechur.imdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.grechur.imdemo.R;
import com.grechur.imdemo.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class NodeProgressView extends View implements Runnable {
    /**
     * View宽度
     */
    private int viewWidth;
    /**
     * View高度
     */
    private int viewHeight;
    /**
     * 白色空心圆图片集合
     */
    private List<Bitmap> noSelectImgs = new ArrayList();
    /**
     * 蓝色实心圆图片集合
     */
    private List<Bitmap> selectImgs = new ArrayList();

    /**
     * 进度比值       控制蓝色进度条
     */
    private int node = 0;
    /**
     * 节点名称
     */
    private ArrayList<AuthStatus> nodeName = null;
    /**
     * 节点数
     */
    private int count;
    /**
     * view 左右的边距
     */
    private int offX;

    /**
     * 节点文本激活颜色
     */
    private String textColor = "#ed145b";
    /**
     * 节点文本未激活颜色
     */
    private String textColorNotEnabled = "#AAAAAA";
    /**
     * 文本框大小
     */
    private int textSize;

    /**
     * 白色空心进度条宽度
     */
    private int maxProgressWidth;
    private Context appContext;
    private int unselectedImgId;
    private int selectedImgId;
    private Bitmap flagDrawable;
    private int top;
    private int imgWidthHalf;


    @SuppressWarnings("deprecation")
    public NodeProgressView(Context context) {
        super(context);
        appContext = context;
        init();
    }

    public NodeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        appContext = context;
        init();
    }


    public void setTitles(ArrayList<AuthStatus> authList) {
        nodeName = authList;
        count = nodeName==null?0:nodeName.size();
        init();
    }

    /**
     * 以节点数来空值进度条 至少大于1
     */
    public void setProgressByNode(int d) {
        node = d+1;
        invalidate();
    }


    /**
     * 初始化图片资源，和基础数值
     */
    @SuppressWarnings("deprecation")
    private void init() {
        flagDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.list_icon_position);

        if (null != nodeName && nodeName.size() > 0) {
            for (AuthStatus authStatus : nodeName) {
                String dictCode = authStatus.dictCode;
                if (null != dictCode) {
                    switch (dictCode) {
                        case CertificationDispatcher.IDENTIFICATION:
                            unselectedImgId = R.drawable.list_icon_identify_unlighten;
                            selectedImgId = R.drawable.list_icon_identify_lighten;
                            break;
                        case CertificationDispatcher.ZHIMA:
                        case CertificationDispatcher.ADDITIONAL_STEP_ZHIMA:
                            unselectedImgId = R.drawable.list_icon_zhimashouquan_unlighten;
                            selectedImgId = R.drawable.list_icon_zhimashouquan_lighten;
                            break;
                        case CertificationDispatcher.LIVENESS:
                        case CertificationDispatcher.ADDITIONAL_STEP_LIVENESS:
                            unselectedImgId = R.drawable.list_icon_face_unlighten;
                            selectedImgId = R.drawable.list_icon_face_lighten;
                            break;
                        case CertificationDispatcher.JOB:
                        case CertificationDispatcher.ADDITIONAL_STEP_WORK_INFO:
                            unselectedImgId = R.drawable.list_icon_work_unlighten;
                            selectedImgId = R.drawable.list_icon_work_lighten;
                            break;
                        case CertificationDispatcher.BANK_CARD:
                            unselectedImgId = R.drawable.list_icon_card_unlighten;
                            selectedImgId = R.drawable.list_icon_card_lighten;
                            break;
                        default:
                            break;
                    }
                }

                Bitmap unselectedDrawable = BitmapFactory.decodeResource(getResources(), unselectedImgId);
                noSelectImgs.add(unselectedDrawable);
                Bitmap selectedDrawable = BitmapFactory.decodeResource(getResources(), selectedImgId);
                selectImgs.add(selectedDrawable);
            }

        }

        //UI线程初始化数值
        this.post(this);
    }

    @Override
    public void run() {
        //读取View宽度
        viewWidth = NodeProgressView.this.getWidth();
        //读取View高度
        viewHeight = NodeProgressView.this.getHeight();
        offX = UiUtils.dip2px(32, appContext);
        top = UiUtils.dip2px(14, appContext);
        // 进度条宽度计算
        maxProgressWidth = viewWidth - offX * 2;
        imgWidthHalf = UiUtils.dip2px(14, appContext);
        textSize = UiUtils.dip2px(11, appContext);
        //绘制
        invalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //添加认证中的标记图片
        int x1 = maxProgressWidth / (count - 1) * (node - 1) + offX - imgWidthHalf;
        int y1 = top;

        if (node >= count) {
//            drawBitmap(canvas, flagDrawable, 29, 19, x1, y1);
        } else {
            x1 = maxProgressWidth / (count - 1) * node + offX - imgWidthHalf;
//            drawBitmap(canvas, flagDrawable, 29, 19, x1, y1);
        }

        //绘制未选中图标和文本
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);//文字的大小
        for (int i = 0, j = noSelectImgs.size(); i < j; i++) {
            Bitmap noSelectImg = noSelectImgs.get(i);
            int x = maxProgressWidth / (count - 1) * i + offX - imgWidthHalf;//白色圆心的x坐标
            int y = flagDrawable.getHeight() + top;
            drawBitmap(canvas, noSelectImg, 29, 29, x, top);
//            if (i < index) {
//                paint.setColor(Color.parseColor(textColor));
//            } else {
            paint.setColor(Color.parseColor(textColorNotEnabled));
//            }
            if (null != nodeName && nodeName.size() > 0) {
                AuthStatus authStatus = nodeName.get(i);
                String str = authStatus.dictName;
                Rect textBundle = new Rect();
                paint.getTextBounds(str,0,str.length(),textBundle);
                float textWidht = textBundle.width();
                int textHeight = textBundle.height();
                int textTop = UiUtils.dip2px(29, appContext)+ top + UiUtils.dip2px(5, appContext)+textHeight;
                canvas.drawText(str, x + imgWidthHalf - textWidht / 2, textTop, paint);
            }
        }

        /**
         * 画图形之间的线段
         */
        for (int i = 0; i < noSelectImgs.size() - 1; i++) {
            int startLineX = maxProgressWidth / (count - 1) * i + offX + imgWidthHalf;
            int lineY = top + UiUtils.dip2px(29, appContext) / 2;
            int endLineX = maxProgressWidth / (count - 1) * (i + 1) - imgWidthHalf + offX;
            Paint paint2 = new Paint();
            paint2.setColor(Color.parseColor("#F0F0F0"));//设置线的颜色
            paint2.setStrokeWidth(4.0f);//设置线的宽度
            canvas.drawLine(startLineX, lineY, endLineX, lineY, paint2);

        }
         /**
         * 画图形之间的线段
         */
        for (int i = 0; i < (node-1); i++) {
            int startLineX = maxProgressWidth / (count - 1) * i + offX + imgWidthHalf;
            int lineY = top + UiUtils.dip2px(29, appContext) / 2;
            int endLineX = maxProgressWidth / (count - 1) * (i + 1) - imgWidthHalf + offX;
            Paint paint2 = new Paint();
            paint2.setColor(Color.parseColor("#4ACE9C"));//设置线的颜色
            paint2.setStrokeWidth(4.0f);//设置线的宽度
            canvas.drawLine(startLineX, lineY, endLineX, lineY, paint2);

        }

        //绘制选中图片
        for (int i = 0, j = node; i < j; i++) {
            if(selectImgs!=null && selectImgs.size()>0 && i< selectImgs.size()) {
                Bitmap selectImg = selectImgs.get(i);
                int x = maxProgressWidth / (count - 1) * i + offX - imgWidthHalf;//白色圆心的x坐标
                int y = flagDrawable.getHeight() + top;
                drawBitmap(canvas, selectImg, 29, 29, x, top);
            }
        }


    }

    /**
     * 画图
     *
     * @param: flagDrawable要画的图
     * @param: rectW  要画的区域宽度
     * @param: rectH    要画的区域高度
     * @param: left     图距离区域的左边
     * @param: top       图距离区域的右边
     * @param: paint         画笔
     */
    private void drawBitmap(Canvas canvas, Bitmap flagDrawable, int rectW, int rectH, int left, int top) {

        int rectWidth = UiUtils.dip2px(rectW, appContext);
        int rectHeight = UiUtils.dip2px(rectH, appContext);
        Paint paint = new Paint();
        int width = flagDrawable.getWidth();
        int height = flagDrawable.getHeight();

        int rectLeft = (rectWidth - width) / 2;
        int rectTop = (rectHeight - height) / 2;
        Rect rectSrc = new Rect(0, 0, width, height);
        Rect rectDesc = new Rect(rectLeft + left, rectTop + top, width + rectLeft + left, height + rectTop + top);
        canvas.drawBitmap(flagDrawable, rectSrc, rectDesc, paint);
    }


}
