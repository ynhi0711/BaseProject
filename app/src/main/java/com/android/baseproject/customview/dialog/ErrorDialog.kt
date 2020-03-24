package com.android.baseproject.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.android.baseproject.R
import com.cavice.customer.R
import com.cavice.customer.extension.visible
import kotlinx.android.synthetic.main.dialog_error_layout.*

/*
*Created by NhiNguyen on 10/4/2019.
*/

class ErrorDialog(
    context: Context,
    private val message: String?,
    private val title: String? = null,
    private val textConfirm: String? = null,
    private val textCancel: String? = null,
    private val onCancel: (() -> Unit)? = null,
    private val onTryAgain: (() -> Unit)? = null
) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error_layout)
        setCancelable(true)
        window?.apply {
            setBackgroundDrawableResource(R.color.color_transparent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageDialog?.visibility = View.GONE
        title?.let { tvTitle?.text = it }
        message?.let { tvMessage?.text = it }
        textConfirm?.let {
            tvConfirm?.text = it
            tvConfirm?.visible()
        }
        textCancel?.let { tvCancel?.text = it }
        tvCancel?.setOnClickListener {
            onCancel?.invoke()
            dismiss()
        }
        tvConfirm?.setOnClickListener {
            onTryAgain?.invoke()
            dismiss()
        }
    }

    fun setMessage(message: String) {
        tvMessage?.text = message
    }
}