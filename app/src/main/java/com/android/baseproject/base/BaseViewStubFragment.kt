package com.cavice.customer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import com.cavice.customer.R
import com.cavice.customer.model.error.ErrorMessage
import com.cavice.customer.view.CustomToolbar
import com.tbruyelle.rxpermissions2.RxPermissions


/*
*Created by NhiNguyen on 8/21/2019.
*/

abstract class BaseViewStubFragment<T : IBaseViewModel> : BaseFragment<T>(), BaseFragmentView {

    private var mSavedInstanceState: Bundle? = null
    private var hasInflated: Boolean = false
    private var mViewStub: ViewStub? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_base, container, false)
        mViewStub = view.findViewById(R.id.fragmentViewStub) as ViewStub
        mViewStub!!.layoutResource = getLayoutId()
        mSavedInstanceState = savedInstanceState

        if (!hasInflated) {
            val inflatedView = mViewStub!!.inflate()
            initView(inflatedView, mSavedInstanceState)
            afterViewStubInflated(view)
        }

        return view
    }

    /**
     *
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected fun afterViewStubInflated(originalViewContainerWithViewStub: View?) {
        hasInflated = true
        if (originalViewContainerWithViewStub != null) {
            val pb =
                originalViewContainerWithViewStub.findViewById<ProgressBar>(R.id.inflateProgressbar)
            pb.visibility = View.GONE
        }
    }

    protected open fun initView(inflatedView: View, savedInstanceState: Bundle?) {
        context?.apply {
            view?.findViewWithTag<CustomToolbar>(getString(R.string.tag_toolbar))?.onBackClicked {
                activity?.onBackPressed()
            }
        }
        viewModel.apply {
            mRxPermissions = RxPermissions(this@BaseViewStubFragment)
            error.observe(viewLifecycleOwner, Observer {
                (activity as? BaseActivity<*>)?.handelError(it)
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

    override fun onResume() {
        super.onResume()
        if (mViewStub != null && !hasInflated) {
            val inflatedView = mViewStub!!.inflate()
            initView(inflatedView, mSavedInstanceState)
            afterViewStubInflated(view)
        }
    }

    override fun onDetach() {
        super.onDetach()
        hasInflated = false
    }
}