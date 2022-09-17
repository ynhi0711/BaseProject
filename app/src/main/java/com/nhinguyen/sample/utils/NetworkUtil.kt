package com.nhinguyen.sample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings

object NetworkUtil {

    fun isAirplaneModeOn(context: Context): Int {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        )
    }

    fun isConnectedToInternet(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.run {
                getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    result = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                }
            }
        } else {
            connectivityManager?.run {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    result = type == ConnectivityManager.TYPE_WIFI
                            || type == ConnectivityManager.TYPE_MOBILE
                }
            }
        }
        return result
    }
}