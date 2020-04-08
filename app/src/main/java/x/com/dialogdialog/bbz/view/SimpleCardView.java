package x.com.dialogdialog.bbz.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.com.dialogdialog.R;

/**
 * cardview的缩减版，支持圆角、阴影、阴影颜色；若背景是图片，需要自行裁剪圆角
 * Created by L.J.Q on 2018/10/21.
 */
public class SimpleCardView extends FrameLayout {

    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};
    private float mCornerRadius;
    private RoundShadowDrawable mBackground;

    public SimpleCardView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleCardView);
        ColorStateList backgroundColor;
        if (a.hasValue(R.styleable.SimpleCardView_cardBackgroundColor)) {
            backgroundColor = a.getColorStateList(R.styleable.SimpleCardView_cardBackgroundColor);
        } else {
            final TypedArray aa = context.obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            final int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();

            final float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = ColorStateList.valueOf(themeColorBackground);
        }
        mCornerRadius = a.getDimension(R.styleable.SimpleCardView_cardCornerRadius, 0);
        float elevation = a.getDimension(R.styleable.SimpleCardView_cardElevation, 0);
        int startColor = a.getColor(R.styleable.SimpleCardView_cardElevationStartColor, getResources().getColor(R.color.cardview_shadow_start_color));
        int endColor = a.getColor(R.styleable.SimpleCardView_cardElevationEndColor, getResources().getColor(R.color.cardview_shadow_end_color));

        a.recycle();

        if (mCornerRadius > 0 || elevation > 0) {
            mBackground = createBackground(context, backgroundColor, mCornerRadius,
                    elevation, startColor, endColor);
            mBackground.setAddPaddingForCorners(false);
            setBackground(mBackground);
        }
    }

    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        mBackground.setColor(color);
    }

    public ColorStateList getCardBackgroundColor() {
        return mBackground.getColor();
    }

    protected float getCornerRadius() {
        return mCornerRadius;
    }


    private RoundShadowDrawable createBackground(Context context,
                                                 ColorStateList backgroundColor, float radius, float elevation,
                                                 int startColor, int endColor) {
        return new RoundShadowDrawable(context.getResources(), backgroundColor, radius,
                elevation, elevation, startColor, endColor);
    }
}
