package x.com.hl_dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vise.log.ViseLog;

public class NDialog {
    /**
     * 对话框模版样式
     */
    public static final int NDialog_Iphone = R.layout.ndialog_iphone;
    public static final int nDialog_Sex = R.layout.ndialog_sex;
    public static final int nDialog_Share = R.layout.ndialog_share;
    public static final int nDialog_Photo = R.layout.ndialog_photo;
    public static final int nDialog_Update = R.layout.ndialog_update;
    /**
     * 对话框处于屏幕顶部位置
     */
    public static final int DIALOG_LOCATION_TOP = 12;
    /**
     * 对话框处于屏幕中间位置
     */
    public static final int DIALOG_LOCATION_CENTER = 10;
    /**
     * 对话框处于屏幕底部位置
     */
    public static final int DIALOG_LOCATION_BOTTOM = 11;
    /**
     * 消息位于对话框的位置 居左
     */
    public static final int MSG_LAYOUT_LEFT = 1;
    /**
     * 消息位于对话框的位置 居中
     */
    public static final int MSG_LAYOUT_CENTER = 0;
    /**
     * 动画
     */
    public static final int DIALOG_ANIM_NORMAL = R.style.DialogAnimation;
    //从下往上滑动动画
    public static final int DIALOG_ANIM_SLID_BOTTOM = R.style.DialogAnimationSlidBottom;
    //从上往下滑动动画
    public static final int DIALOG_ANIM_SLID_TOP = R.style.DialogAnimationSlidTop;
    //从右往左滑动动画
    public static final int DIALOG_ANIM_SLID_RIGHT = R.style.DialogAnimationSlidRight;

    // 弹出dialog时候是否要显示阴影
    private static boolean dimEnable = true;
    /**
     * 对话框宽度所占屏幕宽度的比例
     */
    public static final float WIDTHFACTOR = 0.75f;
    /**
     * Dialog对象
     */
    private Dialog dialog;
    private Context context;
    private float alpha = 0.8f;//点击透明度

    /**
     * 构造器
     *
     * @param context          上下文
     * @param layoutStyle      对话框布局样式
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    public NDialog(Context context, int layoutStyle, float widthcoefficient) {
        this(context, layoutStyle, widthcoefficient, dimEnable);
    }

    /**
     * 构造器
     *
     * @param context          上下文
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    public NDialog(Context context, float widthcoefficient) {
        this(context, R.layout.ndialog_default, widthcoefficient, dimEnable);
    }

    /**
     * 构造器
     *
     * @param layoutStyle      布局样式
     * @param widthcoefficient 对话框宽度所占屏幕宽度的比重（0-1）
     * @param dimEnable        是否显示阴影
     */
    private NDialog(Context context, int layoutStyle, float widthcoefficient, boolean dimEnable) {
        Dialog dialog;
        if (dimEnable) {
            dialog = new Dialog(context, R.style.Dialog);
        } else {
            dialog = new Dialog(context, R.style.DialogDim);
        }
        dialog.setContentView(layoutStyle);

        Window window = dialog.getWindow();
        // 获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenwidth = metrics.widthPixels;
        int width;
        if (widthcoefficient > 0) {
            width = (int) (screenwidth * widthcoefficient);
        } else {
            width = (int) (screenwidth * WIDTHFACTOR);
        }
        // 设置对话框宽度
        window.getAttributes().width = width;
        this.dialog = dialog;
        this.context = context;
    }


    //属性设置
    public NDialog setTouchOutSideCancelable(boolean outside, boolean cancelable) {
        this.dialog.setCanceledOnTouchOutside(outside);
        this.dialog.setCancelable(cancelable);
        return this;
    }

