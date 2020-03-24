package com.android.baseproject.customview

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.android.baseproject.R

/*
*Created by NhiNguyen on 8/21/2019.
*/

class LoadingProgress(context: Context) : Dialog(context) {

    private var countLoading = 0

    init {
        initLoadingProgress(context)
    }

    private fun initLoadingProgress(context: Context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.loading_progress)
        this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
    }

    override fun show() {
        try {
            if (countLoading == 0) {
                super.show()
            }
            countLoading++
        } catch (e: Exception) {
        }
    }

    override fun dismiss() {
        countLoading--
        if (countLoading > 0 || !super.isShowing()) return
        super.dismiss()
    }

    fun forceDismiss() {
        countLoading = 0
        super.dismiss()
    }
}