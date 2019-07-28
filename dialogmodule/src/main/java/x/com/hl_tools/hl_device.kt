package x.com.hl_tools

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils

object hl_device {

    /**
     * 是否是三星手机
     */
    val isSamSungPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("samsung")
            } else false
        }

    /**
     * 是否是乐视手机
     */
    val isLePhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("leeco") || brand.toLowerCase().contains("letv")
            } else false
        }

    /**
     * 是否是360手机
     */
    val is360Phone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("360") || brand.toLowerCase().contains("qiku")
            } else false
        }

    /**
     * 是否是魅族手机
     */
    val isMeiZuPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("meizu")
            } else false
        }

    val isVivoPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("vivo")
            } else false
        }

    /**
     * 是否是小米手机
     */
    val isXiaoMiPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("xiaomi")
            } else false
        }

    /**
     * 是否是中兴手机
     */
    val isZTEPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("zte")
            } else false
        }

    /**
     * 是否是一加手机
     */
    val isOnePlusPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("oneplus")
            } else false
        }

    /**
     * 是否是努比亚手机
     */
    val isNubiaPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("nubia")
            } else false
        }

    /**
     * 是否是索尼手机
     */
    val isSonyPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("sony")
            } else false
        }

    /**
     * 是否是谷歌手机
     */
    val isGooglePhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("google")
            } else false
        }

    val isOppoPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.toLowerCase().contains("oppo") || isOppoR15Phone
            } else false
        }

    val isHuaWeiPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                if (brand.toUpperCase() == "HUAWEI") {
                    true
                } else
                    brand.toUpperCase() == "HONOR"
            } else false
        }

    val isOppoR15Phone: Boolean
        get() = Build.MODEL == "PACM00"

    //获取屏幕角度方向
    fun getRotation(activity: Activity): Int {
        return activity.windowManager.defaultDisplay.rotation
    }

    //设备是否支持相机设备
    fun isSupportCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}
