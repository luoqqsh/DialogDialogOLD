package x.com.hl_web;

import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;
import com.vise.log.ViseLog;

/**
 * Created by pp on 17/5/19.
 */

public class TbsManager {
    static TbsManager ma;

    public static TbsManager getInstance() {
        if (ma == null) {
            ma = new TbsManager();
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
