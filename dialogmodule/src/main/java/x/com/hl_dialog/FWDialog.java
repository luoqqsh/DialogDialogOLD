package x.com.hl_dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vise.log.ViseLog;

public class FWDialog {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public FWDialog(WindowManager wm, View view, int windowtype) {
       
       
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        if (wm == null) return;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.alpha = 0.3f;
        wmParams.flags =
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getRealSize(p);
        wmParams.width = p.x;
        wmParams.height = p.y;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        wm.addView(view, wmParams);

    }

    public void hideFWDialog(WindowManager wm,View view){
        wm.removeViewImmediate(view);
    }
   
        
}
    

