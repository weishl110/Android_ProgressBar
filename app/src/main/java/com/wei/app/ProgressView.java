package com.wei.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by ${wei} on 2017/3/24.
 */

public class ProgressView extends View {

    private String secondColor = "#429AE6";
    private String progressColor = "#F4F4F4";
    private String centerColor = "#336666";
    private String textColor = "#efefef";
    private int progressColor_value, secondColor_value;
    private Paint mPaint;
    private float centerY, centerX;
    private float mRadius;
    private float nomalPadding = 10;
    private RectF rectF;
    //第二角度值，需根据数据计算
    private float sweepAngle;
    private Context context;
    private boolean isSet;

    private float textsize = 30.0f;
    private Paint mTextPaint;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

    }

    private void init(Context context) {
        this.context = context;

        nomalPadding = dp2px(nomalPadding);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //中间文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textsize);
        mTextPaint.setColor(Color.parseColor(textColor));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float width = w - getPaddingLeft() - getPaddingRight() - nomalPadding;
        float height = h - getPaddingTop() - getPaddingBottom() - nomalPadding;
        float minValue = Math.min(width, height);
        float paintWidth = minValue / 12;//画笔的宽度
        centerX = (float) w / 2;
        centerY = (float) h / 2;
        //获取半径
        mRadius = (minValue - paintWidth / 2) / 2;

        //根据宽高计算中间textsize
        textsize = minValue / 7;
        textsize = dp2px(textsize);
        mTextPaint.setTextSize(textsize);

        //第二进度矩阵
        rectF = new RectF();
        rectF.left = (float) w / 2.0f - mRadius;
        rectF.top = (float) h / 2.0f - mRadius;
        rectF.right = rectF.left + mRadius * 2;
        rectF.bottom = rectF.top + mRadius * 2;
        mPaint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外圆
        mPaint.setColor(Color.parseColor(progressColor));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);

        //绘制中间的实心圆
        mPaint.setColor(Color.parseColor(centerColor));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, mRadius - mPaint.getStrokeWidth() / 2, mPaint);

        //绘制第二进度
        mPaint.setColor(Color.parseColor(secondColor));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 270.0f, sweepAngle, false, mPaint);

        //绘制中心的文字
        int percent = (int) (sweepAngle / 360 * 100);
        String text = String.format("%d%s", percent, "%");
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        float startX = centerX - rect.width() / 2;
        float startY = centerY + rect.height() / 2;
        canvas.drawText(text, startX, startY, mTextPaint);
        Log.e(TAG, "onDraw: percent = " + percent);

//        if (isSet && sweepAngle <= 360) {
//            sweepAngle += 0.5f;
//            invalidate();
//        }
    }

    /**
     * 设置数据
     *
     * @param value 0~1  是百分比
     */
    private static final String TAG = "ProgressView";

    public void setData(float value) {
        isSet = true;
        sweepAngle = 0;
        sweepAngle = value * 360;
        invalidate();
        Log.e(TAG, "setData: value = " + value + "   sweepAngle = " + sweepAngle);
    }

    private float dp2px(float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }
}
