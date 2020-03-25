//package x.com.hl_tools.permissions;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import x.com.hl_log.ViseLog;
//import x.com.hl_tools.hl_leak;
//import x.com.hl_tools.hl_permissions;
//
//public class ApplyPermissionFragment extends Fragment {
//    private final int REQUEST_CODE_PERMISSIONS = 1211;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
//            handleIntent(getIntent());
//        }
//    }
//
//    private void handleIntent(Intent intent) {
//        String[] permissions = intent.getStringArrayExtra("permissions");
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS);
//    }
//
//    public static void launch(Context context, String[] permissions) {
//        if (permissions != null) {
//            Intent intent = new Intent(context, ApplyPermissionFragment.class);
//            intent.putExtra("permissions", permissions);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } else {
//            ViseLog.e("权限申请失败", "权限列表获取失败！");
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSIONS:
//                boolean[] isNoTip = new boolean[permissions.length];
//                for (int i = 0; i < permissions.length; i++) {
//                    isNoTip[i] = !ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
//                }
//                hl_permissions.requestPermissionsResult(permissions, grantResults, isNoTip);
//                finish();
//                break;
//        }
//    }
//
//}
