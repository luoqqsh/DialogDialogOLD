package x.com.dialogdialog.Viewpager2

import kotlinx.android.synthetic.main.fragment_viewpager2.*
import x.com.dialogdialog.R
import x.com.hl_base.LazyLoadFragment
import java.util.*

class ViewFragment : LazyLoadFragment() {
    private lateinit var list: ArrayList<String>
    override fun setContentView(): Int {
        return R.layout.fragment_viewpager2
    }

    override fun lazyLoad() {
        list = ArrayList()
        list.add("0")
        list.add("1")
        list.add("2")
        list.add("3")
        list.add("4")
        rrrrr.text = "doremi"
//        view_pager.adapter = object : FragmentStateAdapter(this) {
//            override fun createFragment(position: Int): Fragment {
//                return
//            }
//
//            override fun getItemCount(): Int {
//                return list.size
//            }
//        }
    }
}