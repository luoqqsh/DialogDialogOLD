package x.com.dialogdialog

import android.Manifest
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import x.com.dialogdialog.Base.BaseActivity
import x.com.hl_dialog.DFNDialog
import x.com.hl_log.ViseLog
import x.com.hl_tools.DialogTools.myObjectPool
import x.com.hl_tools.hl_orientation
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
        media.setOnClickListener { startActivity(Intent(mContext, HHMediaViewActivity::class.java)) }
        x5web.setOnClickListener { startActivity(Intent(mContext, H5Activity::class.java)) }
        log.setOnClickListener { startActivity(Intent(mContext, LogActivity::class.java)) }
        open.setOnClickListener { startActivity(Intent(mContext, GLActivity::class.java)) }
        mapping.setOnClickListener { startActivity(Intent(mContext, MappingActivity::class.java)) }
        close.setOnClickListener {
            val orientation = hl_orientation()
            orientation.orientationEnable(this, null)
        }

        dialog.setOnClickListener {
            val b = DFNDialog.newInstance()
            b.setMessage("dddddddddd", 4)
            if (!b.isAdded) {
                b.show(supportFragmentManager, "normalDialog")
            }
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
                ViseLog.d("true2222")
            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>) {
                ViseLog.d(failPermission)
            }
        })
        hl_permissions.applyPermission(mContext, arrayOf(Manifest.permission.READ_PHONE_STATE), object : hl_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {

            }

            override fun requestPermissionFail(failPermission: Map<String, Boolean>) {

            }
        })
    }
}
