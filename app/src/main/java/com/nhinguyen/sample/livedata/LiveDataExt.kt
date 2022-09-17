package com.nhinguyen.sample.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun <T> LiveData<T>.toMultipleLiveEvent(): MultipleLiveEvent<T> {
    val result = MultipleLiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}

fun <X, Y> LiveData<X>.switchLiveEvent(transform: (X) -> LiveData<Y>): MultipleLiveEvent<Y> {
    return this.switchMap { transform(it) }.toMultipleLiveEvent()
}


fun <T> Flow<T>.asLiveDataNotReExecuted(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO
): LiveData<T> {
    val result = MutableLiveData<T>()
    scope.launch(context) {
        collect {
            result.postValue(it)
        }
    }
    return result
}