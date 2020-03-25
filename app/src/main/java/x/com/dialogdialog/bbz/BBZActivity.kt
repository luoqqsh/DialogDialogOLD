package x.com.dialogdialog.bbz

import android.os.Bundle
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.ActivityBbzBinding
import x.com.hl_base.hl_activity.HLBaseActivity
import x.com.hl_log.ViseLog
import java.util.*
import kotlin.collections.set

class BBZActivity : HLBaseActivity() {
    lateinit var binding: ActivityBbzBinding
    private val fragments = HashMap<String, Fragment>()
    private val currentFragment: Fragment = SportFragment()

    private val a = arrayOf("sport","fuli","renwu","mine")
    override fun initUI(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bbz)
        initTab()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bbz
    }

    fun initTab() {
        val bottomNavigationBar: RadioGroup = binding.bottomNavigation
        fragments[a[0]] = SportFragment()
        fragments[a[1]] = SportFragment()
        fragments[a[2]] = SportFragment()
        fragments[a[3]] = SportFragment()
        switchContent(currentFragment, SportFragment())
        bottomNavigationBar.setOnCheckedChangeListener { group, i ->
            run {
                ViseLog.d(i)
                switchContent(currentFragment, fragments[a[i]]!!)
            }
        }
    }

    /**
     * fragment替换（保留之前的状态）
     * 注：如果此时希望调用显示的fragment对象的onResume方法，则执行to.onResume();即可。
     * 但注意的是：从fragmentActivity跳转到下一个Activity，再按返回键时，FrameLayout容器中所有的fragment都将会调用onResume方法；
     */
    private fun switchContent(from: Fragment, to: Fragment) {
        if (from !== to) {
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