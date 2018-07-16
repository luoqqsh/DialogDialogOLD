package x.com.dialogmobile.LoginDialog

import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.widget.Toast

import x.com.dialogmobile.EDialogBuilder
import x.com.dialogmobile.NDialogBuilder

class LDialogBuilder(private val context: Context) {
    private var edialog1: Dialog? = null
    private var edialog2: Dialog? = null
    private var phone: String? = null
    private var inputPhone: onInputPhone? = null
    private var inputCode: onInputCode? = null

    interface onInputPhone {
        fun inputPhonenext(phone: String?, type: Int)
    }

    interface onInputCode {
        fun inputCodenext(phone: String?, code: String)
    }

    /**
     * 设置时间到了监听
     */
    fun setonInputPhoneListener(inputPhone: onInputPhone): LDialogBuilder {
        this.inputPhone = inputPhone
        return this
    }

    /**
     * 设置时间到了监听
     */
    fun setonInputCodeListener(inputCode: onInputCode): LDialogBuilder {
        this.inputCode = inputCode
        return this
    }

    fun inputphone(): LDialogBuilder {
        edialog1 = EDialogBuilder(context, 0, 1.0f).setTouchOutSideCancelable(false)
                .setMessage("您的手机号码", NDialogBuilder.MSG_LAYOUT_LEFT)
                .setInputtype(InputType.TYPE_CLASS_NUMBER)
                .setDialogAnimation(EDialogBuilder.DIALOG_ANIM_SLID_RIGHT)
                .setBtnClickListener(false, "下一步", object : EDialogBuilder.onDialogbtnClickListener {
                    override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int, inputtext: String) {
                        if (whichBtn == 1) {
                            phone = inputtext
                            if (phone!!.isEmpty() || phone!!.length != 11) {
                                Toast.makeText(context, "手机号不符合要求", Toast.LENGTH_SHORT).show()
                            } else {
                                inputPhone!!.inputPhonenext(phone, 0)
                            }
                        }
                    }
                })
                .create()
        edialog1!!.show()
        return this
    }


    fun inputcode(phonenum: String): LDialogBuilder {
        edialog2 = EDialog2Builder(context, 0, 1.0f)
                .setTouchOutSideCancelable(false)
                .setTitle("输入验证码")
                .setMessage("验证码已发送至$phonenum", NDialogBuilder.MSG_LAYOUT_CENTER)
                .setDialogAnimation(EDialogBuilder.DIALOG_ANIM_SLID_RIGHT)
                .setBtnClickListener(false, "点击重新获取", object : EDialog2Builder.onDialogbtnClickListener {
                    override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int) {
                        if (whichBtn == 1) {
                            inputPhone!!.inputPhonenext(phone, 1)
                        }
                    }
                })
                .setonInputFinishListener(object : EDialog2Builder.onInputFinishListener {
                    override fun onInputFinish(vcode: String) {
                        inputCode!!.inputCodenext(phone, vcode)
                    }
                })
                .setOnTimeoutListener(object : EDialog2Builder.onOutTimeListener {
                    override fun onOutTime() {}
                })
                .create()
        edialog2!!.show()
        return this
    }

    fun dismissdialog1() {
        if (edialog1 != null && edialog1!!.isShowing) {
            edialog1!!.dismiss()
        }
    }

    fun dismissdialog2() {
        if (edialog2 != null && edialog2!!.isShowing) {
            edialog2!!.dismiss()
        }
    }
}
