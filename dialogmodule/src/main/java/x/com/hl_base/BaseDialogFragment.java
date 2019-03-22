package x.com.hl_base;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseDialogFragment extends DialogFragment {
    private OnDismissListener onDismissListener;

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.OnDismiss();
            onDismissListener = null;
        }
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(this);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (!isAdded()) {
            return super.show(transaction, tag);
        }
        return 0;
    }

    @Override
    public void showNow(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.showNow(manager, tag);
        }
    }

    public interface OnDismissListener {
        void OnDismiss();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
