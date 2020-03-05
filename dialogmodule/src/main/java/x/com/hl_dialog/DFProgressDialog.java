package x.com.hl_dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import x.com.hl_dialog.base.BaseDialogFragment2;
import x.com.hl_base.hl_interface.OnSimpleListener;
import x.com.hl_view.CircleProgressBar;

public class DFProgressDialog extends BaseDialogFragment2 {
    private CircleProgressBar roundProgressBar;
    private TextView progressTips;
    private int totalProgress = 0;
    private OnSimpleListener onCancelListener;
    private String tip;

    public static DFProgressDialog newInstance(String tip) {
        return newInstance(tip, 0);
    }

    public static DFProgressDialog newInstance(String tip, int total) {
        DFProgressDialog fragment = new DFProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString("tip", tip);
        bundle.putInt("total", total);
        fragment.setArguments(bundle);
        return fragment;
    }

    public DFProgressDialog setOnCancelListener(OnSimpleListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public void onStart() {
        super.onStart();
//        setCanceledOnTouchOutside(false);//触摸不可取消，按返回可以取消
        if (onCancelListener != null) {
//            setOnKeyListener((dialog, keyCode, event) -> {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    //按返回键时关闭弹窗的同时，取消操作
//                    dismiss();
//                    onCancelListener.onListen();
//                    return true;
//                }
//                return false;
//            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ndialog_progress;
    }

    @Override
    protected void initUI(View layout) {
        if (getArguments() != null) {
            tip = getArguments().getString("tip");
            totalProgress = getArguments().getInt("total", 0);
        }
        roundProgressBar = layout.findViewById(R.id.dp_progress_round_bar);
        progressTips = layout.findViewById(R.id.dp_progress_tips);
        roundProgressBar.setMax(100);
        roundProgressBar.setProgress(0);
        progressTips.setText(tip);
        progressTips.setVisibility(TextUtils.isEmpty(tip) ? View.INVISIBLE : View.VISIBLE);
    }

    public void showProgressDialog(int progress) {
        showProgressDialog(progress, 0, tip);
    }

    public void showProgressDialog(int progress, String tip) {
        showProgressDialog(progress, 0, tip);
    }

    /**
     * 多步骤时处理进度，需要
     *
     * @param progress 当前步骤进度，需要init传入总进度，否则只显示当前步骤进度
     * @param index    当前第几个步骤，这个需要初始化的时候传入总进度才有效（从0开始）
     * @param tip      提示语
     */
    public void showProgressDialog(Integer progress, int index, String tip) {
        this.tip = tip;
        if (totalProgress != 0) {
            progress = (int) ((index * 100 + progress.floatValue()) / totalProgress * 100);
        }
        setProgress(progress);
        setTip(tip);
    }

    @Override
    public void dismiss() {
        setProgress(0);
        super.dismiss();
    }

    private void setProgress(float progress) {
        if (roundProgressBar != null) {
            roundProgressBar.setProgress((int) progress);
        }
    }

    private void setTip(String tips) {
        if (roundProgressBar != null && !progressTips.getText().equals(tips)) {
            progressTips.setText(tips);
        }
    }
}
