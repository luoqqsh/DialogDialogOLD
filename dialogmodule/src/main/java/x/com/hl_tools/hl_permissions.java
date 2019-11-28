package x.com.hl_tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import x.com.hl_tools.permissions.ApplyPermissionActivity;

/**
 * 动态权限申请
 */
public class hl_permissions {
    private static Map<String, Boolean> failPermission;

    public interface RequestPermissionCallBack {
        void requestPermissionSuccess();

        void requestPermissionFail(Map<String, Boolean> failPermission);
    }

    private static RequestPermissionCallBack mPermissionCallBack;

    public static boolean checkPermission(Context context, String permission) {
        if (permission == null) return false;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void applyWriteStoragePermission(Context context, RequestPermissionCallBack permissionCallBack) {
        String[] q = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        applyPermission(context, q, permissionCallBack);
    }

    public static void applyLocationPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        String[] q = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        applyPermission(context, q, permissionCallBack);
    }

    public static void applyCameraPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        String[] q = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        applyPermission(context, q, permissionCallBack);
    }

    public static void applyWallpaperPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        String[] q = new String[]{Manifest.permission.SET_WALLPAPER, Manifest.permission.SET_WALLPAPER_HINTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        applyPermission(context, q, permissionCallBack);
    }

    public static void applyPermission(Context context, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        failPermission = new HashMap<>();
        mPermissionCallBack = permissionCallBack;
        //6.0及以上 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            boolean isNeedApply = false;
            for (String permission : permissionList) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    isNeedApply = true;
                    break;
                }
            }
            if (isNeedApply) {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ApplyPermissionActivity.launch(context, permissionList);
            } else {
                if (permissionCallBack != null) {
                    permissionCallBack.requestPermissionSuccess();
                    mPermissionCallBack = null;
                }
            }
        } else {
            //6.0以下直接执行
            if (permissionCallBack != null) {
                permissionCallBack.requestPermissionSuccess();
            }
        }
    }

    public static void requestPermissionsResult(String[] permissions, int[] grantResults, boolean[] isNoTip) {
        boolean isApplyFail = false;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                //存在权限申请被拒绝
                isApplyFail = true;
                failPermission.put(permissions[i], isNoTip[i]);
            }
        }
        if (isApplyFail) {
            //被拒绝了
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionFail(failPermission);
            }
        } else {
            //申请成功
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionSuccess();
            }
        }
        mPermissionCallBack = null;
    }
}
