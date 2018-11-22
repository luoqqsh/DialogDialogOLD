package x.com.hl_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.vise.log.ViseLog;

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

public class DFPermissionDialog extends DialogFragment {
    private List<PermissionModel> list_permission;
    private Fragment fragmentcontext;
    private ListDialog dialog;
    private static hl_permissionsOther.OnPermissionListener listener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fragmentcontext = this;

        hl_permissions.getInstance().applyPermission(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new hl_permissions.RequestPermissionCallBack() {
                    @Override
                    public void requestPermissionSuccess() {
                        ViseLog.d("true2222");
                    }

                    @Override
                    public void requestPermissionFail() {
                        ViseLog.d("false1111");
                    }
                });
        return dialog;
    }

    public static DFPermissionDialog newInstance(hl_permissionsOther.OnPermissionListener listener1) {
        DFPermissionDialog fragment = new DFPermissionDialog();
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
    }
}
