package com.nhinguyen.sample.utils

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi

@Suppress("unused")
class SPUtils(context: Context, private val moshi: Moshi) {

    private val sp: SharedPreferences =
        context.getSharedPreferences("back_yard", Context.MODE_PRIVATE)

    var sortType: String?
        get() = sp.getString(KEY_SORT_TYPE, null)
        set(sort) = sp.edit().putString(KEY_SORT_TYPE, sort).apply()

    companion object {
        const val KEY_SORT_TYPE = "sort_type"
    }
}
