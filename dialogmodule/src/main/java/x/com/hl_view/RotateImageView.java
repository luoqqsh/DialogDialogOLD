package x.com.hl_view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import x.com.hl_dialog.R;

/**
 * Created by ChenJiaLiang on 2017/9/14.
 * Email:576507648@qq.com
 */

public class RotateImageView extends AppCompatImageView implements LoadingBaseView {

    private int singleTime;
    private int num;
    private boolean isShun;
    private float singleRotate;
    private boolean isEnable = false;

    public RotateImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setNum(int num) {
        this.num = num;
        singleRotate = 360 * 1.0f / num;
    }

    public void setShun(boolean shun) {
        isShun = shun;
    }

    public void setSingleTime(int singleTime) {
        this.singleTime = singleTime;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    public void start() {
        if (isEnable) {
            return;
        }
        isEnable = true;
        postDelayed(new Runnable() {
            float currentRotate = 0;

            @Override
            public void run() {
                if (!isEnable) return;
                if (isShun) {
                    currentRotate += singleRotate;
                    if (currentRotate >= 360) {
                        currentRotate = 0;
                    }
                } else {
                    currentRotate -= singleRotate;
                    if (currentRotate <= -360) {
                        currentRotate = 0;
                    }
                }
                setRotation(currentRotate);
                postDelayed(this, singleTime);
            }
        }, singleTime);
    }

    public void stop() {
        isEnable = false;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView);
            singleTime = a.getInt(R.styleable.RotateImageView_r_singleTime, 0);
            num = a.getInt(R.styleable.RotateImageView_r_num, 0);
            isShun = a.getBoolean(R.styleable.RotateImageView_r_isShun, true);

            a.recycle();

            if (singleTime > 0 && num > 0) {
                singleRotate = 360 * 1.0f / num;
//                start();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPivotX(w * 1.0f / 2);
        setPivotY(h * 1.0f / 2);//支点在图片中心
    }
}
