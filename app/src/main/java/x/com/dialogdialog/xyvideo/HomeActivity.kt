package x.com.dialogdialog.xyvideo

import android.os.Bundle
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.ActivityXyvideoBinding
import x.com.hl_base.activity.HLBaseVMActivity
import x.com.hl_base.mvvm.NoViewModel
import java.util.HashMap

class HomeActivity : HLBaseVMActivity<NoViewModel, ActivityXyvideoBinding>() {
    private val fragments = HashMap<String, Fragment>()
    private var currentFragment: Fragment = HomeVideoListFragment()

    private val a = arrayOf("video", "smallvideo")
    override fun getLayoutId(): Int {
        return R.layout.activity_xyvideo
    }

    override fun initView(savedInstanceState: Bundle?) {
        val bottomNavigationBar: RadioGroup = dataBinding.bottomNavigation
        fragments[a[0]] = HomeVideoListFragment()
        fragments[a[1]] = HomeSmallVideoListFragment()
        switchContent(currentFragment, HomeVideoListFragment())
        bottomNavigationBar.setOnCheckedChangeListener { group, i ->
            run {
                var j = 0;
                when (i) {
                    R.id.video -> j = 0
                    R.id.svideo -> j = 1
                }
                switchContent(currentFragment, fragments[a[j]]!!)
            }
        }
    }

    private fun switchContent(from: Fragment, to: Fragment) {
        if (from !== to) {
            currentFragment = to;
            val transaction = supportFragmentManager.beginTransaction()
            // 先判断是否被add过
            if (!to.isAdded) {
                // 隐藏当前的fragment，add下一个fragment到Activity中
                transaction.hide(from).add(R.id.content_frame, to).commit()
            } else {
                // 隐藏当前的fragment，显示下一个fragment
                transaction.hide(from).show(to).commit()
                //该命令可注释，若希望fragment切换的过程中，被显示的fragment执行onResume方法，则解注；
                // to.onResume();
            }
        }
    }
}