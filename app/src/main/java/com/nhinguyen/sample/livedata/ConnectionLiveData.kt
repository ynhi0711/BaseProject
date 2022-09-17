package com.nhinguyen.sample.livedata

import android.annotation.TargetApi
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.nhinguyen.sample.utils.NetworkUtil

class ConnectionLiveData(val context: Context) : SingleLiveEvent<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityManagerCallback()
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
        }

        // Set ConnectionLiveData's value by false on resume state
        // To trigger onAvailable callback again in case Put app to background -> Turn off wifi -> Open app again -> Turn on wifi
        if (!NetworkUtil.isConnectedToInternet(context)) postValue(false)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager.registerNetworkCallback(
            builder.build(),
            getConnectivityManagerCallback()
        )
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val netInfo = connectivityManager.getNetworkInfo(network)
                if (netInfo != null && netInfo.isConnected && netInfo.isAvailable) {
                    if (value != true)
                        postValue(true)
                }
                Log.d("NHINTY", "onAvailable: ")
            }

            override fun onLost(network: Network) {
                if (value != false)
                    postValue(false)
                Log.d("NHINTY", "onLost: ")
            }
        }
        return connectivityManagerCallback
    }
}