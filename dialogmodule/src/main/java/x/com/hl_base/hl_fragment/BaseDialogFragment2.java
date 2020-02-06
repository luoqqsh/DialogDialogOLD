package x.com.hl_base.hl_fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

/**
 * 是以Fragment为基础的，所以不要在构造方法里传参
 * 佳亮封装
 */
public abstract class BaseDialogFragment2 extends DialogFragment implements DialogInterface.OnShowListener {

    private boolean mCancelable = true;
    private boolean isCanCancelByTouch = true;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnShowListener onShowListener;
    private DialogInterface.OnCancelListener onCancelListener;
    private DialogInterface.OnKeyListener onKeyListener;
    private boolean isNoDismissByTouchAndBackPress;
//    private TaskDelayManager taskDelayManager;
    protected FragmentActivity mActivity;
    protected Context mContext;
    private boolean isNeedShowing;
    private boolean isShowLog;

    public BaseDialogFragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkContext();
        if (isShowLog) ViseLog.d("onCreate " + mActivity);
    }

    public void setShowLog(boolean isShowLog) {
        this.isShowLog = isShowLog;
    }

    private void checkContext() {
        if (mActivity == null || mActivity.isFinishing() || mContext == null) {
            mContext = getContext();
            mActivity = getActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isNeedShowing = false;
        if (isShowLog) ViseLog.d("onDestroy " + mActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isNeedShowing = false;
        if (isShowLog) ViseLog.d("onDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowLog) ViseLog.d("onStart");
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(getWidth(), getHeight());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowLog) ViseLog.d("onResume " + mActivity);
        if (!isNeedShowing) {
            dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isShowLog) ViseLog.d("onStop " + mActivity);
        if (!isNeedShowing) {
            dismiss();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        checkContext();
        if (isShowLog) ViseLog.d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        checkContext();
        if (isShowLog) ViseLog.d("onCreateView " + mActivity);
        View layout = inflater.inflate(getLayoutId(), container);
        initUI(layout);
        return layout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        checkContext();
        if (isShowLog) ViseLog.d("onCreateDialog " + mActivity);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            int gravity = getGravity();
            if (gravity != 0) {
                window.setGravity(gravity);
            }
            int dialogAnim = getDialogEnterExitAnimStyle();
            if (dialogAnim != 0) {
                window.setWindowAnimations(dialogAnim);
            }
            //setLayout 在这个位置无效，写在onStart中有效果
//            window.setLayout(getWidth(), getHeight());
        }
        dialog.setCancelable(mCancelable);
        dialog.setCanceledOnTouchOutside(isCanCancelByTouch);
        dialog.setOnShowListener(this);
        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);
        if (onKeyListener != null) dialog.setOnKeyListener(onKeyListener);
        if (isNoDismissByTouchAndBackPress) setDialogNoDismissByTouchAndBackPress(dialog);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (isShowLog) ViseLog.d("onShow " + mActivity);
        if (onShowListener != null) {
            onShowListener.onShow(dialog);
        }
        if (!isNeedShowing) {
            dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isNeedShowing = false;

        if (isShowLog) ViseLog.d("onDismiss " + mActivity);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (isShowLog) ViseLog.d("onCancel " + mActivity);
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getTheme() {
        return R.style.DialogFragment;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected int getDialogEnterExitAnimStyle() {
//        return R.style.Anim_Trans_500;
        //默认不要动画
        return 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isShowLog) ViseLog.d("onViewCreated " + mActivity);
//        initUI(view);
    }

    public void setDialogNoDismissByTouchAndBackPress() {
        isNoDismissByTouchAndBackPress = true;
        if (getDialog() != null) {
            setDialogNoDismissByTouchAndBackPress(getDialog());
        }
    }

    public void setCanceledOnTouchOutside(boolean isCanCancelByTouch) {
        this.isCanCancelByTouch = isCanCancelByTouch;
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(isCanCancelByTouch);
        }
    }

    /**
     * 是否可取消，包括点击屏幕外及返回键
     */
    public void setCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
        if (getDialog() != null) {
            getDialog().setCancelable(mCancelable);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        if (getDialog() != null) {
            getDialog().setOnDismissListener(onDismissListener);
        }
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        if (getDialog() != null) {
            getDialog().setOnShowListener(onShowListener);
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        if (getDialog() != null) {
            getDialog().setOnCancelListener(onCancelListener);
        }
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
        if (getDialog() != null) {
            getDialog().setOnKeyListener(onKeyListener);
        }
    }

    public boolean isShowing() {
        //调用show，再很快就调用dismiss，如果在调用dismiss之前判断是否isShowing，此时会返回false，因为UI都还没创建，dialog都是空的
        //如果先判断isShowing为true时再dismiss，就会造成不会运行dismiss！，所以这里直接采用 isNeedShowing 来替代
//        return getDialog() != null && getDialog().isShowing();
        return isNeedShowing;
    }

    protected abstract void initUI(View layout);

    protected abstract int getLayoutId();

    public void show(FragmentActivity activity, Fragment fragment) {
        if (activity != null) {
            show(activity, 0);
        } else if (fragment != null) {
            show(fragment, 0);
        }
    }

    public void show(FragmentActivity activity) {
        show(activity, 0);
    }

    public void show(FragmentActivity activity, long dismissDelayTime) {
        if (activity == null || activity.isFinishing()) return;
        show(activity.getSupportFragmentManager(), dismissDelayTime);
    }

    public void show(Fragment fragment) {
        show(fragment, 0);
    }

    public void show(Fragment fragment, long dismissDelayTime) {
        if (fragment == null) return;
        show(fragment.getChildFragmentManager(), dismissDelayTime);
    }

    public void show(FragmentManager fragmentManager, long dismissDelayTime) {
        if (fragmentManager == null) {
            if (isShowLog) ViseLog.d("show return fragmentManager==null");
            return;
        }
        if (!isShowing()) {
            if (isAdded()) {
                if (isShowLog) ViseLog.d("show return isAdded");
                return;
            }
            isNeedShowing = true;
            show(fragmentManager, getClass().getSimpleName() + hashCode());
        }
        if (dismissDelayTime > 0) {
//            if (taskDelayManager != null) {
//                taskDelayManager.cancel();
//                taskDelayManager = null;
//            }
//            taskDelayManager = new TaskDelayManager() {
//                @Override
//                public void onListen(Long index) {
//                    dismiss();
//                }
//            };
//            taskDelayManager.delay(dismissDelayTime);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isNeedShowing = true;
        boolean isStateSaved = manager.isStateSaved();
        if (!isStateSaved) {
            super.show(manager, tag);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        isNeedShowing = true;
        return super.show(transaction, tag);
    }

    @Override
    public void showNow(FragmentManager manager, String tag) {
        isNeedShowing = true;
        super.showNow(manager, tag);
    }

    @Override
    public void dismiss() {
        isNeedShowing = false;
        if (isShowLog) ViseLog.d("dismiss " + mActivity);
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            if (isShowLog) ViseLog.d("dismiss return fragmentManager==null");
            return;
        }
        boolean isStateSaved = manager.isStateSaved();
        if (isStateSaved) {
            if (isShowLog) ViseLog.d("dismiss return isStateSaved = true");
            return;
        }
        if (!isAdded()) {
            if (isShowLog) ViseLog.d("dismiss return !isAdded");
            return;
        }
//        if (taskDelayManager != null) {
//            taskDelayManager.cancel();
//            taskDelayManager = null;
//        }
        super.dismiss();
    }

    private void setDialogNoDismissByTouchAndBackPress(Dialog dialog) {
        if (dialog == null) return;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }
}