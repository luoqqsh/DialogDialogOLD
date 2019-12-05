package x.com.hl_base;

import android.content.Context;
import android.util.Log;

import x.com.hl_log.ViseLog;
import x.com.hl_log.inner.LogcatTree;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_screen;

public class HlApp {
    private static HlApp hlApp;
    public static Context mContext;

    /**
     * 初始化
     *
     * @param context 上下文
     * @param isDebug debug
     */
    private HlApp(Context context, boolean isDebug) {
        mContext = context.getApplicationContext();
        setShowLog(isDebug);
    }

    public static HlApp init(Context context, boolean isDebug) {
        if (hlApp == null) {
            hlApp = new HlApp(context, isDebug);
        }
        return hlApp;
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
        ViseLog.getLogConfig()
                .configAllowLog(isDebug)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-4} ")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }
}
