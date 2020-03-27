package x.com.dialogdialog.Viewpager2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.fragment_viewpager2.*
import x.com.dialogdialog.R
import x.com.hl_base.fragment.LazyLoadFragment
import java.util.*

class ViewFragment : LazyLoadFragment() {
    private lateinit var list: ArrayList<String>
    override fun setContentView(): Int {
        return R.layout.fragment_viewpager2
    }

    override fun lazyLoad() {
        list = ArrayList()
        list.add("01122330000000000000000")
        list.add("12233440000000000000000")
        list.add("23344550000000000000000")
        list.add("34455660000000000000000")
        list.add("45566770000000000000000")
        list.add("55566770000000000000000")
        list.add("65566770000000000000000")
        list.add("75566770000000000000000")
        list.add("85566770000000000000000")
        rrrrr.text = "doremi"
        view_pager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return ItemVideoFragment(list[position],position)
            }

            override fun getItemCount(): Int {
                return list.size
            }
        }
    }
}