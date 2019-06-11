package x.com.hl_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.com.hl_base.BaseDialogFragment;

public class DFNDialog extends BaseDialogFragment {
    private String aa = "DFNDialog";
    private OnDismissListener onDismissListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new NDialog(getActivity(), NDialog.nDialog_Update, 0.8f, true)
                .setTouchOutSideCancelable(true, true)
                .setMessage(aa, NDialog.MSG_LAYOUT_CENTER)
                .setBtnClickListener(false, "好", "不好", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                dismiss();
                                Toast.makeText(context, "点击了好", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                dialog.dismiss();
                                Toast.makeText(context, "点击了不好", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();

        //返回键处理
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
        return dialog;
    }

    //传值例子
    public static DFNDialog newInstance() {
        DFNDialog fragment = new DFNDialog();
//        Bundle bundle = new Bundle();
//        bundle.putInt("pid", pid);
//        bundle.putString("message", message);
//        fragment.setArguments(bundle);

//        getArguments().getString("message");
        return fragment;
    }

    public DFNDialog setMessage(String message, int layout) {
        aa = message;
        return this;
    }

    public interface OnDismissListener {
        void OnDismiss();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.OnDismiss();
        }
    }
}
