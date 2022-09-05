package com.zest.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import java.util.*

class NetworkStateReceiver : BroadcastReceiver() {

    private var listeners: MutableSet<OnNetworkStateReceiverListener> = HashSet()
    private var connected: Boolean? = null

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        connected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        for (listener in listeners)
            notifyState(listener)
    }

    private fun notifyState(listener: OnNetworkStateReceiverListener?) {
        if (connected == null || listener == null) return
        if (connected == true) listener.networkAvailable()
        else listener.networkUnavailable()
    }

    fun addListener(listener: OnNetworkStateReceiverListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: OnNetworkStateReceiverListener) {
        listeners.remove(listener)
    }

    interface OnNetworkStateReceiverListener {
        fun networkAvailable()
        fun networkUnavailable()
    }
}
