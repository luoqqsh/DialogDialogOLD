package x.com.dialogdialog.bbz.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/11/21.
 * describe：
 */
public class StepNumView extends AppCompatTextView {
    private ValueAnimator valueAnimator;

    public StepNumView(Context context) {
        super(context);
    }

    public StepNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    private int currentNum = 0;


    public int getCurrentNum() {
        return currentNum;
    }

    /**
     * 设置数据
     */
    public void setStepNum(int num) {
        setCurrentStepNum(num);
        if (onStepChangeListener != null) {
            onStepChangeListener.onStepChange(num);
        }

    }

    /**
     * 设置数据
     * <p>
     * 步数抽取，处理步数ui问题
     */
    private void setCurrentStepNum(int num) {
        currentNum = num;

        if (stepProgressView != null) {
            stepProgressView.setCurrentStep(num);
        }
        if (num >= 100000) {
            setTextSize(45);
        } else {
            setTextSize(48);
        }


        setText(num + "");


    }


    /**
     * 启动动画
     */
    public void startAnimation(int currentNum) {

//        currentNum = 10000;
        //设置uishiyo
        if (onStepChangeListener != null) {
            onStepChangeListener.onStepChange(currentNum);
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }

        valueAnimator = ValueAnimator.ofInt(0, currentNum);
        valueAnimator.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentStepNum((Integer) animation.getAnimatedValue());

//                if (onStepChangeListener != null) {
//                    onStepChangeListener.onStepChange((Integer) animation.getAnimatedValue());
//                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.setStartDelay(600);
        valueAnimator.start();
    }

    StepProgressView stepProgressView;

    public void setProgressView(StepProgressView stepProgress) {

        this.stepProgressView = stepProgress;
    }


    public void setOnStepChangeListener(OnStepChangeListener onStepChangeListener) {
        this.onStepChangeListener = onStepChangeListener;
    }

    OnStepChangeListener onStepChangeListener;

    public interface OnStepChangeListener {

        void onStepChange(int step);
    }
}
