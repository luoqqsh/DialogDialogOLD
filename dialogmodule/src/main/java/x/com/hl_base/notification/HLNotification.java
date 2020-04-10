package x.com.hl_base.notification;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.textservice.TextInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import x.com.hl_base.interfaces.OnSimpleListener;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;

public class HLNotification {

    public enum DURATION_TIME {
        SHORT, LONG
    }

    private OnSimpleListener onNotificationClickListener;
    private OnSimpleListener onDismissListener;

    private DURATION_TIME durationTime = DURATION_TIME.LONG;
    private int backgroundColor;

    private Toast toast;
    private WeakReference<FragmentActivity> context;
    private String title;
    private String message;
    private int iconResId;

    private View customView;
    private NotifyToastShadowView rootView;

    private RelativeLayout boxBody;
    private LinearLayout btnNotic;
    private LinearLayout boxTitle;
    private ImageView imgIcon;
    private TextView txtTitle;
    private TextView txtMessage;
    private RelativeLayout boxCustom;

    private TextInfo titleTextInfo;
    private TextInfo messageTextInfo;

    private HLNotification() {
    }

    public static HLNotification build(FragmentActivity context, String message) {
        synchronized (HLNotification.class) {
            HLNotification HLNotification = new HLNotification();
            ViseLog.showInfo("装载消息通知: " + HLNotification.toString());
            HLNotification.context = new WeakReference<>(context);
            HLNotification.message = message;
            return HLNotification;
        }
    }

    public static HLNotification build(FragmentActivity context, int messageResId) {
        synchronized (HLNotification.class) {
            HLNotification HLNotification = new HLNotification();
            HLNotification.context = new WeakReference<>(context);
            HLNotification.message = context.getString(messageResId);
            return HLNotification;
        }
    }

    public static HLNotification show(FragmentActivity context, int messageResId) {
        return show(context, context.getString(messageResId));
    }

    public static HLNotification show(FragmentActivity context, String message, DURATION_TIME durationTime) {
        HLNotification HLNotification = build(context, message);
        HLNotification.durationTime = durationTime;
        HLNotification.showNotification();
        return HLNotification;
    }

