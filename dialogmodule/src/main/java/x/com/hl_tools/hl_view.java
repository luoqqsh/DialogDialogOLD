package x.com.hl_tools;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

public class hl_view {
    private static final int DELAY = 1000;
    private static long lastClickTime = 0;

    //防止手抖
    public static boolean isNotFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > DELAY) {
            lastClickTime = currentTime;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 测量view的宽高
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 获得精确的宽度、高度
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.width,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.height,
                View.MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算ListView的高度，父控件为LinearLayout
     *
     * @param lv
     */
    public static void measureLinearLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 计算ListView的高度，父控件为FrameLayout
     *
     * @param lv
     */
    public static void measureFrameLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }
    /**
     * 隐藏输入框
     *
     * @param view view
     */
    public static void hideSoftInput(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示输入框
     *
     * @param view view
     */
    public static void showSoftInput(final View view) {
        showSoftInput(view, 200);
    }

    /**
     * 显示输入框
     *
     * @param view  view
     * @param delay 延时 毫秒
     */
    public static void showSoftInput(final View view, int delay) {
        view.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, delay);
    }

    /**
     * 移动光标到文本框最后
     *
     * @param editText EditText
     */
    public static void moveSelectionToEnd(EditText editText) {
        Editable etext = editText.getText();
        Selection.setSelection(etext, etext.length());
    }
}
