package com.nhinguyen.sample.ui.base

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nhinguyen.sample.R
import com.nhinguyen.sample.di.Injectable
import com.nhinguyen.sample.utils.hideNavigationBottomWhenDialogShowing
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (this is Injectable) {
            AndroidInjection.inject(this)
        }
        super.onCreate(savedInstanceState)
    }

    private var isShowNoInternetDialog = false

    fun showErrorDialog(content: String, onDismissCallback: ((DialogInterface) -> Unit)?) {
        showErrorDialog(
            title = null,
            content = content,
            positiveTitle = null,
            negativeTitle = null,
            onPositiveClickCallback = null,
            onNegativeClickCallback = null,
            isPreventDismiss = false,
            onDismissCallback
        )
    }

    fun showErrorDialog(title: String, content: String) {
        showErrorDialog(
            title = title,
            content = content,
            positiveTitle = null,
            negativeTitle = null,
            onPositiveClickCallback = null,
            onNegativeClickCallback = null,
            isPreventDismiss = false
        )
    }

    fun showErrorDialog(
        title: String? = null,
        content: String,
        positiveTitle: String? = null,
        negativeTitle: String? = null,
        onPositiveClickCallback: ((DialogInterface) -> Unit)? = null,
        onNegativeClickCallback: ((DialogInterface) -> Unit)? = null,
        isPreventDismiss: Boolean = false,
        onDismissCallback: ((DialogInterface) -> Unit)? = null
    ) {
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
        alertDialog = AlertDialog.Builder(this).create()
        title?.let { alertDialog?.setTitle(title) }
        alertDialog?.setCancelable(false)
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.setMessage(content)
        negativeTitle?.let {
            alertDialog?.setButton(
                DialogInterface.BUTTON_NEGATIVE, it
            ) { dialog, _ ->
                onNegativeClickCallback?.invoke(dialog)
            }
        }
        alertDialog?.setButton(
            DialogInterface.BUTTON_POSITIVE, positiveTitle ?: "OK"
        ) { dialog, _ ->
            if (!isPreventDismiss) onPositiveClickCallback?.invoke(dialog)
        }
        alertDialog?.hideNavigationBottomWhenDialogShowing(this)
        alertDialog?.setOnDismissListener {
            onDismissCallback?.invoke(it)
        }
        alertDialog?.show()
        if (isPreventDismiss) {
            alertDialog!!.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    onPositiveClickCallback?.invoke(alertDialog!!)
                }
        }
    }

    fun showConfirmDialog(
        title: String?,
        content: String,
        positiveContent: String?,
        negativeContent: String?,
        onPositiveClickCallback: (() -> Unit)? = null,
        onNegativeClickCallback: (() -> Unit)? = null
    ) {
        val alertDialog = AlertDialog.Builder(this).create()
        title?.let { alertDialog.setTitle(title) }
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setMessage(content)
        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE, positiveContent
        ) { dialog, _ ->
            dialog.dismiss()
            onPositiveClickCallback?.invoke()
        }
        alertDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE, negativeContent
        ) { dialog, _ ->
            dialog.dismiss()
            onNegativeClickCallback?.invoke()
        }
        alertDialog.show()
    }

    fun hideDialog() {
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
    }

    fun showDialogNoInternet() {
        if (!isShowNoInternetDialog) {
            isShowNoInternetDialog = true
            showErrorDialog(
                title = getString(R.string.text_error),
                content = getString(R.string.text_no_internet),
                positiveTitle = null,
                negativeTitle = null,
                onPositiveClickCallback = null,
                onNegativeClickCallback = null,
                isPreventDismiss = false,
                onDismissCallback = {
                    isShowNoInternetDialog = false
                }
            )
        }
    }

    fun hideDialogNoInternet() {
        if (isShowNoInternetDialog) {
            hideDialog()
            isShowNoInternetDialog = false
        }
    }
}