package com.cavice.customer.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.android.baseproject.customview.LoadingProgress
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

/*
*Created by NhiNguyen on 8/20/2019.
*/

abstract class BaseActivity<T : IBaseViewModel> : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: T

    private var loadingProgress: LoadingProgress? = null
    private var errorDialog: ErrorDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        lifecycle.addObserver(viewModel as LifecycleObserver)
        viewModel.mRxPermissions = RxPermissions(this)
        initView()
    }

    /**
     * Define the layout res id can be used to [Activity.setContentView]
     *
     * @return the layout res id
     */
    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    /**
     * Init [View] components here. Such as set adapter for [RecyclerView], set listener
     * or anything else
     */
    protected open fun initView() {
        viewModel.error.observe(this, Observer {

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            loadingProgress?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun showLoadingDialog() {
        if (loadingProgress == null) {
            loadingProgress = LoadingProgress(this)
        }
        loadingProgress?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    open fun dismissLoadingDialog() {
        try {
            loadingProgress?.let {
                it.dismiss()
                loadingProgress = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun handelError(errorMessage: ErrorMessage?) {
        errorMessage?.let {
            when (errorMessage.error) {
                CommonError.NETWORK_ERROR -> errorMessage.message = getString(R.string.no_network)
                CommonError.UNAUTHENTICATED -> {
                    Timber.e("Error Here")
                    Prefs(context = this).clearApiToken()
                    Prefs(context = this).clearUserInfo()
                    RxEvent.send(Event.LoginEvent(false, null))
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    return
                }
                else -> {
                }
            }
            showErrorDialog(errorMessage.message)
            viewModel.error.postValue(null)
        }
    }

    fun showErrorDialog(message: String) {
        if (errorDialog == null) {
            errorDialog = ErrorDialog(
                context = this,
                message = message,
                textCancel = getString(R.string.text_ok)
            ) {
                errorDialog = null
            }
        }
        errorDialog?.apply {
            if (!isShowing) {
                setMessage(message)
                show()
            }
        }
    }

    fun showKeyboard(context: Context?, isShow: Boolean, view: View) {
        runOnUiThread {
            this@BaseActivity.currentFocus?.let {
                try {
                    val imm =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (isShow)
                        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                    else
                        imm?.hideSoftInputFromWindow(
                            it.applicationWindowToken, 0
                        )

                } catch (e: IllegalStateException) {
                } catch (e: Exception) {
                }
            }
        }
    }

    fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        navHostFragment?.childFragmentManager?.fragments?.let {
            if (it.isNotEmpty()) {
                return it[0]
            }
        }
        return null
    }
}