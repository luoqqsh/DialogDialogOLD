package x.com.dialogdialog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UmengMessageService;

import x.com.hl_log.ViseLog;

public class MyPushIntentService extends UmengMessageService {

    @Override
    public void onMessage(Context context, Intent intent) {
        Log.d("vise", "onMessageService");
        ViseLog.d("onMessageService");
        ViseLog.d(intent.getAction());
        //通知开关，如果关闭的话，拦截

    }

}