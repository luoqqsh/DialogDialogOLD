package x.com.hl_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.vise.log.ViseLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DFNDialog extends DialogFragment {
    private String aa = "ww;";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ViseLog.d("onCreateDialog");
        Dialog dialog = new NDialog(getActivity(), NDialog.nDialog_Update, 0.8f)
                .setTouchOutSideCancelable(false, false)
                .setMessage(aa, NDialogBuilder.MSG_LAYOUT_CENTER)
                .setBtnClickListener(true, "好", "不好", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(context, "点击了好", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "点击了不好", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        return dialog;
    }

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
}
