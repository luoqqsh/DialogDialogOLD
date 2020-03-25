package x.com.dialogdialog

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import x.com.dialogdialog.BaseApp.BaseActivity
import x.com.dialogdialog.Viewpager2.ViewActivity
import x.com.dialogdialog.bbz.BBZActivity
import x.com.hl_livedatabus.LiveEventBus
import x.com.hl_log.ViseLog
import x.com.hl_tools.DialogTest.myObjectPool
import x.com.hl_tools.hl_permissions

class DialogMainActivity : BaseActivity() {
    override fun initUI(savedInstanceState: Bundle?) {
        init()
        hl_permissions.applyWriteStoragePermission(mActivity, null)
        hl_permissions.applyLocationPermission(mActivity, null)
        hl_permissions.applyCameraPermission(mActivity, null)
        hl_permissions.applyLocationPermission(this, object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>?) {
                ViseLog.d(failPermission)
            }
        })
//        ViseLog.d(testUtil.stringFromJNI())
        liveEventBus()
    }

    private fun xiecheng() {
        //一般情况下不适用
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(10000L)
            println("World!")
            ViseLog.d("good")
        }

        //在所有子协程完成其任务并不阻塞当前线程
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000L)
            ViseLog.d("aaa")
            withContext(Dispatchers.Main) {
                ViseLog.d("goodhappy")
            }
        }

        //会阻塞主线程，相当于同步做法，执行完成才会继续后面的代码
//        runBlocking(Dispatchers.IO) {
//            launch {
//                delay(5000L)
//                ViseLog.d("adsf")
//            }
//
//        }
//        ViseLog.d("协程结束")
    }

    private fun liveEventBus() {
        //要接收的数据，接收普通消息处理
        LiveEventBus
                .get("KEY_TEST_OBSERVE", Int::class.java)
                .observe(this, Observer { s -> Toast.makeText(mActivity, mActivity.localClassName + s.toString(), Toast.LENGTH_SHORT).show() })

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private fun init() {
        mainJava.setOnClickListener { startActivity(Intent(mActivity, DialogMainJavaActivity::class.java)) }
        mainNotice.setOnClickListener { startActivity(Intent(mActivity, NotificationActivity::class.java)) }
        mainPlayer.setOnClickListener { startActivity(Intent(mActivity, HHMedia3Activity::class.java)) }
        mainWeb.setOnClickListener { startActivity(Intent(mActivity, H5Activity::class.java)) }
        mainLog.setOnClickListener { startActivity(Intent(mActivity, LogActivity::class.java)) }
        mainOpenGL.setOnClickListener { startActivity(Intent(mActivity, GLActivity::class.java)) }
        mainConfusion.setOnClickListener { startActivity(Intent(mActivity, MappingActivity::class.java)) }
        mainView.setOnClickListener { startActivity(Intent(mActivity, ViewActivity::class.java)) }
        mainCamera.setOnClickListener { startActivity(Intent(mActivity, HHCameraActivity::class.java)) }
        mainMediaCodec.setOnClickListener { startActivity(Intent(mActivity, MediaCodecActivity::class.java)) }
        mainMap.setOnClickListener { startActivity(Intent(mActivity, HHMapActivity::class.java)) }
        close.setOnClickListener { startActivity(Intent(mActivity, ListPlayerActivity::class.java)) }
        mainLocal.setOnClickListener {

        }
        mainBBZ.setOnClickListener {
            startActivity(Intent(mActivity, BBZActivity::class.java))
        }
        mainDialog.setOnClickListener {
            startActivity(Intent(mActivity, DialogActivity::class.java))
//            ToolAnimation.addTouchDrak(dialog, true)
            //发送不同消息，注意数据类型
//            LiveEventBus.get("KEY_TEST_OBSERVE").post(321)
//            LiveEventBus.get("KEY_TEST_OBSERVE_FOREVER")
//                    .post("Message To ForeverObserver: " + Random().nextInt(100))
        }
    }

    private fun objectPool() {
        val pool = myObjectPool(2, 4)
        val o1 = pool.acquire()
        val o2 = pool.acquire()
        val o3 = pool.acquire()
        val o4 = pool.acquire()

        ViseLog.d(o1!!.hashCode())
        ViseLog.d(o2!!.hashCode())
        ViseLog.d(o3!!.hashCode())
        ViseLog.d(o4!!.hashCode())
    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(10000L)
//            LiveEventBus.get("KEY_TEST_OBSERVE").post(23423423)
        }
    }
}
