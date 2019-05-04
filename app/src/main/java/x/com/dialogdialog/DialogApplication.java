package x.com.dialogdialog;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;

import x.com.hl_log.ViseLog;
import x.com.hl_log.inner.LogcatTree;
import x.com.hl_tools.DialogTools.NeverCrashHelper;
import x.com.hl_web.x5Manager;

public class DialogApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //参数：分析结果存在的文件
//        Debug.startMethodTracing("/storage/emulated/0/DCIM/app.trash");
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-4} ")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
        FileDownloader.setup(getApplicationContext());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        NeverCrashHelper.init((t, e) -> {
            Log.d("luoqqsh", Log.getStackTraceString(e));
//            e.printStackTrace();
            showToast(e.getMessage());
        });
        x5Manager.getInstance().init(this);
//        Debug.stopMethodTracing();
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), "出现异常，请联系客服解决或稍候再试！", Toast.LENGTH_SHORT).show());
    }
}
