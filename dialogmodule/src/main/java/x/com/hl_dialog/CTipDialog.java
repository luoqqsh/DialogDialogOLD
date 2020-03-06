package x.com.hl_dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.hl_dialog.BaseNDialog;
import x.com.hl_base.hl_interface.OnClickItemListener;
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
    public static final int ndialog_Input = R.layout.ndialog_input;

    private int layoutId;
    private float clickAlpha = 0.8f;//点击透明度
    private boolean outside, cancelable;
    private int location;//显示位置
    private float widthScale = 1f;
    public boolean isShowClose;
    private String title, message;
    private int titlePosition, messagePosition;
    private List<String> btnText = new ArrayList<>();
    private OnClickItemListener btnClickListener;
    private TextView dialogMsg;
    private EditText nDialogInput;

    public CTipDialog(int layoutId) {
        this.layoutId = layoutId;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (isShowClose) {
            ImageView nDialogClose = mView.findViewById(R.id.nDialogClose);
            nDialogClose.setVisibility(View.VISIBLE);
            nDialogClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
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
        nDialogInput = mView.findViewById(R.id.nDialogInput);
        // 设置确认按钮
        for (int i = 0; i < btnText.size(); i++) {
            final TextView nDialogBtn = mView.findViewWithTag("nDialogBtn" + (i + 1));

            if (nDialogBtn == null) {
                ViseLog.e("请检查Tag是否正确，将第" + i + "个按钮Tag设置为nDialogBtn" + i);
            } else {
                nDialogBtn.setText(btnText.get(i));
                nDialogBtn.setVisibility(View.VISIBLE);
                // 给按钮绑定监听器
                int finalI = i;
                nDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnClickListener != null) {
                            if (btnClickListener.onItemClick(nDialogBtn.getText().toString(), finalI + 1)) {
                                dismiss();
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
                nDialogBtn.setOnTouchListener((view, motionEvent) -> {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            nDialogBtn.setAlpha(clickAlpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            nDialogBtn.setAlpha(1f);
                            break;
                    }
                    return false;
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

    public void refresh(String text, int position) {
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

    public String getInputText() {
        if (nDialogInput != null) {
            return nDialogInput.getText().toString();
        } else {
            ViseLog.e("找不到输入框控件，检查控件id是否是nDialogInput");
            return "";
        }
    }

    @Override
    public int getGravity() {
        return location;
    }

    //一个按钮
    public CTipDialog setBtnClickListener(String btn1text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, null, null, null, null, btnClickListener);
    }

    //两个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, null, null, null, btnClickListener);
    }

    //三个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String btn3text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, null, null, btnClickListener);
    }

    //四个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, null, btnClickListener);
    }

    //五个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, String btn5text, final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, btn5text, btnClickListener);
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @return
     */
    private CTipDialog setClickListener(String btn1text, String btn2text, String btn3text,
                                        String btn4text, String btn5text, final OnClickItemListener btnClickListener) {
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
