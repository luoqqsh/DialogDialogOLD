package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import x.com.hl_dialog.NDialog;
import x.com.hl_dialog.NDialogBuilder;
import x.com.hl_tools.permissions.HuaweiUtils;
import x.com.hl_tools.permissions.MeizuUtils;
import x.com.hl_tools.permissions.MiuiUtils;
import x.com.hl_tools.permissions.OppoUtils;
import x.com.hl_tools.permissions.QikuUtils;
import x.com.hl_tools.permissions.RomUtils;

public class hl_permissions {
    private static final String ROM360 = "rom360";
    private static final String HUAWEI = "huawei";
    private static final String MEIZU = "meizu";
    private static final String MIUI = "miui";
    private static final String OPPO = "oppo";
    private static final String COMMON_ROM = "common_rom";
    private static OnPermissionListener mOnPermissionListener;

    private hl_permissions() {
    }

    public static hl_permissions getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public static hl_permissions getInstance(OnPermissionListener mOnPermissionListener1) {
        mOnPermissionListener = mOnPermissionListener1;
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        @SuppressLint("StaticFieldLeak")
        private static final hl_permissions INSTANCE = new hl_permissions();
    }

    public boolean checkPermission(Context context) {
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

    public void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context);
            }
        } else {
            commonROMPermissionApply(context);
        }
    }

    private void ROM360PermissionApply(final Context context) {
        showDialog(context, ROM360);
    }

    private void huaweiROMPermissionApply(final Context context) {
        showDialog(context, HUAWEI);
    }

    private void meizuROMPermissionApply(final Context context) {
        showDialog(context, MEIZU);
    }

    private void miuiROMPermissionApply(final Context context) {
        showDialog(context, MIUI);
    }

    private void oppoROMPermissionApply(final Context context) {
        showDialog(context, OPPO);
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showDialog(context, COMMON_ROM);
            }
        }
    }

    private void showDialog(Context context, final String flag) {
        Dialog a = new NDialog(context, NDialog.NDialog_Iphone, 0.8f)
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
                                switch (flag) {
                                    case ROM360:
                                        QikuUtils.applyPermission(context);
                                        mOnPermissionListener.result(true);
                                        break;
                                    case HUAWEI:
                                        HuaweiUtils.applyPermission(context);
                                        mOnPermissionListener.result(true);
                                        break;
                                    case MEIZU:
                                        MeizuUtils.applyPermission(context);
                                        mOnPermissionListener.result(true);
                                        break;
                                    case MIUI:
                                        MiuiUtils.applyMiuiPermission(context);
                                        mOnPermissionListener.result(true);
                                        break;
                                    case OPPO:
                                        OppoUtils.applyOppoPermission(context);
                                        mOnPermissionListener.result(true);
                                        break;
                                    case COMMON_ROM:
                                        try {
                                            commonROMPermissionApplyInternal(context);
                                            mOnPermissionListener.result(true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mOnPermissionListener.result(false);
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }).create();
        a.show();

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