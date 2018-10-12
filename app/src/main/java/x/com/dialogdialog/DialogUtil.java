package x.com.dialogdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gps-xing on 2016/11/2.
 * 对话框
 */

public class DialogUtil {
    //Toast//////////////////////////////////////////////////////////////////////////////////////
    public static void showToast(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastLong(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        }
    }

    public static Toast showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
        return toast;
//        showMyToast(Toast.makeText(this, "message", Toast.LENGTH_LONG), 1000);
    }


    //加载框//////////////////////////////////////////////////////////////////////////////////////
    /**
     * 加载框
     */
    private static ProgressDialog loadDialog;

    /**
     * 创建加载框
     */
    public static void createLoadDialog(Context context) {
        loadDialog = new ProgressDialog(context);// 创建加载框
        loadDialog.setMessage("正在加载中，请稍候...");// 设置显示的消息
        loadDialog.setCancelable(true);// 设置是否可以取消
    }

    /**
     * 显示加载框
     */
    public static void showLoadDialog() {
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    public static void showLoadDialog(String message) {
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.setMessage(message);// 设置显示的消息
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    /**
     * 显示加载框
     */
    public static void showLoadDialog(boolean iscancel) {
        loadDialog.setCancelable(iscancel);
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    /**
     * 加载框消失
     */
    public static void dismissLoadDialog() {
        if (loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }


}
