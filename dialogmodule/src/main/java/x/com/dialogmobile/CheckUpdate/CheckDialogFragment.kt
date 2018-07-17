package x.com.dialogmobile.CheckUpdate

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import x.com.dialogmobile.DownloadHelper
import x.com.dialogmobile.NDialogBuilder
import x.com.dialogmobile.PermissionHelper
import x.com.dialogmobile.R
import java.io.File

class CheckDialogFragment(private val activity: Activity, private val msg: String, private val layoutStyle: Int, private val downloadUrl: String,
                          private val isforce: Int, private val downloadFailMessgae: String,
                          private val callback: OnCheckcallback) : DialogFragment() {
    private var downloadHelper: DownloadHelper? = null

    private val mPermissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val mPermissionList_O = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)


    interface OnCheckcallback {
        fun onCancel()
        fun onInstallCancel()

        fun onDownloadFinish(apkFile: File)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = NDialogBuilder(context!!, layoutStyle, 1.0f)
                .setTouchOutSideCancelable(false)
                .setMessage(msg, NDialogBuilder.MSG_LAYOUT_LEFT)
                .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
                .setTitle("发现新版本")
                .setBtnClickListener(true, if (isforce == 1) "退出程序" else "稍后更新", "立即更新",
                        object : NDialogBuilder.onDialogbtnClickListener {
                            override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int) {
                                when (whichBtn) {
                                    1//稍后、退出
                                    -> if (isforce == 1) {
                                        System.exit(0)
                                    } else {
                                        callback.onCancel()
                                        dismiss()
                                    }
                                    2//立即更新
                                    -> PermissionHelper.instance.applyPermission(
                                            this@CheckDialogFragment,
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) mPermissionList_O else mPermissionList,
                                            object : PermissionHelper.RequestPermissionCallBack {
                                                override fun requestPermissionSuccess() {
                                                    dismiss()
                                                    downloadHelper = DownloadHelper(activity, downloadUrl,
                                                            object : DownloadHelper.DownloadCallBack {

                                                                override fun installCancel() {
                                                                    //取消安装
                                                                    callback.onInstallCancel()
                                                                }

                                                                override fun downloadSuccess(file: File) {
                                                                    //下载完成，setCheckUp(true)时自动执行安装
                                                                    callback.onDownloadFinish(file)
                                                                }


                                                                override fun downloadFail() {
                                                                    activity.runOnUiThread {
                                                                        NDialogBuilder(activity, 0, 1.0f)
                                                                                .setTitle("下载失败")
                                                                                .setTouchOutSideCancelable(false)
                                                                                .setMessage(downloadFailMessgae, NDialogBuilder.MSG_LAYOUT_LEFT)
                                                                                .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
                                                                                .setBtnClickListener(true, "重新下载", object : NDialogBuilder.onDialogbtnClickListener {
                                                                                    override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int) {
                                                                                        downloadHelper!!.start()
                                                                                    }
                                                                                })
                                                                                .create().show()
                                                                    }
                                                                }
                                                            })
                                                            .setCheckUp(true)
                                                            .setIsForce(isforce == 1)
                                                            .setNotificationShow(true, "正在下载...", R.mipmap.ic_launcher)
                                                            .setDialogShow(true, "下次再说", "立即安装")
                                                    downloadHelper!!.start()
                                                }

                                                override fun requestPermissionFail() {
                                                    dismiss()
                                                    Toast.makeText(activity, "权限申请失败", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                }
                            }
                        })
                .create()
        //屏蔽返回键
        dialog.setOnKeyListener { dialogInterface, i, keyEvent -> keyEvent.keyCode == KeyEvent.KEYCODE_BACK }
        return dialog
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.instance.requestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        //super.onDismiss(dialog);
    }
}
