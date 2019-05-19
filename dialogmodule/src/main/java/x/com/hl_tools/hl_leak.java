package x.com.hl_tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class hl_leak {

    //华为手机的坑
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        Field f;
        Object obj_get;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    setFieldAccessible(f);
                }
                obj_get = f.get(imm);
                if (obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    //不判断的话，可能导致有输入框的页面无法弹出软键盘的问题
//                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
//                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    private static void setFieldAccessible(final Field field)
    {
        AccessController.doPrivileged(new PrivilegedAction() {
            @Override
            public Object run() {
                field.setAccessible(true);
                return null;
            }
        });
    }
    public static void fixInput(Context context) {
        method(context, "mServedView");
        method(context, "mNextServedView");
        if (hl_device.isHuaWeiPhone()) {
            fixInputMethodManagerLeak(context);
        }
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
