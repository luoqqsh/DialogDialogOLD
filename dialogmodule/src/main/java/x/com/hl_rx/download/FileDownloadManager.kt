package x.com.hl_rx.download

import android.Manifest
import android.content.Context
import kotlinx.coroutines.*
import okhttp3.*
import x.com.hl_log.ViseLog
import x.com.hl_tools.hl_file
import x.com.hl_tools.hl_permissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class FileDownloadManager(context: Context) {
    private var downloadListener: DownloadListener? = null
    private val downloadUrls: MutableList<String>
    private val resultFilePaths: MutableList<String>
    private var okHttpClient: OkHttpClient? = null
    private val context: Context = context.applicationContext
    private var call: Call? = null
    private var loadedLength: Long = 0//已下载的文件大小
    private var isCancel = false
    fun setDownloadListener(downloadListener: DownloadListener?) {
        this.downloadListener = downloadListener
    }

    @Synchronized
    fun startDownload(url: String, folderName: String, fileName: String) {
        startDownload(url, "$folderName/$fileName")
    }

    @Synchronized
    fun startDownload(url: String, resultFilePath: String) {
        ViseLog.showInfo("""$url$resultFilePath""".trimIndent())
        if (checkRepeat(url, resultFilePath)) {
            downloadUrls.add(url)
            resultFilePaths.add(resultFilePath)
        }
        hl_file.createFileFolder(resultFilePaths[0])
        download(downloadUrls[0], resultFilePaths[0])
    }

    @Synchronized
    fun cancelDownload() {
        isCancel = true
        call!!.cancel()
    }

    @Synchronized
    private fun checkRepeat(url: String, resultFilePath: String): Boolean {
        for (i in downloadUrls.indices) {
            if (downloadUrls[i] == url && resultFilePaths[i] == resultFilePath) {
                return false
            }
        }
        return true
    }

    @Synchronized
    fun download(url: String?, resultFilePath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (hl_permissions.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (okHttpClient == null) {
                    okHttpClient = OkHttpClient()
                }
                var downloadFileLength: Long = 0
                if (hl_file.isFileExist(resultFilePath)) {
                    //如果文件存在，需要先获取下载文件大小，已确定是否启用断点续传
                    val getSizeRequest = Request.Builder().url(url!!).build()
                    try {
                        val execute = okHttpClient!!.newCall(getSizeRequest).execute()
                        val body = execute.body
                        if (body != null) {
                            downloadFileLength = body.contentLength()
                            body.close()
                            //如果大小一致，说明已经下载好了。可以直接返回结果
                            if (File(resultFilePath).length() == downloadFileLength) {
                                withContext(Dispatchers.Main) {
                                    if (downloadListener != null) downloadListener!!.onDownloadFinish(resultFilePath)
                                }
                                return@launch
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    call = if (downloadFileLength == 0L) {
                        val request = Request.Builder().url(url).build()
                        okHttpClient!!.newCall(request)
                    } else {
                        val request = Request.Builder().addHeader("RANGE", "bytes=$loadedLength-$downloadFileLength").url(url).build()
                        okHttpClient!!.newCall(request)
                    }
                } else {
                    val request = Request.Builder().url(url!!).build()
                    call = okHttpClient!!.newCall(request)
                }

                val response = call!!.execute()
                isCancel = false
                saveFile(response, resultFilePath)

//                call!!.enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        ViseLog.e(e.message)
//                        if (downloadListener != null) {
//                            downloadListener!!.onDownLoadError(e.message)
//                        }
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        isCancel = false
//                        saveFile(response, resultFilePath)
//                    }
//                })
            } else {
                ViseLog.e("没有文件读写权限")
                if (downloadListener != null) {
                    downloadListener!!.onDownLoadError("没有文件读写权限")
                }
            }
        }
    }

    private suspend fun saveFile(response: Response, resultFilePath: String) {
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        var body: ResponseBody? = null
        val file = File(resultFilePath)
        loadedLength = file.length()
        try {
            val buf = ByteArray(2048)
            var len = 0
            body = response.body
            if (body == null) {
                if (downloadListener != null) downloadListener!!.onDownLoadError("没有找到文件数据")
                return
            }
            `is` = body.byteStream()
            val total = body.contentLength().toInt()
            if (loadedLength == total.toLong()) {
                loadedLength = 0
            }
            withContext(Dispatchers.Main) {
                if (downloadListener != null) downloadListener!!.onDownLoadStart(total.toInt())
            }
            fos = FileOutputStream(file, true)
            var progress = 0
            //还在请求阶段触发的取消，网络卡的时候容易出现
            if (isCancel) {
                withContext(Dispatchers.Main) {
                    if (downloadListener != null) downloadListener!!.onDownLoadCancel()
                }
                return
            }
            while (!isCancel && `is`.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                loadedLength += len.toLong()
                val progressNew = (loadedLength * 1.0f / total * 100).toInt()
                ViseLog.showInfo("$loadedLength $total $progressNew")
                //避免多次传值和出现回退情况
                if (progressNew > progress) {
                    progress = progressNew
                    // 下载中更新进度条
                    withContext(Dispatchers.Main) {
                        if (downloadListener != null) downloadListener!!.onDownLoadProgress(progress)
                    }
                }
            }
            fos.flush()
            //触发取消会停止掉while循环，而继续往下走
            if (isCancel) {
                withContext(Dispatchers.Main) {
                    if (downloadListener != null) downloadListener!!.onDownLoadCancel()
                }
                return
            }
            // 下载完成
            withContext(Dispatchers.Main) {
                if (downloadListener != null) downloadListener!!.onDownloadFinish(resultFilePath)
            }

        } catch (e: Exception) {
            if (isCancel) {
                withContext(Dispatchers.Main) {
                    if (downloadListener != null) downloadListener!!.onDownLoadCancel()
                }
                return
            }
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                if (downloadListener != null) downloadListener!!.onDownLoadError(e.message)
            }

        } finally {
            try {
                body?.close()
                `is`?.close()
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                if (downloadListener != null) downloadListener!!.onDownLoadError(e.message)
            }
        }
    }

    companion object {
        private var fileDownloadManager: FileDownloadManager? = null
        fun getInstance(context: Context): FileDownloadManager? {
            if (fileDownloadManager == null) {
                fileDownloadManager = FileDownloadManager(context)
            }
            return fileDownloadManager
        }
    }

    init {
        downloadUrls = ArrayList()
        resultFilePaths = ArrayList()
    }
}