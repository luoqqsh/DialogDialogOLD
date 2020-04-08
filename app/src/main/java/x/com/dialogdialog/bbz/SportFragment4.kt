package x.com.dialogdialog.bbz

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentFuliBinding
import x.com.hl_base.fragment.HLBaseVMFragment
import x.com.hl_base.mvvm.NoViewModel
import x.com.hl_log.ViseLog

class SportFragment4 : HLBaseVMFragment<NoViewModel, FragmentFuliBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_fuli
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("444444")
        dataBinding.aaaas.text = "44444444"
    }
    override fun onPause() {
        super.onPause()
        ViseLog.d("4onPause")
    }

    override fun onResume() {
        super.onResume()
        ViseLog.d("4onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        ViseLog.d("4onDestroy")
    }
}