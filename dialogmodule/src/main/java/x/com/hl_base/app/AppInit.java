package x.com.hl_base.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_log.inner.LogcatTree;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_screen;

public class AppInit {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static boolean isDebug = false;
    private static volatile Application sApplication;

    public static void setContext(Context mContext) {
        AppInit.mContext = mContext.getApplicationContext();
    }

    public static Context getAppContext() {
        if (mContext == null) {
            ViseLog.e("context初始化异常");
        }
        return mContext.getApplicationContext();
    }

    public static Application getApplicationByInvoke() {
        if (sApplication == null) {
            synchronized (AppInit.class) {
                if (sApplication == null) {
                    try {
                        sApplication = (Application) Class.forName("android.app.ActivityThread")
                                .getMethod("currentApplication")
                                .invoke(null, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sApplication;
    }

    /**
     * 输出设备信息，有init可不传context
     */
    public static void printInformation() {
        printInformation(mContext);
    }

    public static void printInformation(Context context) {
        ViseLog.d(hl_screen.getScreenInfo(context));
        ViseLog.d(hl_device.INSTANCE.getDeviceInfo(context));
//        ViseLog.d(hl_device.INSTANCE.getDeviceInfoDetail(context));
    }

    /**
     * 日志配置
     *
     * @param isDebug debug
     */
    public static void setShowLog(boolean isDebug) {
        setShowLog(isDebug, 3);
    }

    public static void setShowLog(boolean debug, int tagCount) {
        isDebug = debug;
        ViseLog.getLogConfig()
                .configAllowLog(debug)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-" + tagCount + "} ")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    public static void setNeverCrash(HOnListener<Throwable> onListener) {
        NeverCrashHelper.init((t, e) -> {
//            e.printStackTrace();
            ViseLog.d(e.getMessage() + "\n" + Log.getStackTraceString(e));
            new Handler(Looper.getMainLooper()).post(() -> {
                if (onListener != null) {
                    onListener.onListen(e);
                } else {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public static void setNeverCrash() {
        setNeverCrash(null);
    }
}
