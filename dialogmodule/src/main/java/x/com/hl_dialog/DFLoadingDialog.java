package x.com.hl_dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DFLoadingDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ProgressDialog loadDialog = new ProgressDialog(getActivity());// 创建加载框
        loadDialog.setMessage("正在加载中，请稍候...");// 设置显示的消息
        loadDialog.setCancelable(true);// 设置是否可以取消
        return loadDialog;
    }
}
