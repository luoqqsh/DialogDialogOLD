package x.com.hl_tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class hl_activityx {
    private static Stack<Activity> mActivityStack;

    /**
     * 添加一个Activity到堆栈中
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (null == mActivityStack) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 从堆栈中移除指定的Activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 获取顶部的Activity
     *
     * @return
     */
    public static Activity getTopActivity() {
        if (mActivityStack.isEmpty()) {
            return null;
        } else {
            return mActivityStack.get(mActivityStack.size() - 1);
        }
    }

    /**
     * 结束所有的Activity，退出应用
     */
    public static void removeAllActivity() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }
    }

    /**
     * 将一个Fragment添加到Activity中
     *
     * @param fragmentManager fragment管理器
     * @param fragment        需要添加的fragment
     * @param frameId         布局FrameLayout的Id
     */
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId) {
        if (null != fragmentManager && null != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment);
            transaction.commit();
        }
    }

    /**
     * 将一个Fragment添加到Activity中,并添加tag标识
     *
     * @param fragmentManager fragment管理器
     * @param fragment        需要添加的fragment
     * @param frameId         布局FrameLayout的Id
     * @param tag             fragment的唯一tag标识
     * @param addToBackStack  是否添加到栈中，可通过返回键进行切换fragment
     */
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId, String tag, boolean addToBackStack) {
        if (null != fragmentManager && null != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
        }
    }

    /**
     * 对Fragment进行显示隐藏的切换，减少fragment的重复创建
     *
     * @param fragmentManager fragment管理器
     * @param hideFragment    需要隐藏的Fragment
     * @param showFragment    需要显示的Fragment
     * @param frameId         布局FrameLayout的Id
     * @param tag             fragment的唯一tag标识
     */
    public static void switchFragment(FragmentManager fragmentManager, Fragment hideFragment, Fragment showFragment, int frameId, String tag) {
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!showFragment.isAdded()) {
                transaction.hide(hideFragment)
                        .add(frameId, showFragment, tag)
                        .commit();
            } else {
                transaction.hide(hideFragment)
                        .show(showFragment)
                        .commit();
            }
        }
    }

    /**
     * 替换Activity中的Fragment
     *
     * @param fragmentManager fragment管理器
     * @param fragment        需要替换到Activity的Fragment
     * @param frameId         布局FrameLayout的Id
     */
    public static void replaceFragmentFromActivity(FragmentManager fragmentManager, Fragment fragment, int frameId) {
        if (null != fragmentManager && null != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment);
            transaction.commit();
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }

    //获取是否存在NavigationBar
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

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}
