package x.com.dialogdialog.bbz

import android.os.Bundle
import x.com.dialogdialog.R
import x.com.dialogdialog.databinding.FragmentSportBinding
import x.com.hl_base.hl_fragment.HLBaseMVVMFragment
import x.com.hl_log.ViseLog

class SportFragment : HLBaseMVVMFragment<FragmentSportBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_sport
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("1111111")
        dataBinding.aaaas.text = "111111131"

//        val viewModelRxLife: HLBaseViewModel_RxLife = ViewModelProvider(this).get(HLBaseViewModel_RxLife::class.java)

    }
}