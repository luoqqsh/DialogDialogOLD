package x.com.dialogdialog.bbz.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatTextView;

import x.com.hl_log.ViseLog;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/11/21.
 * describe：
 */
public class GoldView extends AppCompatTextView {
    private ValueAnimator valueAnimator;

    public GoldView(Context context) {
        super(context);
    }

    public GoldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void setGold(int currtentTotalgold, int gold1) {


        ViseLog.e("当前金币====== " + currtentTotalgold + "  " + gold1);
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }

        if (gold1 <= 0) {
            return;
        }
        valueAnimator = ValueAnimator.ofInt(currtentTotalgold, currtentTotalgold + gold1);
        valueAnimator.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setGold((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.start();
    }

    public void setGold(int gold) {
        setText(String.valueOf(gold));
    }


}
