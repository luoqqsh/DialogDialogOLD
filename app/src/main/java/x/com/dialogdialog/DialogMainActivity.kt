package x.com.dialogdialog

import android.Manifest
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import x.com.dialogdialog.Base.BaseActivity
import x.com.dialogdialog.Viewpager2.ViewActivity
import x.com.hl_dialog.DFListFileDialog
import x.com.hl_log.ViseLog
import x.com.hl_tools.DialogTools.myObjectPool
import x.com.hl_tools.hl_file
import x.com.hl_tools.hl_permissions

class DialogMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setBackgroundDrawableResource(R.color.blue1)
        init()
        permissions()
        test()
    }

    private fun test() {
    }

    private fun init() {
        java.setOnClickListener { startActivity(Intent(mContext, DialogMainJavaActivity::class.java)) }
        tongzhi.setOnClickListener { startActivity(Intent(mContext, NotificationActivity::class.java)) }
        media.setOnClickListener { startActivity(Intent(mContext, HHMedia3Activity::class.java)) }
        x5web.setOnClickListener { startActivity(Intent(mContext, H5Activity::class.java)) }
        log.setOnClickListener { startActivity(Intent(mContext, LogActivity::class.java)) }
        open.setOnClickListener { startActivity(Intent(mContext, GLActivity::class.java)) }
        mapping.setOnClickListener { startActivity(Intent(mContext, MappingActivity::class.java)) }
        view.setOnClickListener { startActivity(Intent(mContext, ViewActivity::class.java)) }
        camera.setOnClickListener { startActivity(Intent(mContext, HHCameraActivity::class.java)) }
        mediacodec.setOnClickListener { startActivity(Intent(mContext, ViewActivity::class.java)) }
        vchart.setOnClickListener { startActivity(Intent(mContext, vchartActivity::class.java)) }
        map.setOnClickListener { startActivity(Intent(mContext, HHMapActivity::class.java)) }
        close.setOnClickListener {
            //            val orientation = hl_orientation()
//            orientation.orientationEnable(this, null)
            animation_view4.playAnimation()

        }

        dialog.setOnClickListener {
            //            val b = DFNDialog.newInstance()
//            b.setMessage("dddddddddd", 4)
//            b.showFDialog(mContext)

            val df = DFListFileDialog.newInstance(hl_file.QQFILE)
            df.setOnSelectFileListener { file -> ViseLog.d(file.absolutePath) }
            df.showFDialog(mContext)
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

    private fun permissions() {
        //检查权限
        //需要在activity重写onRequestPermissionsResult回调方法。不处理回调不写
        hl_permissions.applyWriteStoragePermission(mContext, object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>) {
                ViseLog.d(failPermission)
            }
        })
        hl_permissions.applyLocationPermission(mContext, object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>) {
                ViseLog.d(failPermission)
            }
        })
        hl_permissions.applyPermission(mContext, arrayOf(Manifest.permission.CAMERA), object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {

            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>) {

            }
        })
    }
}
