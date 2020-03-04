package x.com.hl_base.hl_fragment;

import android.app.Dialog;
import android.os.Build;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.hl_interface.OnSimpleListener;
import x.com.hl_log.ViseLog;

public abstract class BaseDialog {
    protected static WeakReference<FragmentActivity> newContext;
    protected static List<BaseDialog> dialogList = new ArrayList<>();//对话框队列
    public WeakReference<FragmentActivity> context;
    public WeakReference<BaseDialogFragment3> dialog;//我才是本体！
    private BaseDialog baseDialog;
    protected boolean dismissedFlag = false;
    private boolean isAlreadyShown;
    private boolean isShow;
    protected OnSimpleListener onDismissListener;
    protected OnSimpleListener dismissEvent;

    protected abstract void initUI(View mView);

    protected abstract int getLayoutId();

    protected void initDefaultSettings() {

    }

    protected void showDialog(FragmentActivity mActivity) {
        //只有这里创建的，才可以修改主题
        this.baseDialog = this;
        context = new WeakReference<>(mActivity);
        if (isAlreadyShown) {
            return;
        }
        isAlreadyShown = true;
        dismissedFlag = false;
//        if (DialogSettings.dialogLifeCycleListener != null)
//            DialogSettings.dialogLifeCycleListener.onCreate(this);
//        styleId = style;
        dismissEvent = new OnSimpleListener() {
            @Override
            public void onListen() {
                dismissedFlag = true;
                isShow = false;
                if (onDismissListener != null) onDismissListener.onListen();
                dialogList.remove(baseDialog);
                showNext();
            }
        };
        dialogList.add(this);
//        showNow();
        showNext();
    }

    protected void showNext() {
        List<BaseDialog> cache = new ArrayList<>(BaseDialog.dialogList);
        for (BaseDialog dialog : cache) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (dialog.context.get().isDestroyed()) {
                    ViseLog.e("# 由于 context 已被回收，卸载Dialog：" + dialog);
                    dialogList.remove(dialog);
                }
            }
        }
        for (BaseDialog dialog : dialogList) {
            if (dialog.isShow) {
                ViseLog.showInfo("# 启动中断：已有正在显示的Dialog：" + dialog);
                return;
            }
        }
        for (BaseDialog dialog : dialogList) {
            dialog.showNow();
            return;
        }
    }

    private void showNow() {
        isShow = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (context.get() == null || context.get().isDestroyed()) {
                if (newContext == null || newContext.get() == null) {
                    ViseLog.e("Context错误的指向了一个已被关闭的Activity或者Null，有可能是Activity因横竖屏切换被重启或者您手动执行了unload()方法，请确认其能够正确指向一个正在使用的Activity");
                    return;
                }
                context = new WeakReference<>(newContext.get());
            }
        }
        FragmentManager fragmentManager = context.get().getSupportFragmentManager();
        //创建dialogFragment开始
        dialog = new WeakReference<>(new BaseDialogFragment3().setLayoutId(baseDialog, getLayoutId()));

//        dialog.get().setStyle(DialogFragment.STYLE_NORMAL, styleId);
        dialog.get().show(fragmentManager, "kongzueDialog");

        dialog.get().setOnShowListener(new BaseDialogFragment3.PreviewOnShowListener() {
            @Override
            public void onShow(Dialog dialog) {
//                showEvent();

            }
//                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        boolean flag = false;
//                        if (onBackClickListener != null) {
//                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                                return flag = onBackClickListener.onBackClick();
//                            }
//                        }
//                        return flag;
//                    }
//                });
//            }
//        });
//        if (DialogSettings.systemDialogStyle == 0 && style == DialogSettings.STYLE.STYLE_IOS && !(baseDialog instanceof TipDialog) && !(baseDialog instanceof BottomMenu) && !(baseDialog instanceof ShareDialog)) {
//            dialog.get().setAnim(R.style.iOSDialogAnimStyle);
//        }
//
//        if (baseDialog instanceof TipDialog) {
//            if (cancelable == null)
//                cancelable = DialogSettings.cancelableTipDialog ? BOOLEAN.TRUE : BOOLEAN.FALSE;
//        } else {
//            if (cancelable == null)
//                cancelable = DialogSettings.cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
//        }
//        dialog.get().setCancelable(cancelable == BOOLEAN.TRUE);
        });
    }
}
