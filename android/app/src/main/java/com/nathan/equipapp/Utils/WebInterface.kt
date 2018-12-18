package com.nathan.equipapp.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log

class WebInterface {
    companion object {

        private val TAG = this.javaClass.simpleName
        fun isOnline(context: Context): Boolean {

            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    if (cm != null) {
                        val info = cm.allNetworkInfo
                        if (info != null)
                            for (i in info.indices)
                                if (info[i].state == NetworkInfo.State.CONNECTED) {
                                    Log.d(TAG, "is connected")
                                    return true
                                }
                    } else {
                        Log.d(TAG, "cannot get connectivity manager, responding with no internet connection")
                        return false
                    }
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (cm != null) {
                        val networks = cm.allNetworks
                        val thisNetworkInfo = cm.activeNetworkInfo
                        if (thisNetworkInfo != null) {
                            if (thisNetworkInfo.state == NetworkInfo.State.CONNECTED) {
                                return true
                            }
                        }
                        if (networks != null) {
                            for (network in networks) {
                                val info = cm.getNetworkInfo(network)
                                if (info.state == NetworkInfo.State.CONNECTED) {
                                    return true
                                }
                            }
                        }
                    } else {
                        return false
                    }
                }
            } catch (e: Exception) {
                Log.e("WebInterface ::", " isonline() " + e.toString())
                return false
            }

            return false

        }
    }


}