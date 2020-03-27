package x.com.dialogdialog.Viewpager2

import kotlinx.android.synthetic.main.item_video_fragment.*
import x.com.dialogdialog.R
import x.com.hl_base.fragment.LazyLoadFragment
import x.com.hl_log.ViseLog

class ItemVideoFragment(private val list: String,val position: Int) : LazyLoadFragment() {
    override fun setContentView(): Int {
        return R.layout.item_video_fragment
    }

    override fun lazyLoad() {
        ffaa.text = list
        ViseLog.d(position)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        ViseLog.d("$hidden $position")
    }
}