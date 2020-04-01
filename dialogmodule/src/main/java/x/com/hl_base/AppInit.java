package x.com.hl_base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import x.com.hl_log.ViseLog;
import x.com.hl_log.inner.LogcatTree;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_screen;

public class AppInit {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static boolean isDebug = false;

    public static void setContext(Context mContext) {
        AppInit.mContext = mContext.getApplicationContext();
    }

    public static Context getAppContext() {
        if (mContext == null) {
            ViseLog.e("Context初始化异常");
        }
        return mContext.getApplicationContext();
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
}
