package com.sayed.thirdway.ui.base

import android.app.ProgressDialog
import android.content.Context
import com.sayed.thirdway.R

import java.util.concurrent.atomic.AtomicInteger

class Loading(context: Context) {

    private val loadingDialog: ProgressDialog?
    private val count = AtomicInteger()

    init {
        loadingDialog = ProgressDialog(context)
        loadingDialog.setMessage(context.resources.getString(R.string.loading))
        loadingDialog.setCancelable(false)
        loadingDialog.setCanceledOnTouchOutside(false)
    }

    fun show(show: Boolean) {
        if (show) {
            if (loadingDialog != null && count.getAndIncrement() == 0) {
                loadingDialog.show()
            }
        } else {
            if (loadingDialog != null && count.decrementAndGet() == 0 && loadingDialog.isShowing) {
                loadingDialog.dismiss()
            }
            if (count.get() < 0) count.set(0)
        }
    }

    fun setLoadingMsg(msg: String) {
        loadingDialog!!.setMessage(msg)
    }
}