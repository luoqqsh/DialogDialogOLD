package x.com.hl_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_permissionsOther;
import x.com.hl_tools.model.PermissionModel;

public class DFPermissionListDialog extends DialogFragment {
    private List<PermissionModel> list_permission;
    private Fragment fragmentcontext;
    private ListDialog dialog;
    private static hl_permissionsOther.OnPermissionListener listener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fragmentcontext = this;
        list_permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            list_permission.add(new PermissionModel("允许读写手机存储", true, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        } else {
            list_permission.add(new PermissionModel("允许读写手机存储", false, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            list_permission.add(new PermissionModel("允许读取本机识别码", true, Manifest.permission.READ_PHONE_STATE));
        } else {
            list_permission.add(new PermissionModel("允许读取本机识别码", false, Manifest.permission.READ_PHONE_STATE));
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            list_permission.add(new PermissionModel("允许读取联系人", true, Manifest.permission.READ_CONTACTS));
        } else {
            list_permission.add(new PermissionModel("允许读取联系人", false, Manifest.permission.READ_CONTACTS));
        }
        final hl_permissionsOther p = hl_permissionsOther.getInstance(listener);
        if (p.checkFloatwindow(getActivity())) {
            list_permission.add(new PermissionModel("允许\"显示悬浮窗\"", true, ""));
        } else {
            list_permission.add(new PermissionModel("允许\"显示悬浮窗\"", false, ""));
        }
//        List<String> names = new ArrayList<>();
//        names.add("允许使用通知");
        dialog = new ListDialog(getActivity(), R.style.transparentFrameWindowStyle, list_permission, "请允许以下权限");
        dialog.setItemColor(R.color.colorAccent, R.color.colorPrimary);
        dialog.setItemListener(new ListDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!list_permission.get(position).isIsapply()) {
                    if (position == 3) {
                        p.gotosetFW(getActivity(),true);
                    } else {
                        hl_permissions.getInstance().applyPermission(fragmentcontext, new String[]{list_permission.get(position).getPermission()}, new hl_permissions.RequestPermissionCallBack() {
                            @Override
                            public void requestPermissionSuccess() {
                                list_permission.get(position).setIsapply(true);
                                dialog.refresh();
                            }

                            @Override
                            public void requestPermissionFail(boolean isNoTip) {
                                list_permission.get(position).setIsapply(false);
                                dialog.refresh();
                            }
                        });
                    }
                }
            }
        });

        return dialog;
    }

    public static DFPermissionListDialog newInstance(hl_permissionsOther.OnPermissionListener listener1) {
        DFPermissionListDialog fragment = new DFPermissionListDialog();
        Bundle bundle = new Bundle();
        listener = listener1;
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        hl_permissions.getInstance().requestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.result(true);
    }
}
