package x.com.hl_web;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

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
                if (arg0) {
                    Log.i("hl-x5Sdk", "成功加载腾讯X5内核");
                } else {
                    Log.i("hl-x5Sdk", "加载失败，启用系统内核");
                }
            }

            @Override
            public void onCoreInitFinished() {
                Log.i("hl-x5Sdk", "内核加载完毕");
            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
    }
}