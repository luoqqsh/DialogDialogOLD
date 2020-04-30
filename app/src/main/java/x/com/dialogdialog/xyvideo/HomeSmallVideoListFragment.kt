package x.com.dialogdialog.xyvideo

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentXySmallvideoBinding
import x.com.hl_base.fragment.HLBaseVMFragment
import x.com.hl_base.mvvm.NoViewModel

class HomeSmallVideoListFragment : HLBaseVMFragment<NoViewModel, FragmentXySmallvideoBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_xy_smallvideo
    }

    override fun initUI(savedInstanceState: Bundle?) {

    }
}