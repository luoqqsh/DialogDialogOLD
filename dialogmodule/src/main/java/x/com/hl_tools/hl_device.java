package x.com.hl_tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

public class hl_device {
    //获取屏幕角度方向
    public static int getRotation(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    //设备是否支持相机设备
    public static boolean isSupportCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 是否是三星手机
     */
    public static boolean isSamSungPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toLowerCase().contains("samsung");
        }
        return false;
    }

    /**
     * 是否是魅族手机
     */
    public static boolean isMeiZuPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toLowerCase().contains("meizu");
        }
        return false;
    }

    public static boolean isVivoPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toLowerCase().contains("vivo");
        }
        return false;
    }

    /**
     * 是否是小米手机
     */
    public static boolean isXiaoMiPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toLowerCase().contains("xiaomi");
        }
        return false;
    }

    public static boolean isOppoPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toLowerCase().contains("oppo") || isOppoR15Phone();
        }
        return false;
    }

    public static boolean isHuaWeiPhone() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toUpperCase().equals("HUAWEI")) {
                return true;
            } else return brand.toUpperCase().equals("HONOR");
        }
        return false;
    }

    public static boolean isOppoR15Phone() {
        return Build.MODEL.equals("PACM00");
    }
}
