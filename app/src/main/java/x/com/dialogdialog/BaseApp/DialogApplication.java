package x.com.dialogdialog.BaseApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;

import x.com.hl_base.InitHlApp;
import x.com.hl_base.NeverCrashHelper;
import x.com.hl_log.ViseLog;
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
        InitHlApp.setShowLog(true);
        FileDownloader.setup(getApplicationContext());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        NeverCrashHelper.init((t, e) -> {
//            e.printStackTrace();
            ViseLog.d(Log.getStackTraceString(e));
            showToast(e.getMessage());
        });
        x5Manager.getInstance().init(this);
//        Debug.stopMethodTracing();
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }
}
