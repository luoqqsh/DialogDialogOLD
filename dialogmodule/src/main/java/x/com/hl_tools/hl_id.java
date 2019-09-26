package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.content.Context;

public class hl_id {
    public static String getSSAID(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }
}
