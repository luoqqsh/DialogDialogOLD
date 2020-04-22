package x.com.hl_base.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import x.com.hl_dialog.BuildConfig;

/**
 * Dialog 通用设置
 * Created by ljq on 2019/3/6
 */
public abstract class CommonDialog extends DialogFragment {

    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnCancelListener mOnCancelListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mOnCancelListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void show(FragmentManager manager, String tag) {
        this.show(manager.beginTransaction(), tag);
    }

    public void cancel() {
        getDialog().cancel();
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return isAdded() && getDialog() != null && getDialog().isShowing();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isResumed() || isAdded() || isVisible() || isRemoving() || isDetached()) {
            if (BuildConfig.DEBUG) {
                boolean isResumed = isResumed();
                boolean isAdded = isAdded();
                boolean isVisible = isVisible();
                boolean isRemoving = isRemoving();
                boolean isDetached = isDetached();
                Log.e(getClass().getSimpleName(), "showDialog Failed !isResumed=" + isResumed
                        + ",isAdded=" + isAdded + ",isVisible=" + isVisible + ",isRemoving=" + isRemoving + ",isDetached=" + isDetached);
            }
            return -1;
        }
        try {
            return transaction.add(this, tag).commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "showDialog Failed", e);
            e.printStackTrace();
            return -1;
        }
    }

}