    /**
     * 设置显示关闭按钮
     * isshowclose 是否显示关闭按钮
     *
     * @return this
     */
    public NDialog isshowclose(boolean isshowclose) {
        if (isshowclose) {
            ImageView imgv_close = dialog.findViewById(R.id.imgv_close);
            if (imgv_close == null) {
                ViseLog.e("请检查id是否正确，将关闭按钮id设置为imgv_close");
                return null;
            }
            imgv_close.setVisibility(View.VISIBLE);
            imgv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return this;
    }

    /**
     * 设置是否系统弹窗
     *
     * @return this
     */
    public NDialog setsystemalert(boolean isSystemAlert) {
        if (isSystemAlert) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 标题
     * @return this
     */
    public NDialog setTitle(String title, int layout) {
        TextView dialogTitle = dialog.findViewById(R.id.mdialog_title);
        if (dialogTitle == null) {
            ViseLog.e("请检查id是否正确，将标题id设置为mdialog_title");
            return null;
        }
        if (title != null) {
            dialogTitle.setText(title);
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setGravity(layout);
        } else {
            dialogTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message 消息内容
     * @param layout  文字对齐方式
     * @return this
     */
    public NDialog setMessage(String message, int layout) {
        TextView dialogMsg = dialog.findViewById(R.id.mdialog_message);
        if (dialogMsg == null) {
            ViseLog.e("请检查id是否正确，将消息内容id设置为mdialog_message");
            return null;
        }
        if (message != null) {
            dialogMsg.setText(message);
            dialogMsg.setVisibility(View.VISIBLE);
            dialogMsg.setGravity(layout);
        } else {
            dialogMsg.setVisibility(View.GONE);
        }

        return this;
    }


    /**
     * 给对话框设置动画
     */
    public NDialog setDialogAnimation(int resId) {
        dialog.getWindow().setWindowAnimations(resId);
        return this;
    }

    /**
     * 设置对话框的位置
     *
     * @param location
     * @return
     */
    public NDialog setDialoglocation(int location) {
        Window window = this.dialog.getWindow();
        if (window != null) {
            switch (location) {
                case DIALOG_LOCATION_CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                case DIALOG_LOCATION_BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    break;
                case DIALOG_LOCATION_TOP:
                    window.setGravity(Gravity.TOP);
                    break;
            }
        }
        return this;
    }

    /**
     * 监听器监听对话框按钮点击
     *
     * @author zhl
     */
    public interface onDialogbtnClickListener {
        int BUTTON_1 = 1;
        int BUTTON_2 = 2;
        int BUTTON_3 = 3;
        int BUTTON_4 = 4;
        int BUTTON_5 = 5;

        void onDialogbtnClick(Context context, Dialog dialog, int whichBtn);
    }

    //一个按钮
    public NDialog setBtnClickListener(final boolean isDissmiss, String btn1text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btn1text, null, null, null, null, btnClickListener);
    }

    //两个按钮
    public NDialog setBtnClickListener(final boolean isDissmiss, String btn1text, String btn2text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btn1text, btn2text, null, null, null, btnClickListener);
    }

    //三个按钮
    public NDialog setBtnClickListener(final boolean isDissmiss, String btn1text, String btn2text, String btn3text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btn1text, btn2text, btn3text, null, null, btnClickListener);
    }

    //四个按钮
    public NDialog setBtnClickListener(final boolean isDissmiss, String btn1text, String btn2text, String btn3text, String btn4text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btn1text, btn2text, btn3text, btn4text, null, btnClickListener);
    }

