package x.com.hl_base.hl_fragment;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import x.com.hl_dialog.R;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/29 16:43
 */
public class MessageDialog extends BaseDialog {

    protected int buttonOrientation;


    protected Drawable okButtonDrawable;
    protected Drawable cancelButtonDrawable;
    protected Drawable otherButtonDrawable;

    protected String title = "提示";
    protected String message = "提示信息";
    protected String okButton = "确定";
    protected String cancelButton;
    protected String otherButton;


    protected RelativeLayout boxRoot;
    protected RelativeLayout bkg;
    protected TextView txtDialogTitle;
    protected TextView txtDialogTip;
    protected RelativeLayout boxCustom;
    protected EditText txtInput;
    protected ImageView splitHorizontal;
    protected LinearLayout boxButton;
    protected TextView btnSelectNegative;
    protected ImageView splitVertical1;
    protected TextView btnSelectOther;
    protected ImageView splitVertical2;
    protected TextView btnSelectPositive;

    protected MessageDialog() {
    }

    public static MessageDialog build(@NonNull FragmentActivity context) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.context = new WeakReference<>(context);
            messageDialog.showDialog(context);
//            switch (messageDialog.style) {
//                case STYLE_IOS:
//                    messageDialog.build(messageDialog, R.layout.dialog_select_ios);
//                    break;
//                case STYLE_KONGZUE:
//                    messageDialog.build(messageDialog, R.layout.dialog_select);
//                    break;
//                case STYLE_MATERIAL:
//                    messageDialog.build(messageDialog);
//                    break;
//            }
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, String title, String message) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context, title, message, null, null, null);
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, int titleResId, int messageResId) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    null, null, null
            );
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, String title, String message, String okButton) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context, title, message, okButton, null, null);
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, int titleResId, int messageResId, int okButtonResId) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId)
                    , null, null
            );
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, String title, String message, String okButton, String cancelButton) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context, title, message, okButton, cancelButton, null);
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    null
            );
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, String title, String message, String okButton, String cancelButton, String otherButton) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = build(context);
            messageDialog.title = title;
            if (okButton != null) messageDialog.okButton = okButton;
            messageDialog.message = message;
            messageDialog.cancelButton = cancelButton;
            messageDialog.otherButton = otherButton;

            messageDialog.showDialog(context);
            return messageDialog;
        }
    }

    public static MessageDialog show(@NonNull FragmentActivity context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId, int otherButtonResId) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    context.getString(otherButtonResId)
            );
            return messageDialog;
        }
    }

    protected View rootView;

    @Override
    public void initUI(View rootView) {
//        log("启动对话框 -> " + toString());
//        if (boxCustom != null) boxCustom.removeAllViews();
//        if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
//            materialAlertDialog = (AlertDialog) dialog.get().getDialog();
//        } else {
//            if (rootView != null) {
//                this.rootView = rootView;
//                bkg = rootView.findViewById(R.id.bkg);
//                boxRoot = rootView.findViewById(R.id.box_root);
//                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
//                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
//                boxCustom = rootView.findViewById(R.id.box_custom);
//                txtInput = rootView.findViewById(R.id.txt_input);
//                splitHorizontal = rootView.findViewById(R.id.split_horizontal);
//                boxButton = rootView.findViewById(R.id.box_button);
//                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
//                splitVertical1 = rootView.findViewById(R.id.split_vertical1);
//                btnSelectOther = rootView.findViewById(R.id.btn_selectOther);
//                splitVertical2 = rootView.findViewById(R.id.split_vertical2);
//                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
//                boxInput = rootView.findViewById(R.id.box_input);
//            }
//        }
//
//        refreshView();
//        if (onShowListener != null) onShowListener.onShow(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ensure2;
    }

    private ViewTreeObserver.OnGlobalLayoutListener blurViewRefreshLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
