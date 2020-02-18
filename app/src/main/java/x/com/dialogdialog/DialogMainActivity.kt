package x.com.dialogdialog

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import x.com.dialogdialog.BaseApp.BaseActivity
import x.com.dialogdialog.Viewpager2.ViewActivity
import x.com.hl_log.ViseLog
import x.com.hl_tools.DialogTest.myObjectPool
import x.com.hl_tools.animation.ToolAnimation
import x.com.hl_tools.hl_permissions

class DialogMainActivity : BaseActivity() {
    override fun initUI(savedInstanceState: Bundle?) {
        init()
        hl_permissions.applyWriteStoragePermission(mActivity, null)
        hl_permissions.applyLocationPermission(mActivity, null)
        hl_permissions.applyCameraPermission(mActivity, null)
//        ViseLog.d(testUtil.stringFromJNI())
        test()
    }

    private fun test() {

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

        runBlocking {
            launch {
                delay(5000L)
                ViseLog.d("adsf")
            }
        
            ViseLog.d("协程结束")


        }

//        runBlocking(Dispatchers.IO) {
//            // 注意这里
//            for (i in 0..10) {
//                ViseLog.showLog("$i ${Thread.currentThread().name}")
//                delay(100)
//            }
//        }
//
//        ViseLog.d("bbbbbbbbb ${Thread.currentThread().name}")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private fun init() {
        java.setOnClickListener { startActivity(Intent(mActivity, DialogMainJavaActivity::class.java)) }
        tongzhi.setOnClickListener { startActivity(Intent(mActivity, NotificationActivity::class.java)) }
        media.setOnClickListener { startActivity(Intent(mActivity, HHMedia3Activity::class.java)) }
        x5web.setOnClickListener { startActivity(Intent(mActivity, H5Activity::class.java)) }
        log.setOnClickListener { startActivity(Intent(mActivity, LogActivity::class.java)) }
        open.setOnClickListener { startActivity(Intent(mActivity, GLActivity::class.java)) }
        mapping.setOnClickListener { startActivity(Intent(mActivity, MappingActivity::class.java)) }
        view.setOnClickListener { startActivity(Intent(mActivity, ViewActivity::class.java)) }
        camera.setOnClickListener { startActivity(Intent(mActivity, HHCameraActivity::class.java)) }
        mediacodec.setOnClickListener { startActivity(Intent(mActivity, MediaCodecActivity::class.java)) }
        map.setOnClickListener { startActivity(Intent(mActivity, HHMapActivity::class.java)) }
        close.setOnClickListener {}

        dialog.setOnClickListener {
            //val b = DFNDialog.newInstance()
            //b.setMessage("dddddddddd", 4)
            //b.showFDialog(mActivity)

            //val df = DFListFileDialog.newInstance(hl_file.QQFILE)
            //df.setOnSelectFileListener { file -> ViseLog.d(file.absolutePath) }
            //df.showFDialog(mActivity)
            startActivity(Intent(mActivity, DialogActivity::class.java))
            ToolAnimation.addTouchDrak(dialog, true)
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
}
