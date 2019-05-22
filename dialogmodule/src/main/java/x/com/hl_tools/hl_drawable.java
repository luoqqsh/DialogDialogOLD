package x.com.hl_tools;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class hl_drawable {
    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        //左上、右上、右下、左下的圆角半径
        float[] radiu = {radius, radius, radius, radius, 20, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radiu);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
