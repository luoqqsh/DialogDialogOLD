package x.com.dialogdialog.bbz.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import x.com.hl_tools.hl_screen;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/11/20.
 * describe：
 */
public class StepProgressView extends View {


    Paint mPaint;

    //进度条颜色值
    private int progressColor;


    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    private int maxStep = 6000;
    //当前步数
    private int currentStep = 0;

    int mProgressWidth = 10;

    RectF rectF;

    public StepProgressView(Context context) {
        super(context);
        init();
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    float centX, centY;

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rectF = new RectF();
        progressColor = Color.parseColor("#FFFFDB4D");
        mProgressWidth = hl_screen.dp2px(getContext(), mProgressWidth);
        centY = mProgressWidth / 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int padding = mProgressWidth / 2;
        rectF.set(padding, padding, getMeasuredWidth() - padding, getMeasuredHeight() - padding);
        centX = getMeasuredWidth() / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mPaint.setColor(mart_2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setColor(progressColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, -90, getCurrentValue(currentStep), false, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centX, centY, hl_screen.dp2px(getContext(), 2), mPaint);
    }


    private float getCurrentValue(int step) {
        return (360 * step * 1f) / maxStep;

    }

    /**
     * 设置当前步数
     */
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;

        invalidate();
    }

    /**
     * 设置刻度的最大值
     */
    public void setMaxStep(int maxStep) {

        if (maxStep <= 0) {
            return;
        }
        this.maxStep = maxStep;
    }
}
