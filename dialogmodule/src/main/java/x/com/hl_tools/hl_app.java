package x.com.hl_tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

import x.com.hl_log.ViseLog;

public class hl_app {


    //安装
    public static void installAPK(final Activity activity, final File apkFile) {
        Intent intent = new Intent();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0启动姿势<pre name="code" class="html">    //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同；apkFile为问题1中的外部存储apk文件</pre>
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", apkFile);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，次过程没有用户交互
        } else {//7.0以下启动姿势
            uri = Uri.fromFile(apkFile);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /**
     * 通过APK地址获取此APP的包名和版本等信息
     */
    public static String getCurrentPackageName(Context context) {
        return getPackageName(context, context.getPackageName());
    }

    public static String getPackageName(Context context, String FilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(FilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
//            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //获取安装包名称
            ViseLog.i("包名：" + packageName);
            String version = info.versionName; //获取版本信息
            ViseLog.i("版本：" + version);
            return packageName;
        }
        return null;
    }

    //是否安装该应用
    public static boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getVersionName(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    public static Object getMetaData(Context context, String metaName, Object defaultName) {
        try {
            Bundle applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if (defaultName instanceof String) {
                return applicationInfo.getString(metaName, (String) defaultName);
            } else if (defaultName instanceof Integer) {
                return applicationInfo.getInt(metaName, (int) defaultName);
            } else if (defaultName instanceof Boolean) {
                return applicationInfo.getBoolean(metaName, (boolean) defaultName);
            } else if (defaultName instanceof Float) {
                return applicationInfo.getFloat(metaName, (float) defaultName);
            } else if (defaultName instanceof Long) {
                return applicationInfo.getLong(metaName, (long) defaultName);
            } else {
                return defaultName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defaultName;
        }
    }

    public static String getAppName(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.loadLabel(context.getPackageManager()) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
