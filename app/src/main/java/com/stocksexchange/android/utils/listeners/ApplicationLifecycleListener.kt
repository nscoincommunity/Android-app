package com.stocksexchange.android.utils.listeners

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.stocksexchange.android.model.AppStateStatus
import com.stocksexchange.core.handlers.PreferenceHandler

class ApplicationLifecycleListener(val context: Context) : LifecycleObserver {


    private val preferenceHandler =  PreferenceHandler(context)




    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        preferenceHandler.setAppStateStatus(AppStateStatus.ON_START.title)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        preferenceHandler.setAppStateStatus(AppStateStatus.ON_STOP.title)
    }


}