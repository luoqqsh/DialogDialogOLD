package x.com.hl_tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import java.io.File
import java.util.*
import java.util.regex.Pattern

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

    //设备是否支持相机设备,需要系统大于4.2版本
    fun isSupportCamera(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        } else {
            false
        }
    }

    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }


    fun getSystemVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }



    fun getsupportedAbis(): String {
        var abis = ""
        for (i in if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            return ""
        }) {
            abis += "$i "
        }
        return abis
    }
    /**
     * 获取Cpu内核数
     * @return
     */
    val numCores: Int
        get() = try {
            val dir = File("/sys/devices/system/cpu/")
            val files = dir.listFiles { pathname -> Pattern.matches("cpu[0-9]", pathname.name) }
            files.size
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
    fun getDeviceInfo(context: Context): String {
        return "BRAND-品牌:" + Build.BRAND + "\n" +
                "MANUFACTURER-制造商:" + Build.MANUFACTURER + "\n" +
                "MODEL-型号:" + Build.MODEL + "\n" +
                "当前语言:" + getSystemLanguage() + "\n" +
                "Android系统版本:" + getSystemVersion() + "\n" +
                "Android系统SDK:" + Build.VERSION.SDK_INT + "\n" +
                "系统ROM:" + hl_rom.getName() + "\n" +
                "ROM版本:" + hl_rom.getVersion() + "\n" +
                "支持的CPU架构:" + getsupportedAbis() + "\n" +
                "CPU核数:" + numCores + "\n" +
                "SSAID:" + hl_id.getSSAID(context) + "\n" +
                "Root权限:" + if (hl_root.isRoot()) "已获得Root权限" else "未获得Root权限" + "\n"
    }

    fun getDeviceInfoDetail(context: Context): String {
        return "BRAND-厂商:" + android.os.Build.BRAND + "\n" +
                "BOOTLOADER:" + android.os.Build.BOOTLOADER + "\n" +
                "BOARD:" + android.os.Build.BOARD + "\n" +
                "DEVICE-设备参数:" + android.os.Build.DEVICE + "\n" +
                "DISPLAY-版本显示:" + android.os.Build.DISPLAY + "\n" +
                "FINGERPRINT:" + android.os.Build.FINGERPRINT + "\n" +
                "HARDWARE-硬件类型:" + android.os.Build.HARDWARE + "\n" +
                "HOST-主机:" + android.os.Build.HOST + "\n" +
                "ID-生产ID:" + android.os.Build.ID + "\n" +
                "MANUFACTURER-制造商:" + android.os.Build.MANUFACTURER + "\n" +
                "MODEL-型号:" + android.os.Build.MODEL + "\n" +
                "PRODUCT:" + android.os.Build.PRODUCT + "\n" +
                "TAGS:" + android.os.Build.TAGS + "\n" +
                "USER:" + android.os.Build.USER + "\n" +
                "TIME-手机时间:" + android.os.Build.TIME + "\n" +
                "UNKNOWN:" + android.os.Build.UNKNOWN + "\n" +
                "TYPE:" + android.os.Build.TYPE + "\n"

    }
}
