package x.com.dialogdialog.xyvideo

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentXyVideoBinding
import x.com.hl_base.fragment.HLBaseVMFragment
import x.com.hl_base.mvvm.NoViewModel

class HomeVideoListFragment : HLBaseVMFragment<NoViewModel, FragmentXyVideoBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_xy_video
    }

    override fun initUI(savedInstanceState: Bundle?) {

    }
}