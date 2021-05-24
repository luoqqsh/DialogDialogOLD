package x.com.dialogdialog;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import x.com.base.activity.HLBaseActivity;
import x.com.hl_study.handler.SHandler;
import x.com.hl_study.handler.SLooper;
import x.com.hl_study.handler.SMessage;

class HandlerTest {
    //模拟ActivityThread里的启动应用-线程创建
    public static void main(String[] args) {
        //创建全局唯一主线程Looper对象
        SLooper.prepare();

//        //谷歌handler
//        Handler handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                //...
//            }
//        };
//        handler.sendEmptyMessage(1);//发送空消息
//        handler.sendEmptyMessageDelayed(what, 1000);//延迟发送，单位ms
//        handler.sendMessageAtTime(msg, SystemClock.uptimeMillis() + 1000);//指定时间发送
//        handler.removeMessages(3);//移除指定消息

//        handler.postDelayed(runnable, 1000);//每秒执行1次runnable方法
//        handler.removeCallbacks(runnable);//取消上述方法

        //创建Handler对象
        SHandler sHandler = new SHandler() {
            @Override
            public void handleMessage(SMessage msg) {
                super.handleMessage(msg);
                //主线程，你想做的事情
                //msg.what 消息标志
                //msg.obj 消息内容
                System.out.println(msg.obj.toString());
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                SMessage message = new SMessage();
                message.what = 1;
                message.obj = "fjwe";
                sHandler.sendMessage(message);
                //1.先把消息放入消息队列
                //区分
//                Message message2 = new Message();
//                message2.what = 2;
//                message2.obj = "fjwe2";
//                handler.sendMessage(message2);
            }
        }).start();
        //获取消息
        SLooper.loop();
    }

    //静态内部类，避免内存泄露
    public static class testHandler extends Handler {
        WeakReference<HLBaseActivity> mActivity;//这里使用弱引用

        testHandler(HLBaseActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }
}
