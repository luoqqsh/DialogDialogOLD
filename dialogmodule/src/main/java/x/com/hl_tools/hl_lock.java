package x.com.hl_tools;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

public class hl_lock {
    private static hl_lock lockutil;
    private KeyguardManager mKeyguardManager;
    private Context context;

    public hl_lock(Context context) {
        this.context = context;
    }

    public static hl_lock instance(Context context) {
        if (lockutil == null) {
            lockutil = new hl_lock(context);
        }
        return lockutil;
    }

    private KeyguardManager getKeyguardManager(Context context) {
        if (mKeyguardManager == null)
            mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager;
    }

    /**
     * 判断屏幕是否在锁屏状态
     *
     * @return true:锁上的； false:已解锁
     */
    public boolean isScreenLocked() {
        if (getKeyguardManager(context) == null) return false;
        return getKeyguardManager(context).isKeyguardLocked();
//        return getKeyguardManager().inKeyguardRestrictedInputMode();//过时，api 29开始弃用、失效
//        return getKeyguardManager().isDeviceLocked();//这个只是判断是否密码锁住了，如果没有设置密码，一些判断就不太对了
    }

    /**
     * 判断屏幕是否有密码
     *
     * @return true:有密码； false:无密码
     */
    public boolean isScreenHasLockSecret() {
        return getKeyguardManager(context).isKeyguardSecure();
    }


}
