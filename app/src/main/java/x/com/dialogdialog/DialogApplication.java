package x.com.dialogdialog;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

import x.com.hl_dialog.MeTools.NeverCrashHelper;

public class DialogApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
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
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), "出现异常，请联系客服解决或稍候再试！", Toast.LENGTH_SHORT).show());
    }
}
