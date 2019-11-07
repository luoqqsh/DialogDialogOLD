package x.com.hl_tools;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class hl_floatWindow {
    private static WindowManager windowManager;

    public static void show(Context context, View windowView, boolean isHaveActionInView, String TAG) {
        try {
            if (windowManager == null) {
                windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            }
            windowManager.addView(windowView, getLayoutParams(context, isHaveActionInView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hide(Context context, View windowView, String TAG) {
        try {
            if (windowView == null) return;
            if (windowManager == null) {
                windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            }
            windowManager.removeView(windowView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WindowManager.LayoutParams getLayoutParams(Context context, boolean isHaveActionInView) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置悬浮窗为全屏
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.alpha = 1.0f;
        if (isHaveActionInView) {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        } else {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |//锁屏显示悬浮窗
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        }

        wmParams.width = hl_screen.getScreenWidth(context);
        wmParams.height = hl_screen.getScreenHeight(context) + hl_screen.getDaoHangHeight(context) * 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return wmParams;
    }

}
