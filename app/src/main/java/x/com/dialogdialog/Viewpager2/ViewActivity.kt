package x.com.dialogdialog.Viewpager2

import android.os.Bundle
import x.com.dialogdialog.Base.BaseActivity
import x.com.dialogdialog.R

class ViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewactivity)

        val fragment = ViewFragment()
        supportFragmentManager.beginTransaction().add(R.id.ll_view, fragment).commit()
    }
}