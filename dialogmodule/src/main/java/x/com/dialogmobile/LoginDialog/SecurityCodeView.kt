package x.com.dialogmobile.LoginDialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import x.com.dialogmobile.R

/**
 * Created by xiaviv on 16/9/27.
 */

class SecurityCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private val editText: EditText
    private val TextViews: Array<TextView?>
    private val stringBuffer = StringBuffer()
    private var count = 4
    /**
     * 获取输入文本
     *
     * @return
     */
    var editContent: String? = null
        private set

    private var inputCompleteListener: InputCompleteListener? = null

    init {
        TextViews = arrayOfNulls(4)
        View.inflate(context, R.layout.securitycode_layout, this)

        editText = findViewById(R.id.et)
        TextViews[0] = findViewById(R.id.item_code_iv1)
        TextViews[1] = findViewById(R.id.item_code_iv2)
        TextViews[2] = findViewById(R.id.item_code_iv3)
        TextViews[3] = findViewById(R.id.item_code_iv4)

        editText.isCursorVisible = false//将光标隐藏
        setListener()
    }

    private fun setListener() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                //如果字符不为""时才进行操作
                if (editable.toString() != "") {
                    if (stringBuffer.length > 3) {
                        //当文本长度大于3位时editText置空
                        editText.setText("")
                        return
                    } else {
                        //将文字添加到StringBuffer中
                        stringBuffer.append(editable)
                        editText.setText("")//添加后将EditText置空
                        count = stringBuffer.length
                        editContent = stringBuffer.toString()
                        if (stringBuffer.length == 4) {
                            //文字长度位4  则调用完成输入的监听
                            if (inputCompleteListener != null) {
                                inputCompleteListener!!.inputComplete()
                            }
                        }
                    }

                    for (i in 0 until stringBuffer.length) {
                        TextViews[i]!!.text = editContent!![i].toString()
                        TextViews[i]!!.setBackgroundResource(R.drawable.bg_user_verify_code_blue)
                    }
                }
            }
        })

        editText.setOnKeyListener(OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                return@OnKeyListener if (onKeyDelete()) true else true
            }
            false
        })
    }


    fun onKeyDelete(): Boolean {
        if (count == 0) {
            count = 4
            return true
        }
        if (stringBuffer.length > 0) {
            //删除相应位置的字符
            stringBuffer.delete(count - 1, count)
            count--
            editContent = stringBuffer.toString()
            TextViews[stringBuffer.length]!!.text = ""
            TextViews[stringBuffer.length]!!.setBackgroundResource(R.drawable.bg_user_verify_code_grey)
            if (inputCompleteListener != null)
                inputCompleteListener!!.deleteContent(true)//有删除就通知manger

        }
        return false
    }

    /**
     * 清空输入内容
     */
    fun clearEditText() {
        stringBuffer.delete(0, stringBuffer.length)
        editContent = stringBuffer.toString()
        for (i in TextViews.indices) {
            TextViews[i]!!.text = ""
            TextViews[i]!!.setBackgroundResource(R.drawable.bg_user_verify_code_grey)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    fun setInputCompleteListener(inputCompleteListener: InputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener
    }

    interface InputCompleteListener {
        fun inputComplete()

        fun deleteContent(isDelete: Boolean)
    }

}