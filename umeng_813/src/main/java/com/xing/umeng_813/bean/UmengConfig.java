package com.xing.umeng_813.bean;

import android.text.TextUtils;

public class UmengConfig {
    public boolean isDebug = false;
    public boolean openActivityDurationTrack = true;//页面统计--手动统计
    public boolean encryptEnabled = true;//日志加密
    public int sessionContinueMillis = 30000;//退出应用，再过多长时间进来表示不同的启动

    public String APPKEY = "";
    public final String CHANNEL = "Umeng";
    public String WX_ID = "";
    public String WX_SECRET = "";
    public String QQ_ID = "";
    public String QQ_KEY = "";
    public String SINA_ID = "";
    public String SINA_SECRET = "";
    public String SINA_REDIREC_URL = "";
    public String PUSH_SECRET = "";

    public boolean isInitQQ() {
        return !TextUtils.isEmpty(QQ_ID) && !TextUtils.isEmpty(QQ_KEY);
    }

    public boolean isInitWX() {
        return !TextUtils.isEmpty(WX_ID) && !TextUtils.isEmpty(WX_SECRET);
    }

    public boolean isInitSina() {
        return !TextUtils.isEmpty(SINA_ID) && !TextUtils.isEmpty(SINA_SECRET) && !TextUtils.isEmpty(SINA_REDIREC_URL);
    }

    public boolean isInitPush() {
        return !TextUtils.isEmpty(PUSH_SECRET);
    }
}
