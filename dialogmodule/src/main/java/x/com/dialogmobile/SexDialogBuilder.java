package x.com.dialogmobile;

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
import android.widget.TextView;

public class SexDialogBuilder {
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
     * 缩放动画
     */
    public static final int DIALOG_ANIM_NORMAL = R.style.DialogAnimation;
    /**
     * 从下往上滑动动画
     */
    public static final int DIALOG_ANIM_SLID_BOTTOM = R.style.DialogAnimationSlidBottom;
    /**
     * 从上往下滑动动画
     */
    public static final int DIALOG_ANIM_SLID_TOP = R.style.DialogAnimationSlidTop;
    /**
     * 从右往左滑动动画
     */
    public static final int DIALOG_ANIM_SLID_RIGHT = R.style.DialogAnimationSlidRight;
    /**
     * 对话框透明比例
     */
    public static final float ALPHAFACTOR = 1.0f;
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
    private TextView dialogTitle, dialogMsg;
    private float alpha;//点击透明度

    /**
     * 构造器
     *
     * @param context          上下文
     * @param layoutStyle      对话框布局样式
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    public SexDialogBuilder(Context context, int layoutStyle, float widthcoefficient) {
        this(context, layoutStyle, false, widthcoefficient, ALPHAFACTOR, dimEnable);
    }

    /**
     * 构造器
     *
     * @param context
     * @param layoutStyle      布局样式
     * @param isSystemAlert    是否是系统弹框（service等地方用到系统级别不依赖activity）
     * @param widthcoefficient 对话框宽度所占屏幕宽度的比重（0-1）
     * @param alpha            对话框透明度
     */
    private SexDialogBuilder(Context context, int layoutStyle, boolean isSystemAlert, float widthcoefficient, float alpha, boolean dimEnable) {
        Dialog dialog;
        if (dimEnable) {
            dialog = new Dialog(context, R.style.Dialog);
        } else {
            dialog = new Dialog(context, R.style.DialogDim);
        }
        // 设置对话框风格
        if (layoutStyle == 0) {
            layoutStyle = R.layout.sexdialog_layout;
        }
        dialog.setContentView(layoutStyle);
        Window window = dialog.getWindow();
        // 是否系统级弹框
        if (isSystemAlert) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        // 获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenwidth = metrics.widthPixels;
        int width = 0;
        if (widthcoefficient > 0) {
            width = (int) (screenwidth * widthcoefficient);
        } else {
            width = (int) (screenwidth * WIDTHFACTOR);
        }
        // 设置对话框宽度
        window.getAttributes().width = width;

        // 设置透明
        WindowManager.LayoutParams lp = window.getAttributes();
        if (alpha > 0) {
            lp.alpha = 1.0f;
        } else {
            lp.alpha = ALPHAFACTOR;
        }
        window.setAttributes(lp);
        this.dialog = dialog;
        this.context = context;
        this.alpha = 0.8f;
    }


    //属性设置
    public SexDialogBuilder setTouchOutSideCancelable(boolean touchOutSideCancel) {
        this.dialog.setCanceledOnTouchOutside(touchOutSideCancel);
        this.dialog.setCancelable(touchOutSideCancel);
        return this;
    }


    /**
     * 给对话框设置动画
     *
     * @param resId
     */
    public SexDialogBuilder setDialogAnimation(int resId) {
        this.dialog.getWindow().setWindowAnimations(resId);
        return this;
    }

    /**
     * 设置对话框的位置
     *
     * @param location
     * @return
     */
    public SexDialogBuilder setDialoglocation(int location) {
        Window window = this.dialog.getWindow();
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
        return this;
    }

    /**
     * 设置点击时透明度，
     *
     * @param alpah 透明度
     * @return this
     */
    public SexDialogBuilder setClickAlpah(float alpah) {
        this.alpha = alpah;
        return this;
    }

    /**
     * 监听器监听对话框按钮点击
     *
     * @author zhl
     */
    public interface onDialogbtnClickListener {

        int BUTTON_BOY = 1;
        int BUTTONGIRL = 2;

        /**
         * @param context  上下文
         * @param dialog   点击的哪个对话框
         * @param whichBtn 点击的哪个按钮
         */
        void onDialogbtnClick(Context context, Dialog dialog, int whichBtn);

    }

    //默认一个按钮
    public SexDialogBuilder setBtnClickListener(final boolean isDissmiss, final SexDialogBuilder.onDialogbtnClickListener btnClickListener) {
        return this.setClickListener(isDissmiss, btnClickListener);
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @param isDissmiss       点击按钮后是否取消对话框
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    private SexDialogBuilder setClickListener(final boolean isDissmiss, final onDialogbtnClickListener btnClickListener) {
        final TextView tv_boy = dialog.findViewById(R.id.tv_boy);
        // 给按钮绑定监听器
        tv_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDissmiss) {
                    dialog.dismiss();
                }
                if (btnClickListener != null) {
                    btnClickListener.onDialogbtnClick(context, dialog,
                            onDialogbtnClickListener.BUTTON_BOY);


                }
            }
        });
        tv_boy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tv_boy.setAlpha(alpha);
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_boy.setAlpha(1f);
                        break;
                }
                return false;
            }
        });

        final TextView tv_girl = dialog.findViewById(R.id.tv_girl);
        tv_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDissmiss) {
                    dialog.dismiss();
                }
                if (btnClickListener != null) {
                    btnClickListener.onDialogbtnClick(context, dialog,
                            onDialogbtnClickListener.BUTTONGIRL);
                }
            }
        });
        tv_girl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tv_girl.setAlpha(alpha);
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_girl.setAlpha(1f);
                        break;
                }
                return false;
            }
        });

        final TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        // 给按钮绑定监听器
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDissmiss) {
                    dialog.dismiss();
                }
                if (btnClickListener != null) {
                    dialog.dismiss();
                }
            }
        });
        tv_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tv_cancel.setAlpha(alpha);
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_cancel.setAlpha(1f);
                        break;
                }
                return false;
            }
        });
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
