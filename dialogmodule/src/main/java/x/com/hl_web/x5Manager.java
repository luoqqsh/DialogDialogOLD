package x.com.hl_web;

import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

import x.com.hl_log.ViseLog;

/**
 * Created by pp on 17/5/19.
 */

public class x5Manager {
    private static x5Manager ma;

    public static x5Manager getInstance() {
        if (ma == null) {
            ma = new x5Manager();
        }
        return ma;
    }


    public void init(Context context) {
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                ViseLog.d(arg0);
            }

            @Override
            public void onCoreInitFinished() {
                ViseLog.d("onCoreInitFinished");
            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
    }
}