    public static HLNotification show(FragmentActivity context, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), durationTime);
    }

    public static HLNotification show(FragmentActivity context, String message) {
        HLNotification HLNotification = build(context, message);
        HLNotification.showNotification();
        return HLNotification;
    }

    public static HLNotification show(FragmentActivity context, int titleResId, int messageResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId));
    }

    public static HLNotification show(FragmentActivity context, String title, String message, DURATION_TIME durationTime) {
        HLNotification HLNotification = build(context, message);
        HLNotification.title = title;
        HLNotification.durationTime = durationTime;
        HLNotification.showNotification();
        return HLNotification;
    }

    public static HLNotification show(FragmentActivity context, String title, String message) {
        HLNotification HLNotification = build(context, message);
        HLNotification.title = title;
        HLNotification.showNotification();
        return HLNotification;
    }

    public static HLNotification show(FragmentActivity context, int titleResId, int messageResId, int iconResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId);
    }

    public static HLNotification show(FragmentActivity context, String title, String message, int iconResId, DURATION_TIME durationTime) {
        HLNotification HLNotification = build(context, message);
        HLNotification.title = title;
        HLNotification.iconResId = iconResId;
        HLNotification.durationTime = durationTime;
        HLNotification.showNotification();
        return HLNotification;
    }

    public static HLNotification show(FragmentActivity context, int titleResId, int messageResId, int iconResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, durationTime);
    }

    public static HLNotification show(FragmentActivity context, String title, String message, int iconResId) {
        HLNotification HLNotification = build(context, message);
        HLNotification.title = title;
        HLNotification.iconResId = iconResId;
        HLNotification.showNotification();
        return HLNotification;
    }

    private boolean isShow;

    public void showNotification() {
        ViseLog.showInfo("启动消息通知 -> " + toString());
        isShow = true;
        showIOSNotification();
    }

    private void showIOSNotification() {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (NotifyToastShadowView) inflater.inflate(R.layout.notification_ios, null);

        boxBody = rootView.findViewById(R.id.box_body);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        boxTitle = rootView.findViewById(R.id.box_title);
        imgIcon = rootView.findViewById(R.id.img_icon);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        boxCustom = rootView.findViewById(R.id.box_custom);

        rootView.setParent(context.get());
        rootView.setOnNotificationClickListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onListen();
                }
            }
        });

        boxBody.post(new Runnable() {
            @Override
            public void run() {
                boxBody.setY(-boxBody.getHeight());
                boxBody.animate().setInterpolator(new DecelerateInterpolator()).translationY(-dip2px(5)).setDuration(500);
            }
        });
        btnNotic.post(new Runnable() {
            @Override
            public void run() {
                rootView.setNotifyHeight(btnNotic.getHeight() + getStatusBarHeight());  //可触控区域高度
            }
        });

        boxBody.setPadding(0, getStatusBarHeight(), 0, 0);

        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }

        if (iconResId == 0) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageResource(iconResId);
            }
        }

        txtMessage.setText(message);
        if (isNull(title)) {
            boxTitle.setVisibility(View.GONE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            boxTitle.setVisibility(View.VISIBLE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }

        boxBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (customView == null) toast.cancel();
                return false;
            }
        });

        new kToast().show(context.get(), rootView);
    }


    private void refreshView() {
        if (txtTitle != null) {
            if (isNull(title)) {
                txtTitle.setVisibility(View.GONE);
            } else {
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText(title);
            }
        }
        if (txtMessage != null) {
            txtMessage.setText(message);
            if (isNull(title)) {
                txtMessage.setGravity(Gravity.CENTER);
                TextPaint tp = txtMessage.getPaint();
                tp.setFakeBoldText(true);
            } else {
                txtMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                TextPaint tp = txtMessage.getPaint();
                tp.setFakeBoldText(false);
            }
        }
        if (imgIcon != null) {
            if (iconResId == 0) {
                imgIcon.setVisibility(View.GONE);
            } else {
                imgIcon.setVisibility(View.VISIBLE);
                if (iconResId != 0) {
                    imgIcon.setImageResource(iconResId);
                }
            }
        }
        if (boxCustom != null) {
            if (customView != null) {
                boxCustom.removeAllViews();
                boxCustom.setVisibility(View.VISIBLE);
                boxCustom.addView(customView);
                rootView.setDispatchTouchEvent(false);
                if (onBindView != null) onBindView.onBind(this, customView);
            } else {
                boxCustom.setVisibility(View.GONE);
                rootView.setDispatchTouchEvent(true);
            }
        }

    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.get().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Method show;

    public class kToast {
        private LinearLayout btn;

        public void show(final Context context, final View view) {
            if (toast != null) toast.cancel();
            toast = null;

            toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            toast.setDuration(durationTime.ordinal());
            toast.setView(view);
            toast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    isShow = false;
                    if (onDismissListener != null) onDismissListener.onListen();
                }
            });

            hookHandler(toast);
            try {
                Object mTN;
                mTN = getField(toast, "mTN");
                if (mTN != null) {
                    Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                    if (tnParamsField != null) {
                        tnParamsField.setAccessible(true);
                        WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);

                        //params.windowAnimations = R.style.toastAnim;
                        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                        }

                        Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
                        tnNextViewField.setAccessible(true);
                        tnNextViewField.set(mTN, toast.getView());
                    }

                    try {
                        //目前是没办法了，新版本Android Toast 的TN要show必须有IBinder，IBinder必须取得TN中mWM实例化对象WindowManagerImpl，这几乎没辙了
                        Object mWM = getField(mTN, "mWM");
                        Field tnField = mWM.getClass().getDeclaredField("mDefaultToken");
                        tnField.setAccessible(true);
                        IBinder token = (IBinder) tnField.get(mWM);

                        if (Build.VERSION.SDK_INT >= 25) {
                            show = mTN.getClass().getDeclaredMethod("show", IBinder.class);
                        } else {
                            show = mTN.getClass().getMethod("show");
                        }

                        show.invoke(mTN, token);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        toast.show();
                    }
                }

                //if (durationTime > DURATION_TIME.ALWAYS) {
                //    handler.postDelayed(hideRunnable, mDuration * 1000);
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(object);
            }
            return null;
        }
    }

    //捕获8.0之前Toast的BadTokenException，Google在Android 8.0的代码提交中修复了这个问题(By @Dovar66[https://github.com/Dovar66/DToast])
    private static void hookHandler(Toast toast) {
        if (toast == null || Build.VERSION.SDK_INT >= 26) return;
        try {
            Field sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            Field sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);

            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }

    protected int dip2px(float dpValue) {
        final float scale = context.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //其他
    public OnSimpleListener getOnNotificationClickListener() {
        return onNotificationClickListener;
    }

    public HLNotification setOnNotificationClickListener(OnSimpleListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
        return this;
    }

    public DURATION_TIME getDurationTime() {
        return durationTime;
    }

    public HLNotification setDurationTime(DURATION_TIME durationTime) {
        this.durationTime = durationTime;
        if (isShow) {
            ViseLog.e("必须使用 build(...) 方法创建时，才可以使用 setDurationTime(...) 来修改通知持续时间。");
        }
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public HLNotification setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public HLNotification setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }

    public HLNotification setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HLNotification setMessage(String message) {
        this.message = message;
        refreshView();
        return this;
    }

    public HLNotification setMessage(int messageResId) {
        this.message = context.get().getString(messageResId);
        refreshView();
        return this;
    }

    public int getIconResId() {
        return iconResId;
    }

    public HLNotification setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refreshView();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public HLNotification setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public HLNotification setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }

    public OnSimpleListener getOnDismissListener() {
        return onDismissListener;
    }

    public HLNotification setOnDismissListener(OnSimpleListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public View getCustomView() {
        return customView;
    }

    public HLNotification setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    private OnBindView onBindView;

    public HLNotification setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }

    public void dismiss() {
        if (toast != null) toast.cancel();
    }

    public interface OnBindView {
        void onBind(HLNotification HLNotification, View v);
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
