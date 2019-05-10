package x.com.hl_tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class hl_leak {
    private static Field field;
    private static boolean hasField = true;

    public static void fixLeak(Context context) {
        if (!hasField) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView"};
        for (String param : arr) {
            try {
                if (field == null) {
                    field = imm.getClass().getDeclaredField(param);
                    hasField = false;
                } else {
                    field.setAccessible(true);
                    field.set(imm, null);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void fixInput(Context context) {
        method(context, "mServedView");
        method(context, "mNextServedView");
    }

    private static void method(Context context, String attr) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            Field mCurRootViewField = InputMethodManager.class.getDeclaredField(attr);
            mCurRootViewField.setAccessible(true);
            //取对象
            Object mCurRootView = mCurRootViewField.get(im);
            if (mCurRootView != null) {
                Context newcontext = ((View) mCurRootView).getContext();
                if (newcontext == context) {
                    //破坏GC链
                    mCurRootViewField.set(im, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
