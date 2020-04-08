package x.com.dialogdialog.bbz

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentSportBinding
import x.com.hl_base.fragment.HLBaseVMFragment
import x.com.hl_base.mvvm.NoViewModel
import x.com.hl_log.ViseLog

class SportFragment : HLBaseVMFragment<NoViewModel, FragmentSportBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_sport
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("1111111")

//        val viewModelRxLife: HLBaseViewModel_RxLife = ViewModelProvider(this).get(HLBaseViewModel_RxLife::class.java)

    }
    override fun onPause() {
        super.onPause()
        ViseLog.d("1onPause")
    }

    override fun onResume() {
        super.onResume()
        ViseLog.d("1onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        ViseLog.d("1onDestroy")
    }
}