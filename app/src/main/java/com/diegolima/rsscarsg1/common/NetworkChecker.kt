package com.diegolima.rsscarsg1.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkChecker(private val connectivityManager: ConnectivityManager?) {

    fun performActionIfConnected(action: () -> Unit){
        if(hasInternet()){
            action()
        }
    }

    fun hasInternet(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            val network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            if(activeNetworkInfo != null){
                return activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
                        || activeNetworkInfo.type == ConnectivityManager.TYPE_VPN
                        || activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
            false
        }
    }
}