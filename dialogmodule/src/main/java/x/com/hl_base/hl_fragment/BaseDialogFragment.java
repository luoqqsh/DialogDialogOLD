package x.com.hl_base.hl_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 简单封装
 */
public class BaseDialogFragment extends DialogFragment {
    private OnDismissListener onDismissListener;
    private OnCancelListener onCancelListener;

    public void showFDialog(FragmentActivity mActivity) {
        if (!isAdded()) {
            show(mActivity.getSupportFragmentManager(), mActivity.getClass().getName());
        }
    }

    public void showFDialog(Fragment fragment) {
        if (!isAdded()) {
            show(fragment.getChildFragmentManager(), fragment.getClass().getName());
        }
    }

    @Override
    public void onDismiss(@NotNull final DialogInterface dialog) {
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
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener != null) {
            onCancelListener.OnCancel();
            onCancelListener = null;
        }
    }

    @Override
    public void show(@NotNull FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    public int show(@NotNull FragmentTransaction transaction, String tag) {
        if (!isAdded()) {
            return super.show(transaction, tag);
        }
        return 0;
    }

    @Override
    public void showNow(@NotNull FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.showNow(manager, tag);
        }
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public interface OnDismissListener {
        void OnDismiss();
    }

    public interface OnCancelListener {
        void OnCancel();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initSoftInputListener() {
        Objects.requireNonNull(getDialog().getWindow()).getDecorView()
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        InputMethodManager manager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
                                manager.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        return false;
                    }
                });
    }
}