//            if (isShow) {
//                if (bkg != null && blurView != null) {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
//                    blurView.setLayoutParams(params);
//                    blurView.requestLayout();
//                }
//            } else {
//                if (bkg != null) {
//                    bkg.getViewTreeObserver().removeOnGlobalLayoutListener(blurViewRefreshLayoutListener);
//                }
//            }
        }
    };


    protected void showDialog1() {
//        if (style == DialogSettings.STYLE.STYLE_IOS) {
//            super.showDialog();
//        } else if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
//            if (theme == DialogSettings.THEME.LIGHT) {
//                super.showDialog(R.style.LightDialogWithShadow);
//            } else {
//                super.showDialog(R.style.DarkDialogWithShadow);
//            }
//        } else {
//            super.showDialog(R.style.LightDialogWithShadow);
//        }
    }

    public String getTitle() {
        return title;
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MessageDialog setMessage(String content) {
        this.message = content;
        return this;
    }

    public MessageDialog setMessage(int contentResId) {
        this.message = context.get().getString(contentResId);
        return this;
    }

    public String getOkButton() {
        return okButton;
    }

    public MessageDialog setOkButton(String okButton) {
        this.okButton = okButton;
//        refreshView();
        return this;
    }

    public MessageDialog setOkButton(int okButtonResId) {
        setOkButton(context.get().getString(okButtonResId));
        return this;
    }

//    public MessageDialog setOkButton(String okButton, OnDialogButtonClickListener onOkButtonClickListener) {
//        this.okButton = okButton;
//        this.onOkButtonClickListener = onOkButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOkButton(int okButtonResId, OnDialogButtonClickListener onOkButtonClickListener) {
//        setOkButton(context.get().getString(okButtonResId), onOkButtonClickListener);
//        return this;
//    }
//
//    public MessageDialog setOkButton(OnDialogButtonClickListener onOkButtonClickListener) {
//        this.onOkButtonClickListener = onOkButtonClickListener;
//        refreshView();
//        return this;
//    }

    public String getCancelButton() {
        return cancelButton;
    }

//    public MessageDialog setCancelButton(String cancelButton) {
//        this.cancelButton = cancelButton;
//        refreshView();
//        return this;
//    }

//    public MessageDialog setCancelButton(int cancelButtonResId) {
//        setCancelButton(context.get().getString(cancelButtonResId));
//        return this;
//    }
//
//    public MessageDialog setCancelButton(String cancelButton, OnDialogButtonClickListener onCancelButtonClickListener) {
//        this.cancelButton = cancelButton;
//        this.onCancelButtonClickListener = onCancelButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setCancelButton(int cancelButtonResId, OnDialogButtonClickListener onCancelButtonClickListener) {
//        setCancelButton(context.get().getString(cancelButtonResId), onCancelButtonClickListener);
//        return this;
//    }
//
//    public MessageDialog setCancelButton(OnDialogButtonClickListener onCancelButtonClickListener) {
//        this.onCancelButtonClickListener = onCancelButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public String getOtherButton() {
//        return otherButton;
//    }
//
//    public MessageDialog setOtherButton(String otherButton) {
//        this.otherButton = otherButton;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOtherButton(int otherButtonResId) {
//        setOtherButton(context.get().getString(otherButtonResId));
//        return this;
//    }
//
//    public MessageDialog setOtherButton(String otherButton, OnDialogButtonClickListener onOtherButtonClickListener) {
//        this.otherButton = otherButton;
//        this.onOtherButtonClickListener = onOtherButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOtherButton(int otherButtonResId, OnDialogButtonClickListener onOtherButtonClickListener) {
//        setOtherButton(context.get().getString(otherButtonResId), onOtherButtonClickListener);
//        return this;
//    }
//
//    public MessageDialog setOtherButton(OnDialogButtonClickListener onOtherButtonClickListener) {
//        this.onOtherButtonClickListener = onOtherButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public OnDialogButtonClickListener getOnOkButtonClickListener() {
//        return onOkButtonClickListener;
//    }
//
//    public MessageDialog setOnOkButtonClickListener(OnDialogButtonClickListener onOkButtonClickListener) {
//        this.onOkButtonClickListener = onOkButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
//        return onCancelButtonClickListener;
//    }
//
//    public MessageDialog setOnCancelButtonClickListener(OnDialogButtonClickListener onCancelButtonClickListener) {
//        this.onCancelButtonClickListener = onCancelButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
//        return onOtherButtonClickListener;
//    }
//
//    public MessageDialog setOnOtherButtonClickListener(OnDialogButtonClickListener onOtherButtonClickListener) {
//        this.onOtherButtonClickListener = onOtherButtonClickListener;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOkButtonDrawable(@DrawableRes int okButtonDrawableResId) {
//        this.okButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOkButtonDrawable(Drawable okButtonDrawable) {
//        this.okButtonDrawable = okButtonDrawable;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setCancelButtonDrawable(@DrawableRes int okButtonDrawableResId) {
//        this.cancelButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setCancelButtonDrawable(Drawable cancelButtonDrawable) {
//        this.cancelButtonDrawable = cancelButtonDrawable;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOtherButtonDrawable(@DrawableRes int okButtonDrawableResId) {
//        this.otherButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setOtherButtonDrawable(Drawable otherButtonDrawable) {
//        this.otherButtonDrawable = otherButtonDrawable;
//        refreshView();
//        return this;
//    }
//
//    public int getButtonOrientation() {
//        return buttonOrientation;
//    }
//
//    public MessageDialog setButtonOrientation(int buttonOrientation) {
//        this.buttonOrientation = buttonOrientation;
//        refreshView();
//        return this;
//    }
//
//    //其他
//    public OnDismissListener getOnDismissListener() {
//        return onDismissListener == null ? new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        } : onDismissListener;
//    }
//
//    public MessageDialog setOnDismissListener(OnDismissListener onDismissListener) {
//        this.onDismissListener = onDismissListener;
//        return this;
//    }
//
//    public OnShowListener getOnShowListener() {
//        return onShowListener == null ? new OnShowListener() {
//            @Override
//            public void onShow(BaseDialog dialog) {
//
//            }
//        } : onShowListener;
//    }
//
//    public MessageDialog setOnShowListener(OnShowListener onShowListener) {
//        this.onShowListener = onShowListener;
//        return this;
//    }
//
//    public DialogSettings.STYLE getStyle() {
//        return style;
//    }
//
//    public MessageDialog setStyle(DialogSettings.STYLE style) {
//        if (isAlreadyShown) {
//            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
//            return this;
//        }
//
//        this.style = style;
//        switch (this.style) {
//            case STYLE_IOS:
//                build(this, R.layout.dialog_select_ios);
//                break;
//            case STYLE_KONGZUE:
//                build(this, R.layout.dialog_select);
//                break;
//            case STYLE_MATERIAL:
//                build(this);
//                break;
//        }
//
//        return this;
//    }
//
//    public DialogSettings.THEME getTheme() {
//        return theme;
//    }
//
//    public MessageDialog setTheme(DialogSettings.THEME theme) {
//
//        if (isAlreadyShown) {
//            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
//            return this;
//        }
//
//        this.theme = theme;
//        refreshView();
//        return this;
//    }
//
//    public boolean getCancelable() {
//        return cancelable == BOOLEAN.TRUE;
//    }
//
//    public MessageDialog setCancelable(boolean enable) {
//        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
//        if (dialog != null) dialog.get().setCancelable(cancelable == BOOLEAN.TRUE);
//        return this;
//    }
//
//
//    public TextInfo getTitleTextInfo() {
//        return titleTextInfo;
//    }
//
//    public MessageDialog setTitleTextInfo(TextInfo titleTextInfo) {
//        this.titleTextInfo = titleTextInfo;
//        refreshView();
//        return this;
//    }
//
//    public TextInfo getMessageTextInfo() {
//        return messageTextInfo;
//    }
//
//    public MessageDialog setMessageTextInfo(TextInfo messageTextInfo) {
//        this.messageTextInfo = messageTextInfo;
//        refreshView();
//        return this;
//    }
//
//    public TextInfo getButtonTextInfo() {
//        return buttonTextInfo;
//    }
//
//    public MessageDialog setButtonTextInfo(TextInfo buttonTextInfo) {
//        this.buttonTextInfo = buttonTextInfo;
//        refreshView();
//        return this;
//    }
//
//    public TextInfo getButtonPositiveTextInfo() {
//        return buttonPositiveTextInfo;
//    }
//
//    public MessageDialog setButtonPositiveTextInfo(TextInfo buttonPositiveTextInfo) {
//        this.buttonPositiveTextInfo = buttonPositiveTextInfo;
//        refreshView();
//        return this;
//    }
//
//    public int getBackgroundColor() {
//        return backgroundColor;
//    }
//
//    public MessageDialog setBackgroundColor(int backgroundColor) {
//        this.backgroundColor = backgroundColor;
//        refreshView();
//        return this;
//    }
//
//    public View getCustomView() {
//        return customView;
//    }
//
//    public MessageDialog setCustomView(View customView) {
//        this.customView = customView;
//        refreshView();
//        return this;
//    }
//
//    private OnBindView onBindView;
//
//    public MessageDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
//        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
//        this.onBindView = onBindView;
//        refreshView();
//        return this;
//    }
//
//    public interface OnBindView {
//        void onBind(MessageDialog dialog, View v);
//    }
//
//    public int getBackgroundResId() {
//        return backgroundResId;
//    }
//
//    public MessageDialog setBackgroundResId(int backgroundResId) {
//        this.backgroundResId = backgroundResId;
//        refreshView();
//        return this;
//    }
//
//    public MessageDialog setCustomDialogStyleId(int customDialogStyleId) {
//        if (isAlreadyShown) {
//            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
//            return this;
//        }
//        this.customDialogStyleId = customDialogStyleId;
//        return this;
//    }
//
//    public String toString() {
//        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
//    }
//
//    public OnBackClickListener getOnBackClickListener() {
//        return onBackClickListener;
//    }
//
//    public MessageDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
//        this.onBackClickListener = onBackClickListener;
//        return this;
//    }
}
