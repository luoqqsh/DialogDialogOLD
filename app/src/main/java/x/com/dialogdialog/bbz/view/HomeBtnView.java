package x.com.dialogdialog.bbz.view;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import x.com.dialogdialog.R;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/11/21.
 * describe：
 */
public class HomeBtnView extends AppCompatTextView {
    public HomeBtnView(Context context) {
        super(context);
        initAnimator();
    }

    public HomeBtnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAnimator();
    }

    public HomeBtnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimator();
    }


    ValueAnimator animator;

    private void initAnimator() {
        setBackground(getDefaultSelector());
        animator = ValueAnimator.ofFloat(1f, 0.95f, 1.05f, 1.0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(800);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setScaleX((Float) animation.getAnimatedValue());
                setScaleY((Float) animation.getAnimatedValue());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setScaleX(1f);
                setScaleY(1f);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 设置默认的点击事件图片
     *
     * @return 默认的点击事件图片
     */
    private StateListDrawable getDefaultSelector() {
        //初始化一个空对象
        StateListDrawable statelistDrawable = new StateListDrawable();
        //获取对应的属性值 Android框架自带的属性 attr
        int enable = android.R.attr.state_enabled;
        statelistDrawable.addState(new int[]{enable}, ContextCompat.getDrawable(getContext(), R.mipmap.ic_home_big_btn_enable));
        statelistDrawable.addState(new int[]{-enable}, ContextCompat.getDrawable(getContext(), R.mipmap.ic_home_big_btn_no_enable));
        //没有任何状态时显示的图片，我们给它设置我空集合
        statelistDrawable.addState(new int[]{}, ContextCompat.getDrawable(getContext(), R.mipmap.ic_home_big_btn_enable));
        return statelistDrawable;
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            startAnmator();
        } else {
            stopAnimator();
        }
    }

    public void stopAnimator() {
        if (animator != null) {
            animator.cancel();
        }

    }

    public void startAnmator() {
        if (animator != null) {
            animator.start();
        }
    }
}
