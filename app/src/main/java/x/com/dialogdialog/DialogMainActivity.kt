package x.com.dialogdialog

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import x.com.dialogdialog.BaseApp.BaseActivity
import x.com.dialogdialog.Viewpager2.ViewActivity
import x.com.dialogdialog.bbz.BBZActivity
import x.com.hl_base.service.StepService
import x.com.hl_battery.BatteryInfo
import x.com.hl_livedatabus.LiveEventBus
import x.com.hl_log.ViseLog
import x.com.hl_rx.download.DownloadListener
import x.com.hl_rx.download.FileDownloadManager
import x.com.hl_rx.download.FileDownloadService
import x.com.hl_tools.DialogTest.myObjectPool
import x.com.hl_tools.hl_file
import x.com.hl_tools.hl_permissions
import x.com.hl_tools.hl_sharedP2
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DialogMainActivity : BaseActivity() {
    private var instance: FileDownloadManager? = null

    override fun initUI(savedInstanceState: Bundle?) {
        init()
        hl_permissions.applyWriteStoragePermission(mActivity, null)
        hl_permissions.applyCameraPermission(mActivity, null)
        hl_permissions.applyLocationPermission(this, object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>?) {
                ViseLog.d(failPermission)
            }
        })
//        ViseLog.d(testUtil.stringFromJNI())
        StepService.startStepService(mActivity)

        hl_sharedP2.setParam(mActivity, "asd", "dd是啥")
        val param1 = hl_sharedP2.remove(mActivity, "asd")
        ViseLog.d(param1)
        val param = hl_sharedP2.getParam(mActivity, "asd", "42")
        ViseLog.d(param)

        LiveEventBus.get("batteryInfo", BatteryInfo::class.java)
                .observe(mActivity, Observer { info -> mainInfo.text = info.toString() })
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
//            Glide.with(this).load(Picture(hl_file.SDROOT + File.separator + "jiamiimg.jpg")).into(mainImg)
//            Glide.with(this).load(Picture("https://ss.bscstorage.com/miguan/qutianqi/%E5%8A%A0%E5%AF%86%E6%96%87%E4%BB%B6.jpg")).into(mainImg)

//            KTaskManagerB(OnSimpleListener {
//                val imageBitmap = GlideUtil.instance().getImageBitmap(mActivity, R.mipmap.jiamiimg, 100, 100)
//                ViseLog.d(imageBitmap)
//            })

            val downloadUrl = "http://192.168.102.77/APK/%E8%A5%BF%E6%9F%9A%E8%A7%86%E9%A2%91/2.4.0/xysp_yingyongbao(10).apk"

            FileDownloadService.bindService(mActivity.applicationContext, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    (service as FileDownloadService.DownloadBinder).start(downloadUrl, hl_file.DOWNLOADS, "xx.apk", object : DownloadListener {
                        override fun onDownLoadError(error: String?) {
                            ViseLog.d(error)
                        }

                        override fun onDownLoadStart(startProgress: Int) {
                        }

                        override fun onDownloadFinish(filePath: String?) {
                            ViseLog.d(filePath)
                        }

                        override fun onDownLoadProgress(progress: Int) {
                            ViseLog.showInfo(progress)
                        }

                    })
                }

                override fun onServiceDisconnected(name: ComponentName) {}
            })

//            instance = FileDownloadManager.getInstance(mActivity)
//            instance!!.setDownloadListener(object : DownloadListener {
//                override fun onDownLoadStart(progress: Int) {}
//                override fun onDownLoadCancel() {
//                    ViseLog.d("cancel")
//                }
//
//                override fun onDownLoadProgress(progress: Int) {
////                    ViseLog.showLog(progress)
//                }
//
//                override fun onDownLoadError(error: String) {
//                    ViseLog.e(error)
//                }
//
//                override fun onDownloadFinish(filePath: String) {
//                    ViseLog.d(filePath)
//                }
//            })
//            instance!!.startDownload(downloadUrl, hl_file.DOWNLOADS + File.separator + hl_url.getFileNameFromUrl(downloadUrl))
        }
        mainBBZ.setOnClickListener {
            startActivity(Intent(mActivity, BBZActivity::class.java))
            instance!!.cancelDownload()
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

    @Throws(Exception::class)
    fun getFileStream(url: String?): ByteArray? {
        try {
            val httpUrl = URL(url)
            val conn: HttpURLConnection = httpUrl.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5 * 1000
            val inStream: InputStream = conn.inputStream //通过输入流获取图片数据
            return readInputStream(inStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    fun readInputStream(inStream: InputStream): ByteArray? {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        inStream.close()
        return outStream.toByteArray()
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
