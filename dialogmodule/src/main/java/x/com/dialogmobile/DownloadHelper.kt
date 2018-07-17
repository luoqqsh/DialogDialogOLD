package x.com.dialogmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import x.com.dialogmobile.NDialogBuilder.onDialogbtnClickListener.Companion.BUTTON_CONFIRM
import x.com.dialogmobile.ProgressDialog.PDialog2Builder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class DownloadHelper @SuppressLint("HandlerLeak")
constructor(private val mActivity: Activity, private val mDownloadUrl: String, private var mDownloadCallBack: DownloadCallBack?) {
    private val TAG = "download"
    private val mVersionName: String?
    private var mSavePath: String? = null
    private var mProgress: Int = 0
    private var mIsCancle: Boolean? = false
    private var mProgressHandler: Handler? = null
    private var mIsForce: Int = 0
    private var mPDialog2Builder: PDialog2Builder? = null
    private var mDialog: Dialog? = null
    private var mNotificationHelper: NotificationHelper? = null
    private var mNotificationIconId: Int = 0
    private var mNotificationTitle: String? = null
    private var mIsShowNotification: Boolean = false//是否显示下载进度通知
    private var mIsShowDialog: Boolean = false//是否显示下载进度弹框
    private var mIsCheckUp: Boolean = false//是否是更新，true则自动安装
    private var mBtnSureText: String? = null
    private var mBtnCancelText: String? = null
    private var thread: Thread? = null

    interface DownloadCallBack {
        //void downloadCancel();//取消下载时回调

        fun installCancel() //取消安装时回调

        fun downloadSuccess(file: File) //下载成功时回调

        fun downloadFail() //下载失败时回调
    }

    init {
        mIsForce = 0 //默认不强制
        mIsCheckUp = false
        mVersionName = getFileName(mDownloadUrl)
        mSavePath = Environment.getExternalStorageDirectory().toString() + "/" + "deanDownload"

        mProgressHandler = object : MyHandle(mActivity) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    DOWNLOAD_ING -> {
                        if (mIsShowDialog && mProgress < 100) {
                            mPDialog2Builder!!.setProgress(mProgress)
                        }
                        if (mIsShowNotification && mProgress < 100) {
                            mNotificationHelper!!.setProgress(mProgress, null)
                        }
                    }
                    DOWNLOAD_OVER -> {
                        //刷新进度
                        downloadSuccess()
                        //回调file
                        mDownloadCallBack!!.downloadSuccess(File(mSavePath, mVersionName!!))
                        if (mIsCheckUp) {
                            //判断是否执行安装
                            installAPK()
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据url返回文件名
     *
     * @param downloadUrl 下载路径
     * @return this
     */
    private fun getFileName(downloadUrl: String?): String? {
        var downloadUrl = downloadUrl
        val symbol = arrayOf("/", "//?", "=", "&")
        var split: Array<String>
        if (downloadUrl != null) {
            var i = 0
            while (i < symbol.size && downloadUrl!!.length > 0) {
                split = downloadUrl.split(symbol[i].toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                downloadUrl = split[split.size - 1]
                i++
            }
            return downloadUrl
        } else {
            return ""
        }
    }

    /**
     * 开启下载
     */
    private fun startDownload() {
        mDownloadManager.add(this)
        if (mIsShowNotification && mNotificationHelper == null) {
            mNotificationHelper = NotificationHelper(mActivity)
            if (mNotificationIconId != 0) {
                mNotificationHelper!!.setSmallIcon(mNotificationIconId)
            }
            mNotificationHelper!!.setTitle(mNotificationTitle!!)
            mNotificationHelper!!.setType(NotificationHelper.NOTIFICATION_TYPE_DOWNLOAD)
            mNotificationHelper!!.setNotificationGroup("下载")
            mNotificationHelper!!.setProgress(0, null)
        }
        //显示下载对话框
        if (mIsShowDialog) {
            showDialog()
        }
        //获取网络资源，判断下载或直接安装
        getApkResource()
    }

    /*
     * 显示正在下载对话框
     */
    private fun showDialog() {
        if (mDialog == null || mPDialog2Builder == null) {
            mPDialog2Builder = PDialog2Builder(mActivity, 0, 1f)
                    .setMessage("正在下载：")
                    .setTouchOutSideCancelable(false)
                    .setBtnCancel("取消下载", View.OnClickListener {
                        mIsCancle = true
                        mDialog!!.dismiss()
                        //mDownloadCallBack.downloadCancel();
                    })
                    .setBtnVisity(false, false)
            mDialog = mPDialog2Builder!!.create()
        }
        mDialog!!.show()
    }

    /*
     * 开启新线程
     * 获取网络apk资源文件，判断下载或直接安装
     */
    private fun getApkResource() {
        //显示通知栏下载进度
        mProgress = 0
        thread = Thread(Runnable {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                var mMiddlerogress = 0
                val file = File(mSavePath!!)
                val apkFile = File(mSavePath, mVersionName!!)
                var fos: FileOutputStream? = null
                try {
                    if (!file.exists()) {
                        file.mkdir()
                    }
                    val url = URL(mDownloadUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connect()
                    val `is` = connection.inputStream
                    val len = connection.contentLength//获取网络apk大小
                    if (apkFile.exists()) {
                        apkFile.delete()
                    }
                    fos = FileOutputStream(apkFile)
                    var count = 0
                    val buffer = ByteArray(1024)
                    while (!mIsCancle!!) {
                        val numread = `is`.read(buffer)
                        count += numread
                        // 计算进度条的当前位置
                        mMiddlerogress = (count.toFloat() / len * 100).toInt()
                        if (mMiddlerogress > mProgress && mMiddlerogress < 100) {
                            // 更新进度条
                            mProgress = mMiddlerogress
                            mProgressHandler!!.sendEmptyMessage(DOWNLOAD_ING)
                        }
                        // 下载完成
                        if (numread < 0) {
                            mProgress = 100
                            mProgressHandler!!.sendEmptyMessage(DOWNLOAD_OVER)
                            break
                        }
                        fos.write(buffer, 0, numread)
                    }
                } catch (e: Exception) {
                    if (apkFile.exists()) {
                        apkFile.delete()
                    }
                    if (mDialog != null) {
                        mDialog!!.dismiss()
                    }
                    if (mDownloadCallBack != null) {
                        mDownloadCallBack!!.downloadFail()
                    }
                } finally {
                    if (fos != null) {
                        try {
                            fos.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            } else {
                Log.e(TAG, "getApk: SD卡读写权限受限")
            }
        })
        thread!!.start()
    }

    /*
     * 下载完成
     */
    private fun downloadSuccess() {
        if (mNotificationHelper != null) {
            mNotificationHelper!!.setProgress(100, if (mIsCheckUp) getInstallApkIntent(File(mSavePath, mVersionName!!)) else null)
        }
        if (mPDialog2Builder != null) {
            mPDialog2Builder!!.setProgress(100)
            mPDialog2Builder!!.setBtnCancel(mBtnCancelText, View.OnClickListener {
                if (mDialog != null) mDialog!!.dismiss()
                if (mDownloadCallBack != null) mDownloadCallBack!!.installCancel()
            })
            mPDialog2Builder!!.setBtnSure(mBtnSureText, View.OnClickListener { installAPK() })
            mPDialog2Builder!!.setBtnVisity(mIsForce == 0, true)
        }
    }

    /**
     * 安装apk
     */
    private fun installAPK() {
        try {
            val apkFile = File(mSavePath, mVersionName!!)
            if (!apkFile.exists()) {
                return
            }
            val installApkIntent = getInstallApkIntent(apkFile)
            if (mNotificationHelper != null) mNotificationHelper!!.setProgress(100, installApkIntent)
            mActivity.startActivity(installApkIntent)
        } catch (e: Exception) {
            Toast.makeText(mActivity, "安装失败", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * 获取安装apk的Intent
     *
     * @param apkFile 安装包
     * @return Intent 跳转意图
     */
    private fun getInstallApkIntent(apkFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
            val apkUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile)
            //由于没有在Activity环境下启动Activity,设置下面的标签
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return intent
    }

    internal open class MyHandle(activity: Activity) : Handler() {
        val activity: WeakReference<Activity>

        init {
            this.activity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    /**
     * 设置是否强制更新
     *
     * @param isForce 是否强制
     * @return this
     */
    fun setIsForce(isForce: Boolean): DownloadHelper {
        mIsForce = if (isForce) 1 else 0
        return this
    }

    /**
     * 设置通知参数
     *
     * @return this
     */
    fun setNotificationShow(isShow: Boolean, title: String, iconId: Int): DownloadHelper {
        mIsShowNotification = isShow
        mNotificationTitle = title
        mNotificationIconId = iconId
        return this
    }

    /**
     * 设置是否显示下载进度弹框
     *
     * @param isShow 是否显示
     * @return this
     */
    fun setDialogShow(isShow: Boolean, btnCancelText: String, btnSureText: String): DownloadHelper {
        mIsShowDialog = isShow
        mBtnSureText = btnSureText
        mBtnCancelText = btnCancelText
        return this
    }

    /**
     * 是否是版本更新，true自动执行安装
     *
     * @param isCheckUp 是否为版本升级
     * @return this
     */
    fun setCheckUp(isCheckUp: Boolean): DownloadHelper {
        mIsCheckUp = isCheckUp
        return this
    }

    /**
     * 设置保存路径，默认 Environment.getExternalStorageDirectory() + "/" + "deanDownload";
     *
     * @param path 路径
     * @return this
     */
    fun setSavePath(path: String): DownloadHelper {
        mSavePath = path
        return this
    }

    /**
     * 开启下载
     */
    fun start() {
        //判断文件是否存在
        val file: File = File(mSavePath, mVersionName)
        if (file.exists()) {
            //已存在
            showAlreadyExistsDialog(file)
        } else {
            //不存在，执行下载
            startDownload()
        }
    }

    /**
     * 中断下载
     */
    fun stop() {
        mIsCancle = true
        if (mNotificationHelper != null) {
            mNotificationHelper!!.cancel()
        }
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
        thread = null
        mProgressHandler = null
        mDownloadCallBack = null
        mDownloadManager.remove(this)
    }

    /**
     * 删除文件
     */
    fun deleteFile() {
        val file = File(mSavePath, mVersionName!!)
        if (file.exists()) {
            file.delete()
        }
    }

    companion object {
        private val DOWNLOAD_ING = 1
        private val DOWNLOAD_OVER = 2

        private val mDownloadManager = ArrayList<DownloadHelper>()

        /**
         * 删除制定路径下的文件
         *
         * @param filePath 文件路径
         */
        fun deleteFile(filePath: String) {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
        }

        /**
         * 清空所有下载
         */
        fun cancelAll() {
            if (mDownloadManager != null) {
                val list = ArrayList(mDownloadManager)
                for (helper in list) {
                    helper?.stop()
                }
                mDownloadManager.clear()
                list.clear()
            }
        }
    }

    fun showAlreadyExistsDialog(file: File) {
        mActivity.runOnUiThread {
            NDialogBuilder(mActivity, 0, 1.0f)
                    .setTitle("文件已存在")
                    .setTouchOutSideCancelable(false)
                    .setMessage("文件已存在，是否继续？", NDialogBuilder.MSG_LAYOUT_LEFT)
                    .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
                    .setBtnClickListener(true, "重新下载", "继续", object : NDialogBuilder.onDialogbtnClickListener {
                        override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int) {
                            if (whichBtn == BUTTON_CONFIRM) {
                                startDownload()
                            } else {
                                if (mIsCheckUp) {
                                    //判断是否执行安装
                                    installAPK()
                                }
                                mDownloadCallBack!!.downloadSuccess(file)
                            }
                        }
                    })
                    .create().show()
        }
    }
}
