package x.com.dialogdialog.BaseApp;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import x.com.base.app.AppInit;
import x.com.util.U_thread;

public class DialogApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (U_thread.isRunInUIThread()) {
            AppInit.isDebug = true;
            AppInit.initToast(this);
            AppInit.initLog();
//            AppInit.printInformation();
            AppInit.setNeverCrash();
//            DoraemonKit.install(this, null, "951f6a62f1e0288b4015bdaa183be823");
//            x5Manager.getInstance().init(this);
//            LiveEventBus.config().supportBroadcast(this);//跨进程通过广播
        }
    }
}
