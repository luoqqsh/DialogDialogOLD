package x.com.hl_tools;

import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class hl_vm {
    /**
     * 判断CPU是否支持64位
     * 安卓5.0以上
     *
     * @return 结果
     */
    public static boolean is64bit() {
        boolean is64bit = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            is64bit = Build.SUPPORTED_64_BIT_ABIS.length > 0;
        } else {
            try {
                BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
                if (localBufferedReader.readLine().contains("aarch64")) {
                    is64bit = true;
                }
                localBufferedReader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return is64bit;
    }

    /**
     * 判断CPU是否支持64位
     * 安卓6.0以上
     *
     * @return 结果
     */
    public static boolean is64bit60() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Process.is64Bit();
        }
        return false;
    }

    /**
     * 5.0、6.0均可用
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean isART64(Context context) {

        final String tag = "is64ART";
        final String fileName = "art";

        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> cls = ClassLoader.class;
            Method method = cls.getDeclaredMethod("findLibrary", String.class);
            Object object = method.invoke(classLoader, fileName);
            if (object != null) {
                return ((String) object).contains("lib64");
            }
        } catch (Exception e) {

        }

        return false;
    }

    public static boolean is64() {
        String arch = System.getProperty("os.arch");
        return arch != null && arch.contains("64");
    }

    /**
     * 检测系统使用的是art还是dalvik
     *
     * @return 结果
     */
    public static boolean getIsArtInUse() {
        final String vmVersion = System.getProperty("java.vm.version");
        return vmVersion != null && vmVersion.startsWith("2");
    }
}
