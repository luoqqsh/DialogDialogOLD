package x.com.hl_tools;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;

public class hl_system {
    //返回键设为home键
    public static boolean startHome(int keyCode, Context context) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(home);
            return false;
        }
        return true;
    }

    /**
     * 是否开启GPS
     */
    public static boolean isOpenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 直接拨打
     *
     * @param tel
     * @param context
     */
    public static void toTel(String tel, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //跳转至拨号界面
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    //发送短信
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:"
                + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    //判断当前设备是否为手机
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }
}
