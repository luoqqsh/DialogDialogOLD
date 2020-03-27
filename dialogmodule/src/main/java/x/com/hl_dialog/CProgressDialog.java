package x.com.hl_dialog;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_view.CircleProgressBar;

public class CProgressDialog extends BaseNDialog {
    private CircleProgressBar roundProgressBar;
    private TextView progressTips;
    private int totalProgress = 0;
    private String tip;

    public static CProgressDialog newInstance(String tip) {
        return newInstance(tip, 0);
    }

    public static CProgressDialog newInstance(String tip, int total) {
        CProgressDialog fragment = new CProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString("tip", tip);
        bundle.putInt("total", total);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void initDialog(String tip, int total) {
        this.tip = tip;
        totalProgress = total;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public boolean isCanceledOnTouchOutside() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ndialog_progress;
    }

    @Override
    protected void initUI(View layout, Bundle bundle) {
        if (bundle != null) {
            tip = bundle.getString("tip");
            totalProgress = bundle.getInt("total", 0);
        }
        roundProgressBar = layout.findViewById(R.id.cProgressBar);
        progressTips = layout.findViewById(R.id.cProgressTip);
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
    protected void onDialogDismiss() {
        super.onDialogDismiss();
        setProgress(0);
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
