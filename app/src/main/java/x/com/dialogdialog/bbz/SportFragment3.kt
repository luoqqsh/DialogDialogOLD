package x.com.dialogdialog.bbz

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentFuliBinding
import x.com.hl_base.hl_fragment.HLBaseFragment
import x.com.hl_base.hl_fragment.HLBaseMVVMFragment
import x.com.hl_log.ViseLog

class SportFragment3 : HLBaseMVVMFragment<FragmentFuliBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_fuli
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("3333333")
        dataBinding.aaaas.text = "3333333"
    }
}