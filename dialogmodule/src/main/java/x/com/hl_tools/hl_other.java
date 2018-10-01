package x.com.hl_tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

public class hl_other {
    //key为渠道名的key，对应友盟的 UMENG_CHANNEL
    public static String getChannel(Context context, String key) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    //判断是否是json数据
    public static boolean isJson(String str) {
        try {
            Gson g = new Gson();
            g.fromJson(str, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
