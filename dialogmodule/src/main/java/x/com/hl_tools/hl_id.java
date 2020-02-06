package x.com.hl_tools;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

public class hl_id {
    public static String getSSAID(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getImei(Context context) {
        if (!hl_permissions.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "获取失败，检查READ_PHONE_STATE权限";
        }
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
        if (tm != null) {
            return tm.getImei();
        } else {
            return "获取失败，检查权限";
        }
    }
}
