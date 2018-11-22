package x.com.hl_tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.TelecomManager;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.TELECOM_SERVICE;

public class hl_phone {
    //挂断电话
    public static void closeRing() {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();//挂断电话
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //安卓8.0以上版本接电话，需要ANSWER_PHONE_CALL权限
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void openring(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
        Method method;
        try {
            method = Class.forName("android.telecom.TelecomManager").getMethod("acceptRingingCall");
            method.invoke(telecomManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
