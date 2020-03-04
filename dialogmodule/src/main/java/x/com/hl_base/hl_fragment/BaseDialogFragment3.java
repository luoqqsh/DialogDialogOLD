package x.com.hl_base.hl_fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

public class BaseDialogFragment3 extends DialogFragment {
    private int layoutId;
    private View rootView;
    private String parentId;
    private WeakReference<BaseDialog> parent;
    private PreviewOnShowListener onShowListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getDialog() == null) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
    }

    //parentId：因不可抗力，诸如横竖屏切换、分屏、折叠屏切换等可能造成DialogFragment重启，为保证重启后功能正常运行，因此需要保存自己爹（BaseDialog）的id，方便重新绑定到BaseDialog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt("layoutId");
            parentId = savedInstanceState.getString("parentId");
        }
        super.onCreate(savedInstanceState);
    }

    public BaseDialogFragment3 setLayoutId(BaseDialog baseDialog, int layoutId) {
        this.layoutId = layoutId;
        this.parentId = baseDialog.toString();
        this.parent = new WeakReference<>(baseDialog);
        return this;
    }

    public interface PreviewOnShowListener {
        void onShow(Dialog dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutId == 0) {
            if (onShowListener != null) onShowListener.onShow(getDialog());
            findMyParentAndBindView(null);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        rootView = inflater.inflate(layoutId, null);
        findMyParentAndBindView(rootView);
        return rootView;
    }

    //找爸爸行动
    private void findMyParentAndBindView(View rootView) {
        List<BaseDialog> cache = new ArrayList<>(BaseDialog.dialogList);
        BaseDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseDialog baseDialog : cache) {
            baseDialog.context = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                refreshDialogPosition(getDialog());
                parent.get().initUI(rootView);
                parent.get().initDefaultSettings();
            }
        }
    }

    //刷新window相关设置
    private void refreshDialogPosition(Dialog dialog) {
        if (dialog != null && parent != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //透明状态栏
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = display.getWidth();
                lp.height = display.getHeight();
                lp.windowAnimations = R.style.bottomMenuAnim;
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.bottomMenuAnim);
                window.setAttributes(lp);

            }
        }
    }

    private boolean findMyParent() {
        boolean flag = false;
        List<BaseDialog> cache = new ArrayList<>(BaseDialog.dialogList);
        BaseDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseDialog baseDialog : cache) {
            baseDialog.context = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                flag = true;
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                refreshDialogPosition(getDialog());
            }
        }
        return flag;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("layoutId", layoutId);
        outState.putString("parentId", parentId);
        super.onSaveInstanceState(outState);
    }

    public void setOnShowListener(PreviewOnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null && parent.get().dismissEvent != null) {
            parent.get().dismissEvent.onListen();
        }
        super.onDismiss(dialog);
        parent.clear();
        parent = null;
        ViseLog.d("onDismiss");
    }

    @Override
    public void onPause() {
        super.onPause();
        ViseLog.d("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null) {
            if (parent.get().dismissedFlag) {
                dismiss();
            }
        }
    }
    @Override
    public void onStart()
    {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
//        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );
    }
}
