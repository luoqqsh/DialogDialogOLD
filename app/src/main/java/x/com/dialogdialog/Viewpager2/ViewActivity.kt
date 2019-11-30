package x.com.dialogdialog.Viewpager2

import android.os.Bundle
import x.com.dialogdialog.BaseApp.BaseActivity
import x.com.dialogdialog.R

class ViewActivity : BaseActivity() {
    override fun initUI(savedInstanceState: Bundle?) {
        val fragment = ViewFragment()
        supportFragmentManager.beginTransaction().add(R.id.ll_view, fragment).commit()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_viewactivity
    }
}