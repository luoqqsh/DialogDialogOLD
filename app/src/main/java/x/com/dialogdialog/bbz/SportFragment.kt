package x.com.dialogdialog.bbz

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bbz_sport.*
import x.com.dialogdialog.R
import x.com.hl_base.hl_fragment.HLBaseFragment
import x.com.hl_log.ViseLog

class SportFragment : HLBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.activity_bbz_sport
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("1111111")
        aaaas.text="11111111"
    }
}