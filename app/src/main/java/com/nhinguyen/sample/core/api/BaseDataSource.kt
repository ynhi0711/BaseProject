package com.nhinguyen.sample.core.api

import com.nhinguyen.sample.core.data.Result
import retrofit2.Response
import java.io.IOException

/**
 * Abstract BaseDataSource class with getResultFailure handling
 */
abstract class BaseDataSource {

    enum class ApiErrorCode(val stringValue: String) {
        INVALID_PARAMETER("INVALID_PARAMETER"),
        BAD_REQUEST("BAD_REQUEST"),
        UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS"),
        ACTION_NOT_ALLOWED("ACTION_NOT_ALLOWED"),
        OBJECT_NOT_FOUND("OBJECT_NOT_FOUND"),
        METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED"),
        INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
        IO_EXCEPTION("IO_EXCEPTION"),
        TIMEOUT("TIMEOUT")
    }

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
                if (response.code() == 204) return Result.SuccessEmptyBody()
            }
            getResultFailure(response)
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IOException -> // mean something wrong with network connection
                    Result.Failure(
                        errorId = "",
                        message = "No internet. Please check your connection!",
                        code = ApiErrorCode.IO_EXCEPTION.stringValue,
                        errors = null,
                        title = ""
                    )
                else -> getResultFailure()
            }
        }
    }

    private fun <T> getResultFailure(response: Response<T>): Result<T> {
        val errorString = response.errorBody()?.string() ?: "{}"
        return (Result.Failure(
            errorId = "",
            code = response.code().toString(),
            message = response.message(),
            title = "",
            errors = null
        ))
    }
}

private fun <T> getResultFailure(): Result<T> {
    return Result.Failure(
        errorId = "",
        message = "",
        code = null,
        title = "",
        errors = null
    )
}

fun String?.toApiErrorCode(): BaseDataSource.ApiErrorCode? {
    return BaseDataSource.ApiErrorCode.values()
        .firstOrNull { this?.contains(it.stringValue, true) ?: false }
}