package x.com.hl_tools;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class hl_id {
    //非手机设备，如平板、机顶盒等无此id
    public static String getDeviceId(Context context) {
        if (!hl_permissions.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "获取失败，检查READ_PHONE_STATE权限";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getSSAID(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getIMEI(Context context) {
        if (!hl_permissions.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "获取失败，检查READ_PHONE_STATE权限";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "Android Q 以上版本限制";
        }
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
        if (tm != null) {
            return tm.getImei();
        } else {
            return "获取失败，检查权限";
        }
    }
}
