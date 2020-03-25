package x.com.dialogdialog.bbz

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bbz_sport.*
import x.com.dialogdialog.R
import x.com.hl_base.hl_fragment.HLBaseFragment
import x.com.hl_log.ViseLog

class SportFragment3 : HLBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.activity_bbz_sport
    }

    override fun initUI(savedInstanceState: Bundle?) {
        ViseLog.d("3333333")
        aaaas.text="3333333"
    }
}