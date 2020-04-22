package x.com.hl_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import x.com.hl_base.dialog.CommonDialog
import x.com.hl_tools.hl_permissions

class CPermissionDialog2 : CommonDialog() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cdialog_permission, container, false)
    }

    override fun onViewCreated(mView: View, savedInstanceState: Bundle?) {
        mView.findViewById<View>(R.id.cDialogPermissionGoto).setOnClickListener { hl_permissions.applyWriteStoragePermission(context, null) }
        mView.findViewById<View>(R.id.cDialogPermissionReject).setOnClickListener { dismiss() }
    }
}