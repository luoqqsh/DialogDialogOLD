package x.com.dialogdialog.BaseApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;
import x.com.hl_base.app.AppInit;

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
        AppInit.setNeverCrash();

//        x5Manager.getInstance().init(this);
//        LiveEventBus.config().supportBroadcast(this);//跨进程通过广播
    }
}
