package x.com.hl_tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 动态权限申请
 */
public class hl_permissions {

    public interface RequestPermissionCallBack {
        void requestPermissionSuccess();

        void requestPermissionFail(boolean isNoTip);
    }

    private static final int REQUESTCODE = 1;
 
    private static hl_permissions hlpermissions;
    private RequestPermissionCallBack mPermissionCallBack;
    private Activity activity;

    public static hl_permissions getInstance() {
        if (hlpermissions == null) {
            hlpermissions = new hl_permissions();
        }
        return hlpermissions;
    }

    public static boolean checkpermission(Context context, String permission) {
        if (permission == null) return false;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 基于Fragment动态权限申请
     */
    public void applyPermission(Fragment fragment, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        applyPermission(null, fragment, permissionList, permissionCallBack);
        this.activity = fragment.getActivity();
    }

    /**
     * 基于Activity动态权限申请
     */
    public void applyPermission(Activity activity, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        applyPermission(activity, null, permissionList, permissionCallBack);
        this.activity = activity;
    }

    private void applyPermission(Activity activity, Fragment fragment, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        mPermissionCallBack = permissionCallBack;
        //6.0及以上 申请权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && (fragment != null || activity != null)) {
            boolean isNeedApply = false;
            for (String permission : permissionList) {
                if (ContextCompat.checkSelfPermission(activity == null ? fragment.getContext() : activity, permission) != PERMISSION_GRANTED) {
                    isNeedApply = true;
                    break;
                }
            }
            if (isNeedApply) {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                if (activity != null) {
                    activity.requestPermissions(permissionList, REQUESTCODE);
                } else {
                    fragment.requestPermissions(permissionList, REQUESTCODE);
                }
            } else {
                mPermissionCallBack.requestPermissionSuccess();
            }
        } else {
            //6.0以下直接执行
            mPermissionCallBack.requestPermissionSuccess();
        }
    }

    /**
     * 申请结果回调
     *
     * @param requestCode  请求码
     * @param permissions  申请的权限集合
     * @param grantResults 申请结果，0
     */
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUESTCODE) {
            boolean isNoTip = false;
            boolean isApplyFail = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PERMISSION_GRANTED) {
                    //存在权限申请被拒绝
                    isApplyFail = true;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        isNoTip = true;
                    }
                }
            }
            if (isApplyFail) {
                //被拒绝了
                if (isNoTip) {
                    Toast.makeText(activity, "您已拒绝权限，请前往权限管理开启", Toast.LENGTH_LONG).show();
                }
                mPermissionCallBack.requestPermissionFail(isNoTip);
                
            } else {
                //申请成功
                mPermissionCallBack.requestPermissionSuccess();
            }
        }
    }
}
