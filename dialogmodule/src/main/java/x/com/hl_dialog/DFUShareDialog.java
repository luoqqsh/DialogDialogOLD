package x.com.hl_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.com.hl_base.dialog.BaseDialogFragment;
import x.com.hl_base.dialog.NDialog;

public class DFUShareDialog extends BaseDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        NDialog sharedialog = new NDialog(getActivity(), NDialog.nDialog_Share, 1.0f)
                .setTouchOutSideCancelable(false, false)
                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
                .setBtnClickListener(true, "微信", "朋友圈", "QQ",
                        "新浪微博", "取消", new NDialog.onDialogbtnClickListener() {
                            @Override
                            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                                switch (whichBtn) {
                                    case 1://微信

                                        break;
                                    case 2://朋友圈

                                        break;
                                    case 3://qq

                                        break;
                                    case 4://新浪

                                        break;
                                }
                            }
                        });
        return sharedialog.create();
    }
}
