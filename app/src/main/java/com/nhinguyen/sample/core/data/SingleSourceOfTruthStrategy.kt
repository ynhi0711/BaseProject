package com.nhinguyen.sample.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.Loading())
        val source = databaseQuery.invoke().map { Result.Success(it) as Result<T> }

        val responseStatus = networkCall.invoke()
        if (responseStatus is Result.Success) {
            saveCallResult(responseStatus.data!!)
            emitSource(source)
        } else if (responseStatus is Result.Failure) {
            emit(
                Result.Failure(
                    errorId = responseStatus.errorId,
                    message = responseStatus.message,
                    code = responseStatus.code,
                    title = responseStatus.title,
                    errors = responseStatus.errors
                )
            )
        }
    }

fun <T> resultLiveDataNoCache(
    networkCall: suspend () -> Result<T>
): LiveData<Result<T>> = liveData(Dispatchers.IO) {
    emit(Result.Loading())
    val responseStatus = networkCall.invoke()
    if (responseStatus is Result.Success) {
        emit(responseStatus)
    } else if (responseStatus is Result.Failure) {
        emit(responseStatus)
    }
}

fun <A> resultFlowNoCache(
    networkCall: suspend () -> Result<A>,
    emitLoading: Boolean = true
): Flow<Result<A>> = flow<Result<A>> {
    if (emitLoading) emit(Result.Loading())
    val responseStatus = networkCall.invoke()
    if (responseStatus is Result.Success) {
        emit(Result.Success(responseStatus.data!!))
    } else if (responseStatus is Result.Failure) {
        emit(
            Result.Failure(
                errorId = responseStatus.errorId,
                message = responseStatus.message,
                code = responseStatus.code,
                errors = responseStatus.errors,
                title = responseStatus.title
            )
        )
    }
}.flowOn(Dispatchers.IO)

fun <T> resultLiveEvent(
    networkCall: suspend () -> Result<T>
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.Loading())
        val responseStatus = networkCall.invoke()
        emit(responseStatus)
    }

// 2 methods use to avoid case loading screen display too much
fun <T, A> resultLiveDataWithDelay(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        var responseStatus: Result<A>? = null
        emit(Result.BeforeLoading())
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            if (responseStatus == null && isActive) {
                emit(Result.Loading())
            }
        }
        val source = databaseQuery.invoke().map { Result.Success(it) as Result<T> }
        responseStatus = networkCall.invoke()
        if (responseStatus is Result.Success) {
            saveCallResult(responseStatus.data!!)
            emitSource(source)
        } else if (responseStatus is Result.Failure) {
            emit(
                Result.Failure(
                    errorId = responseStatus.errorId,
                    message = responseStatus.message,
                    code = responseStatus.code,
                    errors = responseStatus.errors
                )
            )
        }
    }

fun <T> resultLiveDataNoCacheWithDelay(
    networkCall: suspend () -> Result<T>
): LiveData<Result<T>?> = liveData(Dispatchers.IO) {
    var responseStatus: Result<T>? = null
    emit(Result.BeforeLoading())
    CoroutineScope(Dispatchers.IO).launch {
        delay(2000)
        if (responseStatus == null && isActive) {
            emit(Result.Loading())
        }
    }
    responseStatus = networkCall.invoke()
    if (responseStatus is Result.Success) {
        emit(responseStatus)
    } else if (responseStatus is Result.Failure) {
        emit(responseStatus)
    }

}