package com.cavice.customer.base

import androidx.lifecycle.MutableLiveData
import com.cavice.customer.model.error.ErrorMessage
import com.tbruyelle.rxpermissions2.RxPermissions

interface IBaseViewModel {
    var isLoading: MutableLiveData<Boolean>
    var error: MutableLiveData<ErrorMessage>
    var mRxPermissions: RxPermissions?
    fun onCreate()
    fun onStart()
    fun onPause()
    fun onResume()
    fun onStop()
    fun onDestroy()
}