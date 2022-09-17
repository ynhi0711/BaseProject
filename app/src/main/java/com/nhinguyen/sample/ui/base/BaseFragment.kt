package com.nhinguyen.sample.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nhinguyen.sample.utils.hideNavigationBottomWhenDialogShowing
import dagger.android.support.AndroidSupportInjection
import com.nhinguyen.sample.core.data.Result
import com.nhinguyen.sample.di.Injectable

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    abstract val resLayoutId: Int
    lateinit var binding: T
    abstract fun initViews()
    abstract fun subscribeUI()

    override fun onAttach(context: Context) {
        if (this is Injectable) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<T>(inflater, resLayoutId, container, false)
            .apply { binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeUI()
    }

    fun <T> handleNetworkResult(
        result: Result<T>?,
        success: (com.nhinguyen.sample.core.data.Result.Success<T>) -> Unit = {},
        successEmptyBody: () -> Unit = {},
        fail: ((Result.Failure<T>) -> Unit)? = null,
        loading: (Result.Loading<T>) -> Unit = {},
        beforeLoading: (Result.BeforeLoading<T>) -> Unit = {}
    ) {
        when (result) {
            is Result.BeforeLoading -> beforeLoading(result)
            is Result.Loading -> loading(result)
            is Result.Success -> success(result)
            is Result.SuccessEmptyBody -> successEmptyBody()

            is Result.Failure -> {
                if (fail != null) {
                    fail(result)
                }
            }
        }
    }

    fun showErrorDialog(
        title: String?,
        content: String,
        onPositiveClickCallback: (() -> Unit)? = null
    ) {
        context?.let {
            val alertDialog = AlertDialog.Builder(it).create()
            title?.let { alertDialog.setTitle(title) }
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setMessage(content)
            alertDialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "OK"
            ) { dialog, _ ->
                dialog.dismiss()
                onPositiveClickCallback?.invoke()
            }
            alertDialog.hideNavigationBottomWhenDialogShowing(requireActivity())
            alertDialog.show()
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
        context?.let {
            val alertDialog = AlertDialog.Builder(it).create()
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
    }

    fun popBackStackByCurrentFragmentId(
        currentFragmentId: Int,
        destinationId: Int? = null
    ) {
        if (findNavController().currentDestination?.id == currentFragmentId) {
            destinationId?.let {
                findNavController().popBackStack(destinationId, false)
            } ?: run {
                findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
//
//    open fun showLoading(isShow: Boolean) = (requireActivity() as MainActivity).showLoading(isShow)
//    open fun isShowLoading() = (requireActivity() as MainActivity).isShowLoading()
}
