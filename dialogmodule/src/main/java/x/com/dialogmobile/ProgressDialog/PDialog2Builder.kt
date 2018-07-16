package x.com.dialogmobile.ProgressDialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import x.com.dialogmobile.R

class PDialog2Builder
/**
 * 构造器
 *
 * @param context          context
 * @param layoutStyle      布局样式
 * @param isSystemAlert    是否是系统弹框（service等地方用到系统级别不依赖activity）
 * @param widthcoefficient 对话框宽度所占屏幕宽度的比重（0-1）
 */
private constructor(private val context: Context, layoutStyle: Int, isSystemAlert: Boolean, widthcoefficient: Float, dimEnable: Boolean) {
    private val dialog: Dialog?
    private var dialogMsg: TextView? = null
    private var tvCancel: TextView? = null
    private var tvSure: TextView? = null
    private var tvProgress: TextView? = null
    private var progressBar: ProgressBar? = null

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
            layoutStyle = R.layout.download_progress_layout
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
        this.dialog = dialog
    }


    //属性设置
    fun setTouchOutSideCancelable(touchOutSideCancel: Boolean): PDialog2Builder {
        this.dialog!!.setCanceledOnTouchOutside(touchOutSideCancel)
        this.dialog.setCancelable(touchOutSideCancel)
        return this
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message 消息内容
     * @return this
     */
    fun setMessage(message: String?): PDialog2Builder {
        dialogMsg = dialog!!.findViewById(R.id.mdialog_message)
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

    /**
     * 取消按钮
     *
     * @param cancelTitle 取消按钮名
     * @param listener    按钮监听
     * @return this
     */
    fun setBtnCancel(cancelTitle: String?, listener: View.OnClickListener): PDialog2Builder {
        if (tvCancel == null) {
            tvCancel = dialog!!.findViewById(R.id.tv_cancel)
        }
        if (dialog != null) {
            if (cancelTitle != null) {
                tvCancel!!.text = cancelTitle
                tvCancel!!.visibility = View.VISIBLE
            } else {
                tvCancel!!.visibility = View.GONE
            }
        }
        tvCancel!!.setOnClickListener(listener)
        return this
    }

    /**
     * 确定按钮
     *
     * @param cancleTitle 确定按钮名
     * @param listener    按钮监听
     * @return this
     */
    fun setBtnSure(cancleTitle: String?, listener: View.OnClickListener): PDialog2Builder {
        if (tvSure == null) {
            tvSure = dialog!!.findViewById(R.id.tv_sure)
        }
        if (dialog != null) {
            if (cancleTitle != null) {
                tvSure!!.text = cancleTitle
                tvSure!!.visibility = View.VISIBLE
            } else {
                tvSure!!.visibility = View.GONE
            }
        }
        tvSure!!.setOnClickListener(listener)
        return this
    }

    /**
     * 设置取消、确定按钮是否显示
     *
     * @param btnCancle 取消按钮
     * @param btnSure 确认按钮
     * @return this
     */
    fun setBtnVisity(btnCancle: Boolean, btnSure: Boolean): PDialog2Builder {
        if (tvCancel != null) {
            tvCancel!!.visibility = if (btnCancle) View.VISIBLE else View.GONE
        }
        if (tvSure != null) {
            tvSure!!.visibility = if (btnSure) View.VISIBLE else View.GONE
        }
        return this
    }

    /**
     * 设置取消、确定按钮是否显示
     *
     * @param progress 进度
     * @return this
     */
    fun setProgress(progress: Int): PDialog2Builder {
        if (progressBar == null) {
            progressBar = dialog!!.findViewById(R.id.progress)
        }
        if (tvProgress == null) {
            tvProgress = dialog!!.findViewById(R.id.tv_progress)
        }
        progressBar!!.progress = progress
        tvProgress!!.text = progress.toString() + "%"
        return this
    }

    /**
     * 进度框超时监听
     *
     * @author zhl
     */
    interface onProgressOutTimeListener {
        fun onProgressOutTime(dialog: Dialog, dialogMsgTextView: TextView)
    }

    /**
     * 创建对话框
     *
     * @return dialog
     */

    fun create(): Dialog {
        if (context is Activity) {
            dialog!!.ownerActivity = context
        }
        return this.dialog!!
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
