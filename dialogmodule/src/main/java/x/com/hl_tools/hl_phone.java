package x.com.hl_tools;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class hl_phone {
    //安卓4.1以上不可用
    public static void open(Context context) {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
//                    telephony.endCall();//挂断电话
            telephony.answerRingingCall();//接听电话
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            try {
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
                context.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");
                intent = new Intent("android.intent.action.MEDIA_BUTTON");
                keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
                context.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");
                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
                context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
                context.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
            } catch (Exception e2) {
                e2.printStackTrace();
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
                context.sendOrderedBroadcast(meidaButtonIntent, null);
            }
        }
    }

    public static void open2(Context context) {
        // 放开耳机按钮 ，使用有序广播
        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
        upIntent.putExtra("android.intent.extra.KEY_EVENT", keyEvent2);

        context.sendOrderedBroadcast(upIntent, "android.permission.CALL_PRIVILEGED");

        // 插耳机
        Intent pushIntent = new Intent(Intent.ACTION_HEADSET_PLUG);

        pushIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pushIntent.putExtra("state", 1);
        pushIntent.putExtra("microphone", 1);
        pushIntent.putExtra("name", "Headset");

        context.sendOrderedBroadcast(pushIntent, "android.permission.CALL_PRIVILEGED");

        // 按下耳机按钮

        Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);

        KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
        localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);

        context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");

        // 放开耳机按钮

        upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

        keyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
        upIntent.putExtra("android.intent.extra.KEY_EVENT", keyEvent2);

        context.sendOrderedBroadcast(upIntent, "android.permission.CALL_PRIVILEGED");

        // 拔出耳机 ，不然手机就一直保持插入耳机状态

        Intent pullIntent = new Intent(Intent.ACTION_HEADSET_PLUG);

        pullIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pullIntent.putExtra("state", 0);
        pullIntent.putExtra("microphone", 1);
        pullIntent.putExtra("name", "Headset");

        context.sendOrderedBroadcast(pullIntent, "android.permission.CALL_PRIVILEGED");
    }

    public static void close() {
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
}
