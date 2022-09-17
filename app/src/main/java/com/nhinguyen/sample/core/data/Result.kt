package com.nhinguyen.sample.core.data

import org.json.JSONArray

sealed class Result<out T> {
    class BeforeLoading<out T> : Result<T>()
    class Loading<out T> : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    class SuccessEmptyBody<out T> : Result<T>()
    data class Failure<out T>(
        val errorId: String,
        val message: String,
        val code: String? = null,
        val title: String? = null,
        val errors: JSONArray? = null
    ) : Result<T>() {
        val parsedErrors: HashMap<String, String> = HashMap()
            get() {
                if (errors == null) return field
                for (i in 0 until errors.length()) {
                    val jsonObject = errors.getJSONObject(i)
                    val fieldJson = jsonObject.getString("field")
                    val message = jsonObject.getString("message")
                    field[fieldJson] = message
                }
                return field
            }
    }

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is SuccessEmptyBody -> "SuccessEmptyBody"
            is Failure -> "Error[exception=$message]"
            is Loading -> "Loading"
            is BeforeLoading -> "BeforeLoading"
        }
    }
}