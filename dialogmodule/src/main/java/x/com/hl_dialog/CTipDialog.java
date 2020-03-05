package x.com.hl_dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_dialog.base.BaseNDialog;
import x.com.hl_log.ViseLog;

public class CTipDialog extends BaseNDialog {
    /**
     * 对话框模版样式
     */
    public static final int nDialog_Iphone = R.layout.ndialog_iphone;
    public static final int nDialog_Iphone_Scroll = R.layout.ndialog_iphone_scroll;
    public static final int nDialog_Sex = R.layout.ndialog_sex;
    public static final int nDialog_Share = R.layout.ndialog_share;
    public static final int nDialog_Photo = R.layout.ndialog_photo;
    public static final int nDialog_Update = R.layout.ndialog_update;
    public static final int nDialog_ListFile = R.layout.ndialog_listfile;
    public static final int nDialog_Normal = R.layout.ndialog_layout;
    public static final int ndialog_Trans = R.layout.ndialog_trans;

    private FragmentActivity mActivity;
    private int layoutId;
    private float clickAlpha = 0.8f;//点击透明度
    private boolean outside, cancelable;
    private int location;//显示位置
    private float widthScale = 1f;
    public boolean isShowClose;
    private String title, message;
    private int titlePosition, messagePosition;
    private List<String> btnText = new ArrayList<>();
    private onDialogbtnClickListener btnClickListener;
    private TextView dialogMsg;

    public CTipDialog(FragmentActivity mActivity, int layoutId) {
        this.mActivity = mActivity;
        this.layoutId = layoutId;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (isShowClose) {
            ImageView nDialogClose = mView.findViewById(R.id.nDialogClose);
            nDialogClose.setVisibility(View.VISIBLE);
            nDialogClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(title)) {
            TextView dialogTitle = mView.findViewById(R.id.nDialogTitle);
            dialogTitle.setText(title);
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setGravity(titlePosition);
        }
        if (!TextUtils.isEmpty(message)) {
            dialogMsg = mView.findViewById(R.id.nDialogMessage);
            dialogMsg.setText(message);
            dialogMsg.setVisibility(View.VISIBLE);
            dialogMsg.setGravity(messagePosition);
        }
        // 设置确认按钮
        for (int i = 0; i < btnText.size(); i++) {
            final TextView mBtn = mView.findViewWithTag("nDialogBtn" + (i + 1));

            if (mBtn == null) {
                ViseLog.e("请检查id是否正确，将第" + i + "个按钮id设置为mbtn" + i);
            } else {
                mBtn.setText(btnText.get(i));
                mBtn.setVisibility(View.VISIBLE);
                // 给按钮绑定监听器
                int finalI = i;
                mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnClickListener != null) {
                            if (btnClickListener.onDialogBtnClick(mActivity, mBtn.getText().toString(), finalI + 1)) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
                mBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mBtn.setAlpha(clickAlpha);
                                break;
                            case MotionEvent.ACTION_UP:
                                mBtn.setAlpha(1f);
                                break;
                        }
                        return false;
                    }
                });
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return layoutId;
    }

    @Override
    public float getWidthScale() {
        return widthScale;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public boolean isCanceledOnTouchOutside() {
        return outside;
    }

    //属性设置
    public CTipDialog setTouchOutSideCancelable(boolean outside, boolean cancelable) {
        this.outside = outside;
        this.cancelable = cancelable;
        return this;
    }

    public CTipDialog setWidthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * 设置显示关闭按钮
     * isShowClose 是否显示关闭按钮
     *
     * @return this
     */
    public CTipDialog isShowClose(boolean isShowClose) {
        this.isShowClose = isShowClose;
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 标题
     * @return this
     */
    public CTipDialog setTitle(String title, int position) {
        this.title = title;
        this.titlePosition = position;
        return this;
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message  消息内容
     * @param position 文字对齐方式
     * @return this
     */
    public CTipDialog setMessage(String message, int position) {
        this.message = message;
        this.messagePosition = position;
        return this;
    }

    public void refresh(String text,int position) {
        if (dialogMsg != null) {
            dialogMsg.setText(text);
            dialogMsg.setGravity(position);
        }
    }

    /**
     * 设置对话框的位置
     *
     * @param location 位置。例如Gravity.Center
     * @return this
     */
    public CTipDialog setDialogLocation(int location) {
        this.location = location;
        return this;
    }

    @Override
    public int getGravity() {
        return location;
    }

    /**
     * 监听器监听对话框按钮点击
     *
     * @author zhl
     */
    public interface onDialogbtnClickListener {
        boolean onDialogBtnClick(Context context, String text, int whichBtn);
    }

    //一个按钮
    public CTipDialog setBtnClickListener(String btn1text,
                                          final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(btn1text, null, null, null, null, btnClickListener);
    }

    //两个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text,
                                          final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, null, null, null, btnClickListener);
    }

    //三个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String btn3text,
                                          final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, null, null, btnClickListener);
    }

    //四个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, null, btnClickListener);
    }

    //五个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, String btn5text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, btn5text, btnClickListener);
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @return
     */
    private CTipDialog setClickListener(String btn1text, String btn2text, String btn3text,
                                        String btn4text, String btn5text, final onDialogbtnClickListener btnClickListener) {
        setBtnListener(btn1text);
        setBtnListener(btn2text);
        setBtnListener(btn3text);
        setBtnListener(btn4text);
        setBtnListener(btn5text);
        this.btnClickListener = btnClickListener;
        return this;
    }

    private void setBtnListener(String btnText) {
        if (!TextUtils.isEmpty(btnText)) {
            this.btnText.add(btnText);
        }
    }
}
