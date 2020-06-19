package com.stocksexchange.core.managers

import android.app.Activity
import android.app.Application
import android.os.Handler
import com.stocksexchange.core.utils.listeners.adapters.ActivityLifecycleCallbacksAdapter
import timber.log.Timber

/**
 * A manager responsible for observing the status of the application and for notifying
 * whether the application is in foreground or background.
 */
class BackgroundManager private constructor(
    application: Application
) : ActivityLifecycleCallbacksAdapter {


    companion object {

        @Volatile
        private var INSTANCE : BackgroundManager? = null

        private const val BACKGROUND_TRANSITION_DELAY = 3000L


        fun getInstance(application: Application): BackgroundManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildBackgroundManager(application).also { INSTANCE = it }
            }
        }


        private fun buildBackgroundManager(application: Application): BackgroundManager {
            return BackgroundManager(application)
        }

    }


    private var mIsInBackground: Boolean = false

    private var mBackgroundTransitionRunnable: Runnable? = null

    private val mBackgroundDelayHandler: Handler = Handler()

    private val mListeners: MutableList<Listener> = mutableListOf()




    init {
        application.registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityResumed(activity: Activity) {
        if(mBackgroundTransitionRunnable != null) {
            mBackgroundDelayHandler.removeCallbacks(mBackgroundTransitionRunnable)
            mBackgroundTransitionRunnable = null
        }

        if(mIsInBackground) {
            mIsInBackground = false
            notifyOnBecameForeground()
        }
    }


    override fun onActivityPaused(activity: Activity) {
        if(!mIsInBackground && (mBackgroundTransitionRunnable == null)) {
            mBackgroundTransitionRunnable = Runnable {
                mIsInBackground = true
                mBackgroundTransitionRunnable = null

                notifyOnBecameBackground()
            }
            mBackgroundDelayHandler.postDelayed(mBackgroundTransitionRunnable, BACKGROUND_TRANSITION_DELAY)
        }
    }


    private fun notifyOnBecameForeground() {
        Timber.i("Application went to foreground!")

        for(listener in mListeners) {
            listener.onBecameForeground()
        }
    }


    private fun notifyOnBecameBackground() {
        Timber.i("Application went to background!")

        for(listener in mListeners) {
            listener.onBecameBackground()
        }
    }


    /**
     * Registers a listener to invoke on foreground/background change.
     *
     * @param listener The listener to register
     */
    fun registerListener(listener: Listener) {
        mListeners.add(listener)
    }


    /**
     * Unregisters a listener to stop getting notified about foreground/background change.
     *
     * @param listener The listener to unregister
     */
    fun unregisterListener(listener: Listener) {
        mListeners.remove(listener)
    }


    /**
     * An interface for notifying about foreground/background changes.
     */
    interface Listener {

        fun onBecameForeground() {
            // Stub
        }

        fun onBecameBackground() {
            // Stub
        }

    }


}