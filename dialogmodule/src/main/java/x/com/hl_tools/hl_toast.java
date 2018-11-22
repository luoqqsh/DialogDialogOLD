package x.com.hl_tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import x.com.hl_dialog.R;

/**
 * Toast
 */

public class hl_toast extends Toast {
    public hl_toast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, int resId, CharSequence text, int duration) {
        Toast result = new Toast(context);

        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.toast_message, null);

        //实例化ImageView和TextView对象
        ImageView imageView = layout.findViewById(R.id.dialog_title);
        TextView textView = layout.findViewById(R.id.toast_message);

        imageView.setImageResource(resId);
        textView.setText(text);

        result.setView(layout);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }
}
