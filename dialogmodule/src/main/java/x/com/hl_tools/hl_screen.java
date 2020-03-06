package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

import static x.com.hl_tools.hl_context.getAppCompActivity;

public class hl_screen {

    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度px
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }

    /**
     * 获取设备宽度
     */
    public static int getDeviceWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(sizePoint);
            } else {
                display.getSize(sizePoint);
            }
            return sizePoint.x;
        }
        return 0;
    }

    /**
     * 获取设备高度
     */
    public static int getDeviceHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(sizePoint);
            } else {
                display.getSize(sizePoint);
            }
            return sizePoint.y;
        }
        return 0;
    }

    /**
     * 获取导航栏高度(下面虚拟按键)
     *
     * @param context 上下文
     * @return 导航栏高度px
     */
    public static int getDaoHangHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    // 获得状态栏高度
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getStatusBarHeightByReadR(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    //获取屏幕密度dpi
    public static int getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViseLog.d("TAG", "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels);
        return displayMetrics.densityDpi;
    }

    @SuppressLint("RestrictedApi")
    public static void hideSupportActionBar(Context context) {
        if (getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        hl_context.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void showSupportActionBar(Context context) {
        if (getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }
        hl_context.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //屏幕旋转，保持比例不变
    public static int getWidthFromScreenW(Context context) {
        return hl_screen.getScreenWidth(context);
    }

    //获取屏幕角度方向
    public static int getRotation(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    public static int getHeightFromWidth(Context context) {
        return (int) (hl_screen.getScreenWidth(context) * 0.5625);
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar2(Context context) {
        return hl_screen.getDeviceHeight(context) - hl_screen.getScreenHeight(context) > 0;
    }

    /**
     * 判断普通机型是否显示导航栏(不适合刘海屏手机)
     *
     * @param context 上下文
     * @return 是否显示导航栏
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 判断全面屏是否启用虚拟键盘
     * 需要在onCreate方法中加入如下代码方能生效
     * //设置底部导航栏颜色
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     * getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
     * }
     *
     * @param activity 上下文
     * @return 结果
     */
    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0; i < vp.getChildCount(); i++) {
            vp.getChildAt(i).getContext().getPackageName();
            if (vp.getChildAt(i).getId() != -1 && "navigationBarBackground".equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllScreenDevice(Context context) {
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            return height / width >= 1.97f;
        }
        return false;
    }

    /**
     * 获取屏幕相关数据
     *
     * @param context 上下文
     * @return 屏幕参数
     */
    public static String getScreenInfo(Context context) {
        return "屏幕(不含导航栏):" + getScreenWidth(context) + "," + getScreenHeight(context) + "\n" +
                "设备(物理分辨率):" + getDeviceWidth(context) + "," + getDeviceHeight(context) + "\n" +
                "导航栏(下方):" + getDaoHangHeight(context) + "," + (checkDeviceHasNavigationBar(context) ? "显示导航栏" : "不存在(隐藏)导航栏") + "\n" +
                "状态栏(上方):" + getStatusBarHeight(context) + "\n" +
                "是否全面屏手机（纵横比）:" + (isAllScreenDevice(context) ? "是全面屏" : "非全面屏") + "\n" +
                "res分辨率:" + context.getResources().getString(R.string.fenbianlv) + "\n";
    }

    /**
     * 获取Cpu内核数
     * @return
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}