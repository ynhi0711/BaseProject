package com.cavice.customer.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cavice.customer.R
import com.cavice.customer.model.error.ErrorMessage
import com.cavice.customer.model.error.OrderSocket
import com.cavice.customer.view.CustomToolbar
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/*
*Created by NhiNguyen on 8/21/2019.
*/

abstract class BaseFragment<T : IBaseViewModel> : DaggerFragment(), BaseFragmentView {

    @Inject
    protected lateinit var viewModel: T

    val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel as LifecycleObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = view ?: inflater.inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    protected open fun initView() {
        context?.apply {
            view?.findViewWithTag<CustomToolbar>(getString(R.string.tag_toolbar))?.onBackClicked {
                activity?.onBackPressed()
            }
        }
        viewModel.apply {
            mRxPermissions = RxPermissions(this@BaseFragment)
            error.observe(viewLifecycleOwner, Observer {
                handleError(it)
            })
            isLoading.observe(viewLifecycleOwner, Observer {
                if (it) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            })
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    open fun updateOrderDetails(data: OrderSocket) {
        /*TODO*/
    }

    protected open fun showLoadingDialog() {
        (activity as? BaseActivity<*>)?.showLoadingDialog()
    }

    protected open fun dismissLoadingDialog() {
        (activity as? BaseActivity<*>)?.dismissLoadingDialog()
    }

    protected open fun handleError(errorMessage: ErrorMessage?) {
        (activity as? BaseActivity<*>)?.handelError(errorMessage)
    }

    @SuppressLint("CheckResult")
    fun openGallery() {
        viewModel.mRxPermissions?.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            ?.subscribe { granted ->
                if (granted) {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { onChooseImage(it) }
        }
    }

    override fun onChooseImage(uri: Uri) {}

    override fun onDestroy() {
        super.onDestroy()
        try {
            dismissLoadingDialog()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val GALLERY_REQUEST = 1001
    }
}