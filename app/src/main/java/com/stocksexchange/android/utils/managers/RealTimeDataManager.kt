package com.stocksexchange.android.utils.managers

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.stocksexchange.android.events.RealTimeDataUpdateEvent
import com.stocksexchange.android.receivers.NetworkStateReceiver
import com.stocksexchange.core.managers.BackgroundManager
import com.stocksexchange.core.providers.ConnectionProvider
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A manager responsible for tracking and notifying system components
 * when real time data needs to be updated (e.g., when the network
 * has become available or the app has come to foreground from the
 * background state).
 */
class RealTimeDataManager constructor(
    application: Application
) : KoinComponent {


    private var mConsumerCount: Int = 0

    private val mConnectionProvider: ConnectionProvider by inject()




    init {
        initNetworkReceiver(application)
        initBackgroundManager(application)
    }


    private fun initNetworkReceiver(context: Context) {
        val networkListener = object : NetworkStateReceiver.Listener {

            override fun onConnected() {
                sendRealTimeDataUpdateEvent()
            }

        }

        context.registerReceiver(
            NetworkStateReceiver(context, networkListener),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    private fun initBackgroundManager(application: Application) {
        val backgroundManagerListener = object : BackgroundManager.Listener {

            override fun onBecameForeground() {
                if(mConnectionProvider.isNetworkAvailable()) {
                    sendRealTimeDataUpdateEvent()
                }
            }

        }

        BackgroundManager.getInstance(application).registerListener(backgroundManagerListener)
    }


    private fun sendRealTimeDataUpdateEvent() {
        EventBus.getDefault().postSticky(RealTimeDataUpdateEvent.newInstance(
            source = this,
            consumerCount = mConsumerCount
        ))
    }


    fun incrementDataUpdateEventConsumerCount() {
        mConsumerCount++
    }


    fun decrementDataUpdateEventConsumerCount() {
        if(mConsumerCount == 0) {
            return
        }

        mConsumerCount--
    }


}