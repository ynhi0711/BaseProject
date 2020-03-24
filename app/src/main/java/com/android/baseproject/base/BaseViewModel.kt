package com.cavice.customer.base

import androidx.lifecycle.*
import com.cavice.customer.model.Order
import com.cavice.customer.model.error.ErrorMessage
import com.cavice.customer.util.Constant
import com.cavice.customer.util.OrderStatus
import com.cavice.customer.util.rx_event.Event
import com.cavice.customer.util.rx_event.RxEvent
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/*
*Created by NhiNguyen on 8/21/2019.
*/

abstract class BaseViewModel : ViewModel(),
    IBaseViewModel, LifecycleObserver {

    private var compositeDisposable = CompositeDisposable()
    private var currentDisposable: Disposable? = null

    override var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    override var error: MutableLiveData<ErrorMessage> = MutableLiveData()

    override var mRxPermissions: RxPermissions? = null

    fun addDisposable(disposable: Disposable, isSaveDisposable: Boolean = false) {
        if (isSaveDisposable) {
            currentDisposable = disposable;
        }
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable.add(disposable)
    }

    fun removeDisposable() {
        currentDisposable?.apply {
            compositeDisposable.remove(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        addDisposable(
            RxEvent.listen(Event.ChangeStatusOrder::class.java)
                .subscribe { event ->
                    event.socket.data?.let {
                        onOrderChanged(it)
                    }
                }
        )
    }

    protected open fun onOrderChanged(socket: Order) {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }
}