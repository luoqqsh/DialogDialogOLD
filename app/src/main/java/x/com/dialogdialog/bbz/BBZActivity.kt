package x.com.dialogdialog.bbz

import android.os.Bundle
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.ActivityBbzBinding
import x.com.hl_base.activity.HLBaseVMActivity
import x.com.hl_base.mvvm.NoViewModel
import x.com.hl_log.ViseLog
import x.com.hl_rx.jiu.OnHttpCallback
import x.com.hl_rx.kotlin.BBZCommonService
import x.com.hl_rx.kotlin.RDClient
import x.com.hl_rx.kotlin.await
import x.com.hl_rx.test.retrofitKt
import java.util.*
import kotlin.collections.set

class BBZActivity : HLBaseVMActivity<NoViewModel, ActivityBbzBinding>() {
    private val fragments = HashMap<String, Fragment>()
    private var currentFragment: Fragment = SportFragment()

    private val a = arrayOf("sport", "fuli", "renwu", "mine")
    override fun getLayoutId(): Int {
        return R.layout.activity_bbz
    }

    override fun initView(savedInstanceState: Bundle?) {
        initBottomTab()
//        var httpManager = RDClient.getInstance()
//        var service = RDClient.getService(BBZCommonService::class.java)
//        httpManager.enqueue(service.getHomeData("bb1862708c28fe26", "42740", "0"), object : OnHttpCallback<String> {
//            override fun onSuccess(o: String?) {
//                ViseLog.d(o)
//            }
//
//            override fun onFail(error: String?) {
//                ViseLog.d(error)
//            }
//        })
        CoroutineScope(Dispatchers.Main).launch {
//            withContext(Dispatchers.Main) {
//            retrofitKt.testRetrofit()
            val data = RDClient.getService(BBZCommonService::class.java).getHomeData("bb1862708c28fe26", "42740", "0").await().data
            ViseLog.d(data)
//            }
        }
    }

    private fun initBottomTab() {
        val bottomNavigationBar: RadioGroup = dataBinding.bottomNavigation
        fragments[a[0]] = SportFragment()
        fragments[a[1]] = SportFragment2()
        fragments[a[2]] = SportFragment3()
        fragments[a[3]] = SportFragment4()
        switchContent(currentFragment, SportFragment())
        bottomNavigationBar.setOnCheckedChangeListener { group, i ->
            run {
                var j = 0;
                when (i) {
                    R.id.sport -> j = 0
                    R.id.fuli -> j = 1
                    R.id.renwu -> j = 2
                    R.id.mine -> j = 3
                }
                switchContent(currentFragment, fragments[a[j]]!!)
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