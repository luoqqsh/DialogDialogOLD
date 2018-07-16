package x.com.dialogdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import x.com.dialogmobile.LoginDialog.LDialogBuilder

/**
 * 手机号+验证码登录弹窗
 */
class LoginActivity : AppCompatActivity() {
    var a: LDialogBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        a = LDialogBuilder(this).inputphone().setonInputPhoneListener(object : LDialogBuilder.onInputPhone {
            override fun inputPhonenext(phone: String?, type: Int) {
                //获取验证码，接口调用
                if (type == 0) {
                    a!!.dismissdialog1()
                    a!!.inputcode(phone!!).setonInputCodeListener(object : LDialogBuilder.onInputCode {
                        override fun inputCodenext(phone: String?, code: String) {
                            //校验验证码，接口调用
                        }
                    })
                }
            }
        })



//        a!!.dismissdialog2()


    }
}
