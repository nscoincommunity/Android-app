package com.stocksexchange.android.receivers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver
import com.stocksexchange.core.utils.extensions.isNetworkAvailable

/**
 * A receiver used for observing and notifying about
 * network state changes.
 */
class NetworkStateReceiver(
    context: Context,
    private val listener: Listener
) : BaseBroadcastReceiver() {


    private var isConnected: Boolean = context.isNetworkAvailable()




    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isNetworkAvailable = context.isNetworkAvailable()

            if(isConnected == isNetworkAvailable) {
                return
            }

            if(isNetworkAvailable) {
                if(!isConnected) {
                    isConnected = true
                    listener.onConnected()
                }
            } else {
                if(isConnected) {
                    isConnected = false
                    listener.onDisconnected()
                }
            }
        }
    }


    interface Listener {

        fun onConnected() {
            // Stub
        }

        fun onDisconnected() {
            // Stub
        }

    }


}