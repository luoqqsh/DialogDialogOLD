package x.com.hl_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import x.com.hl_dialog.MeTools.PermissionHelper;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.model.PermissionModel;

public class DFPermissionDialog extends DialogFragment implements hl_permissions.OnPermissionListener {
    private List<PermissionModel> list_permission;
    private Fragment fragmentcontext;
    private ListDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fragmentcontext = this;
        list_permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            list_permission.add(new PermissionModel("允许读写手机存储", true, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        } else {
            list_permission.add(new PermissionModel("允许读写手机存储", false, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            list_permission.add(new PermissionModel("允许读取本机识别码", true, Manifest.permission.READ_PHONE_STATE));
        } else {
            list_permission.add(new PermissionModel("允许读取本机识别码", false, Manifest.permission.READ_PHONE_STATE));
        }
        final hl_permissions p = hl_permissions.getInstance(this);
        if (p.checkPermission(getActivity())) {
            list_permission.add(new PermissionModel("允许\"显示悬浮窗\"", true, ""));
        } else {
            list_permission.add(new PermissionModel("允许\"显示悬浮窗\"", false, ""));
        }
//        List<String> names = new ArrayList<>();
//        names.add("允许使用通知");
        dialog = new ListDialog(getActivity(), R.style.transparentFrameWindowStyle, list_permission,"请允许以下权限");
        dialog.setItemColor(R.color.colorAccent, R.color.colorPrimary);
        dialog.setItemListener(new ListDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!list_permission.get(position).isIsapply()) {
                    if (position == 2) {
                        p.applyPermission(getActivity());
                    } else {
                        PermissionHelper.getInstance().applyPermission(fragmentcontext, new String[]{list_permission.get(position).getPermission()}, new PermissionHelper.RequestPermissionCallBack() {
                            @Override
                            public void requestPermissionSuccess() {
                                list_permission.get(position).setIsapply(true);
                                dialog.refresh();
                            }

                            @Override
                            public void requestPermissionFail() {
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

    public static DFPermissionDialog newInstance(String content) {
        DFPermissionDialog fragment = new DFPermissionDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void result(boolean isSuccess) {
        if (isSuccess) {
            list_permission.get(2).setIsapply(true);
            dialog.refresh();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.getInstance().requestPermissionsResult(requestCode, permissions, grantResults);
    }
}
