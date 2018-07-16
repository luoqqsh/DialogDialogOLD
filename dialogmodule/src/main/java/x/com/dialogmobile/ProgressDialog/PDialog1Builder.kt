package x.com.dialogmobile.ProgressDialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import x.com.dialogmobile.NDialogBuilder
import x.com.dialogmobile.R

/**
 * 加载进度框，10秒后弹出出错提示
 */
class PDialog1Builder
/**
 * 构造器
 *
 * @param context
 * @param layoutStyle      布局样式
 * @param isSystemAlert    是否是系统弹框（service等地方用到系统级别不依赖activity）
 * @param widthcoefficient 对话框宽度所占屏幕宽度的比重（0-1）
 */
private constructor(private val context: Context, layoutStyle: Int, isSystemAlert: Boolean, widthcoefficient: Float, dimEnable: Boolean) {
    private val dialog: Dialog
    private var errordialog: Dialog? = null
    private var dialogMsg: TextView? = null

    /**
     * 构造器
     *
     * @param context          上下文
     * @param layoutStyle      对话框布局样式
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    constructor(context: Context, layoutStyle: Int, widthcoefficient: Float) : this(context, layoutStyle, false, widthcoefficient, dimEnable) {}

    init {
        var layoutStyle = layoutStyle
        val dialog: Dialog
        if (dimEnable) {
            dialog = Dialog(context, R.style.Dialog)
        } else {
            dialog = Dialog(context, R.style.DialogDim)
        }
        // 设置对话框风格
        if (layoutStyle == 0) {
            layoutStyle = R.layout.pdialog_layout
        }
        dialog.setContentView(layoutStyle)
        val window = dialog.window
        // 是否系统级弹框
        if (isSystemAlert) {
            window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        // 获取屏幕宽度
        val metrics = DisplayMetrics()
        window!!.windowManager.defaultDisplay.getMetrics(metrics)
        val screenwidth = metrics.widthPixels
        var width = 0
        if (widthcoefficient > 0) {
            width = (screenwidth * widthcoefficient).toInt()
        } else {
            width = (screenwidth * WIDTHFACTOR).toInt()
        }
        // 设置对话框宽度
        window.attributes.width = width
        window.setGravity(Gravity.CENTER)
        this.dialog = dialog
    }


    //属性设置
    fun setTouchOutSideCancelable(touchOutSideCancel: Boolean): PDialog1Builder {
        this.dialog.setCanceledOnTouchOutside(touchOutSideCancel)
        this.dialog.setCancelable(touchOutSideCancel)
        return this
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message 消息内容
     * @return this
     */
    fun setMessage(message: String?): PDialog1Builder {
        dialogMsg = dialog.findViewById(R.id.pdialog_message)
        if (dialogMsg != null) {
            if (message != null) {
                dialogMsg!!.text = message
                dialogMsg!!.visibility = View.VISIBLE
            } else {
                dialogMsg!!.visibility = View.GONE
            }
        }
        return this
    }

    fun create(): Dialog {
        if (context is Activity) {
            dialog.ownerActivity = context
        }
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                //显示错误对话框
                dialog.dismiss()
                errordialog = NDialogBuilder(context, 0, 1.0f)
                        .setTouchOutSideCancelable(false)
                        .setMessage("345tret", NDialogBuilder.MSG_LAYOUT_LEFT)
                        .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
                        .setBtnClickListener(true, "", object : NDialogBuilder.onDialogbtnClickListener {
                            override fun onDialogbtnClick(context: Context, dialog: Dialog, whichBtn: Int) {
                                errordialog!!.dismiss()
                            }
                        })
                        .create()
                errordialog!!.show()
            }
        }.start()
        return dialog
    }

    companion object {
        // 弹出dialog时候是否要显示阴影
        private val dimEnable = true
        /**
         * 对话框透明比例
         */
        val ALPHAFACTOR = 1.0f
        /**
         * 对话框宽度所占屏幕宽度的比例
         */
        val WIDTHFACTOR = 0.75f
    }
}
