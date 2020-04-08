package x.com.dialogdialog.bbz

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentFuliBinding
import x.com.hl_base.fragment.HLBaseVMFragment
import x.com.hl_base.mvvm.NoViewModel
import x.com.hl_log.ViseLog

class SportFragment2 : HLBaseVMFragment<NoViewModel, FragmentFuliBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_fuli
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("2222222")
        dataBinding.aaaas.text = "2222222"
    }

    override fun onPause() {
        super.onPause()
        ViseLog.d("2onPause")
    }

    override fun onResume() {
        super.onResume()
        ViseLog.d("2onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        ViseLog.d("2onDestroy")
    }
}