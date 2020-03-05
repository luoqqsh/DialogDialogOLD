package x.com.hl_dialog.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.hl_interface.OnSimpleListener;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

public abstract class BaseNDialog {
    protected static WeakReference<FragmentActivity> newContext;
    protected static List<BaseNDialog> dialogList = new ArrayList<>();//对话框队列
    public WeakReference<FragmentActivity> mActivitys;
    public WeakReference<BaseNDialogFragment> dialog;//我才是本体！
    protected boolean dismissedFlag = false;
    private boolean isAlreadyShown;
    private boolean isShow;
    protected OnSimpleListener dismissEvent;
    protected OnSimpleListener pauseEvent;
    protected OnSimpleListener resumeEvent;
    protected OnSimpleListener showEvent;
    protected OnSimpleListener cancelEvent;
    protected OnSimpleListener onDismissListener;
    protected OnSimpleListener onPauseListener;
    protected OnSimpleListener onResumeListener;
    protected OnSimpleListener onShowListener;
    protected OnSimpleListener onCancelListener;

    protected void initWindow(Dialog dialog) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (isCancelable()) {
                            if (cancelEvent != null) {
                                cancelEvent.onListen();
                            }
                        } else {
                            return true;
                        }
                    }
                    return false;
                }
            });
            Window window = dialog.getWindow();
            if (window != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    //透明状态栏
//                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                }
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager windowManager = mActivitys.get().getWindowManager();
                DisplayMetrics metrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(metrics);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = (int) (metrics.widthPixels * getWidthScale());
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                lp.windowAnimations = R.style.bottomMenuAnim;
                window.setWindowAnimations(getDialogAnim());
                window.setGravity(getGravity());
                window.setAttributes(lp);
            }
        }
    }

    protected abstract void initUI(View mView, Bundle bundle);

    public boolean isCancelable() {
        return true;
    }

    public boolean isCanceledOnTouchOutside() {
        return true;
    }

    protected abstract int getLayoutId();

    public float getWidthScale() {
        return 0.8f;
    }

    public int getGravity() {
        return Gravity.CENTER;
    }

    public int getDialogAnim() {
        return 0;
    }

    public int getDialogStyle() {
        return R.style.Dialog;
    }

    private void initEvent() {
        dismissEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                dismissedFlag = true;
                isShow = false;
                dialogList.remove(BaseNDialog.this);
                onDialogDismiss();
            }
        };
        pauseEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                onDialogPause();
            }
        };
        resumeEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                onDialogResume();
            }
        };
        showEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                onDialogShow();
            }
        };
        cancelEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                onDialogCancel();
            }
        };
    }

    public void setOnDismissListener(OnSimpleListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnPauseListener(OnSimpleListener onPauseListener) {
        this.onPauseListener = onPauseListener;
    }

    public void setOnResumeListener(OnSimpleListener onResumeListener) {
        this.onResumeListener = onResumeListener;
    }

    public void setOnShowListener(OnSimpleListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public void setOnCancelListener(OnSimpleListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    protected void onDialogDismiss() {
        if (onDismissListener != null) {
            onDismissListener.onListen();
        } else {
            showNext();
        }
    }

    protected void onDialogPause() {
        if (onPauseListener != null) {
            onPauseListener.onListen();
        }
    }

    protected void onDialogResume() {
        if (onResumeListener != null) {
            onResumeListener.onListen();
        }
    }

    protected void onDialogCancel() {
        if (onCancelListener != null) {
            onCancelListener.onListen();
        }
    }

    protected void onDialogShow() {
        if (onShowListener != null) {
            onShowListener.onListen();
        }
    }

    public void showDialog(FragmentActivity mActivity) {
        mActivitys = new WeakReference<>(mActivity);
//        if (isAlreadyShown) {
//            return;
//        }
        isAlreadyShown = true;
        dismissedFlag = false;
        initEvent();
        dialogList.add(this);
//        showNow();
        showNext();
    }

    protected void showNext() {
        List<BaseNDialog> cache = new ArrayList<>(BaseNDialog.dialogList);
        for (BaseNDialog dialog : cache) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (dialog.mActivitys.get().isDestroyed()) {
                    ViseLog.e("# 由于 context 已被回收，卸载Dialog：" + dialog);
                    dialogList.remove(dialog);
                }
            }
        }
        for (BaseNDialog dialog : dialogList) {
            if (dialog.isShow) {
                ViseLog.showInfo("# 启动中断：已有正在显示的Dialog：" + dialog);
                return;
            }
        }
        for (BaseNDialog dialog : dialogList) {
            dialog.showNow();
            return;
        }
    }

    private void showNow() {
        isShow = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (mActivitys.get() == null || mActivitys.get().isDestroyed()) {
                if (newContext == null || newContext.get() == null) {
                    ViseLog.e("Context错误的指向了一个已被关闭的Activity或者Null，有可能是Activity因横竖屏切换被重启或者您手动执行了unload()方法，请确认其能够正确指向一个正在使用的Activity");
                    return;
                }
                mActivitys = new WeakReference<>(newContext.get());
            }
        }
        FragmentManager fragmentManager = mActivitys.get().getSupportFragmentManager();
        //创建dialogFragment开始
        dialog = new WeakReference<>(new BaseNDialogFragment().setLayoutId(this, getLayoutId(), getDialogStyle()));
        dialog.get().show(fragmentManager, getClass().getSimpleName() + hashCode());
    }

    public void doDismiss() {
        dismissedFlag = true;
        if (dialog != null && dialog.get() != null) {
            dialog.get().dismiss();
        }
    }
}
