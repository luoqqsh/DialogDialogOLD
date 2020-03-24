package x.com.dialogdialog.BaseApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.liulishuo.filedownloader.FileDownloader;

import x.com.hl_base.AppInit;
import x.com.hl_base.NeverCrashHelper;
import x.com.hl_log.ViseLog;

public class DialogApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.setShowLog(true);
        AppInit.printInformation();

        FileDownloader.setup(getApplicationContext());

        NeverCrashHelper.init((t, e) -> {
//            e.printStackTrace();
            ViseLog.d(Log.getStackTraceString(e));
            showToast(e.getMessage());
        });
//        x5Manager.getInstance().init(this);

//        LiveEventBus.config().supportBroadcast(this);//跨进程通过广播
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }
}
