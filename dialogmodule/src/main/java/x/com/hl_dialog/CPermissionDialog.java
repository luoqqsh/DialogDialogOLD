package x.com.hl_dialog;

import android.os.Bundle;
import android.view.View;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_tools.hl_permissions;

public class CPermissionDialog extends BaseNDialog {

    @Override
    protected int getLayoutId() {
        return R.layout.cdialog_permission;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        mView.findViewById(R.id.cDialogPermissionGoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hl_permissions.applyWriteStoragePermission(mActivitys.get(),null);
            }
        });
        mView.findViewById(R.id.cDialogPermissionReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