    //五个按钮
    public NDialog setBtnClickListener(final boolean isDissmiss, String btn1text, String btn2text, String btn3text, String btn4text, String btn5text, final onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btn1text, btn2text, btn3text, btn4text, btn5text, btnClickListener);
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @param isDissmiss       点击按钮后是否取消对话框
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    private NDialog setClickListener(final boolean isDissmiss, String btn1text, String btn2text, String btn3text, String btn4text, String btn5text, final onDialogbtnClickListener btnClickListener) {
        if (btn1text != null) {
            // 设置确认按钮
            final TextView mbtn1 = dialog.findViewById(R.id.mbtn1);
            if (mbtn1 == null) {
                ViseLog.e("请检查id是否正确，将第一个按钮id设置为mbtn1");
                return null;
            }
            mbtn1.setText(btn1text);
            mbtn1.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mbtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDissmiss) {
                        dialog.dismiss();
                    }
                    if (btnClickListener != null) {
                        btnClickListener.onDialogbtnClick(context, dialog,
                                onDialogbtnClickListener.BUTTON_1);
                    }
                }
            });
            mbtn1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mbtn1.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            mbtn1.setAlpha(1f);
                            break;
                    }
                    return false;
                }
            });
        }
        if (btn2text != null) {
            // 设置确认按钮
            final TextView mbtn2 = dialog.findViewById(R.id.mbtn2);
            if (mbtn2 == null) {
                ViseLog.e("请检查id是否正确，将第二个按钮id设置为mbtn2");
                return null;
            }
            mbtn2.setText(btn2text);
            mbtn2.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mbtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDissmiss) {
                        dialog.dismiss();
                    }
                    if (btnClickListener != null) {
                        btnClickListener.onDialogbtnClick(context, dialog,
                                onDialogbtnClickListener.BUTTON_2);
                    }
                }
            });
            mbtn2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mbtn2.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            mbtn2.setAlpha(1f);
                            break;
                    }
                    return false;
                }
            });
        }
        if (btn3text != null) {
            // 设置确认按钮
            final TextView mbtn3 = dialog.findViewById(R.id.mbtn3);
            if (mbtn3 == null) {
                ViseLog.e("请检查id是否正确，将第三个按钮id设置为mbtn3");
                return null;
            }
            mbtn3.setText(btn3text);
            mbtn3.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mbtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDissmiss) {
                        dialog.dismiss();
                    }
                    if (btnClickListener != null) {
                        btnClickListener.onDialogbtnClick(context, dialog,
                                onDialogbtnClickListener.BUTTON_3);
                    }
                }
            });
            mbtn3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mbtn3.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            mbtn3.setAlpha(1f);
                            break;
                    }
                    return false;
                }
            });
        }
        if (btn4text != null) {
            // 设置确认按钮
            final TextView mbtn4 = dialog.findViewById(R.id.mbtn4);
            if (mbtn4 == null) {
                ViseLog.e("请检查id是否正确，将第四个按钮id设置为mbtn4");
                return null;
            }
            mbtn4.setText(btn4text);
            mbtn4.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mbtn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDissmiss) {
                        dialog.dismiss();
                    }
                    if (btnClickListener != null) {
                        btnClickListener.onDialogbtnClick(context, dialog,
                                onDialogbtnClickListener.BUTTON_4);
                    }
                }
            });
            mbtn4.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mbtn4.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            mbtn4.setAlpha(1f);
                            break;
                    }
                    return false;
                }
            });
        }

        if (btn5text != null) {
            // 设置确认按钮
            final TextView mbtn5 = dialog.findViewById(R.id.mbtn5);
            if (mbtn5 == null) {
                ViseLog.e("请检查id是否正确，将第四个按钮id设置为mbtn4");
                return null;
            }
            mbtn5.setText(btn5text);
            mbtn5.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mbtn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDissmiss) {
                        dialog.dismiss();
                    }
                    if (btnClickListener != null) {
                        btnClickListener.onDialogbtnClick(context, dialog,
                                onDialogbtnClickListener.BUTTON_5);
                    }
                }
            });
            mbtn5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mbtn5.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            mbtn5.setAlpha(1f);
                            break;
                    }
                    return false;
                }
            });
        }
        return this;
    }

    /**
     * 创建对话框
     *
     * @return dialog
     */
    public Dialog create() {
        if (context instanceof Activity) {
            dialog.setOwnerActivity((Activity) context);
        }
        return dialog;
    }
}
