package x.com.hl_dialog;

import android.os.Bundle;
import android.view.View;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_view.LoadingBaseView;

public class CWaitingDialog extends BaseNDialog {

    private LoadingBaseView loadingView;

    @Override
    protected void onDialogResume() {
        super.onDialogResume();
        if (loadingView != null) {
            loadingView.start();
        }
    }

    @Override
    protected void onDialogPause() {
        super.onDialogPause();
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    @Override
    protected void initUI(View layout, Bundle bundle) {
        loadingView = layout.findViewById(R.id.loadingView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_waitting;
    }

    @Override
    public boolean isCanceledOnTouchOutside() {
        return false;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    protected void onDialogDismiss() {
        super.onDialogDismiss();
        loadingView = null;
    }
}
