package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.vise.log.ViseLog;

import x.com.hl_dialog.NDialog;
import x.com.hl_dialog.NDialogBuilder;
import x.com.hl_tools.permissions.HuaweiUtils;
import x.com.hl_tools.permissions.MeizuUtils;
import x.com.hl_tools.permissions.MiuiUtils;
import x.com.hl_tools.permissions.OppoUtils;
import x.com.hl_tools.permissions.QikuUtils;
import x.com.hl_tools.permissions.RomUtils;

/**
 * 特殊权限申请
 */
public class hl_permissionsOther {
    private static final String ROM360 = "rom360";
    private static final String HUAWEI = "huawei";
    private static final String MEIZU = "meizu";
    private static final String MIUI = "miui";
    private static final String OPPO = "oppo";
    private static final String COMMON_ROM = "common_rom";
    private static OnPermissionListener mOnPermissionListener;

    private hl_permissionsOther() {
    }

    public static hl_permissionsOther getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public static hl_permissionsOther getInstance(OnPermissionListener mOnPermissionListener1) {
        mOnPermissionListener = mOnPermissionListener1;
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        @SuppressLint("StaticFieldLeak")
        private static final hl_permissionsOther INSTANCE = new hl_permissionsOther();
    }

    /**
     * 检查悬浮窗权限
     *
     * @param context 上下文
     * @return 是否有权限
     */
    public boolean checkFloatwindow(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    /**
     * 去设置悬浮窗，需要先实例化后调用
     * hl_permissionsOther p = hl_permissionsOther.getInstance(listener);
     *
     * @param context      上下文
     * @param isneeddialog 是否需要显示申请对话框
     */
    public void gotoSetFloatWindow(Context context, boolean isneeddialog) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context, isneeddialog);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context, isneeddialog);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context, isneeddialog);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context, isneeddialog);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context, isneeddialog);
            }
        } else {
            commonROMPermissionApply(context, isneeddialog);
        }
    }

    /**
     * 检查是否有通知权限
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean checkNotification(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }
    /**
     * 是否启用辅助功能服务
     *
     * @param mContext             context
     * @param serviceCanonicalName 服务类名
     * @return 结果
     */
    public static boolean checkAccessibilitySettingsOn(Context mContext, String serviceCanonicalName) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = mContext.getPackageName() + "/" + serviceCanonicalName;
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            ViseLog.e("Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
//                    ViseLog.e("-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
            ViseLog.e("***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
    /**
     * 去设置通知
     *
     * @param context 上下文
     * @return 是否成功
     */
    public static boolean gotoSetNotification(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * 去应用设置详情页，主要是手动设置锁屏权限和部分机型的电话权限
     *
     * @param context 上下文
     */
    public static void gotoAppSet(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 检查是否有修改系统设置权限
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean checkSetSystem(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        } else {
            return true;
        }
    }

    /**
     * 去设置允许修改系统设置权限
     *
     * @param context 上下文
     */
    public static void gotoSetSystem(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断是否有WRITE_SETTINGS权限if(!Settings.System.canWrite(this))
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }

    /**
     * 有权查看使用情况的应用
     *
     * @param context 上下文
     * @return 结果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static boolean isNoOption(Context context) {
        PackageManager packageManager = context.getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 是否启用权查看使用情况的应用
     *
     * @param context 上下文
     * @return 结果
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean checkUsage(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        return queryUsageStats != null && !queryUsageStats.isEmpty();
    }

    /**
     * 跳转权查看使用情况的应用页面
     *
     * @param context 上下文
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void gotoSetUsage(Context context) {
        if (isNoOption(context)) {
            context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    /**
     * 跳转到辅助功能设置页面
     *
     * @param context context
     */
    public static void gotoSetAccessibility(Context context) {
        context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    ///////////////////////////////以下内容仅和悬浮窗相关///////////////////////////////////////////////
    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e("TAG", Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void ROM360PermissionApply(final Context context, boolean isneeddialog) {
        showDialog(context, ROM360, isneeddialog);
    }

    private void huaweiROMPermissionApply(final Context context, boolean isneeddialog) {
        showDialog(context, HUAWEI, isneeddialog);
    }

    private void meizuROMPermissionApply(final Context context, boolean isneeddialog) {
        showDialog(context, MEIZU, isneeddialog);
    }

    private void miuiROMPermissionApply(final Context context, boolean isneeddialog) {
        showDialog(context, MIUI, isneeddialog);
    }

    private void oppoROMPermissionApply(final Context context, boolean isneeddialog) {
        showDialog(context, OPPO, isneeddialog);
    }


    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context, boolean isneeddialog) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context, isneeddialog);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showDialog(context, COMMON_ROM, isneeddialog);
            }
        }
    }

    private void showDialog(Context context, final String flag, boolean isneeddialog) {
        if (isneeddialog) {
            Dialog a = new NDialog(context, NDialog.nDialog_Iphone, 0.8f)
                    .setTouchOutSideCancelable(false, false)
                    .setMessage("您的手机没有授予悬浮窗权限，请开启后再试！", NDialogBuilder.MSG_LAYOUT_CENTER)
                    .setBtnClickListener(true, "稍后再说", "现在去开启", new NDialog.onDialogbtnClickListener() {
                        @Override
                        public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                            switch (whichBtn) {
                                case 1:
                                    if (null != mOnPermissionListener) {
                                        mOnPermissionListener.result(false);
                                    }
                                    break;
                                case 2:
                                    goSet(flag, context);
                                    break;
                            }
                        }
                    }).create();
            a.show();
        } else {
            goSet(flag, context);
        }
    }

    private void goSet(String flag, Context context) {
        switch (flag) {
            case ROM360:
                QikuUtils.applyPermission(context);
                break;
            case HUAWEI:
                HuaweiUtils.applyPermission(context);
                break;
            case MEIZU:
                MeizuUtils.applyPermission(context);
                break;
            case MIUI:
                MiuiUtils.applyMiuiPermission(context);
                break;
            case OPPO:
                OppoUtils.applyOppoPermission(context);
                break;
            case COMMON_ROM:
                try {
                    commonROMPermissionApplyInternal(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mOnPermissionListener != null) {
                        mOnPermissionListener.result(false);
                    }
                }
                break;
        }
        if (mOnPermissionListener != null) {
            mOnPermissionListener.result(true);
        }
    }

    public void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public interface OnPermissionListener {
        void result(boolean isSuccess);
    }
}