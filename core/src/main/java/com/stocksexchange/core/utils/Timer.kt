package com.stocksexchange.core.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.SystemClock

/**
 * A timer used for scheduling a countdown until a time in the future,
 * with regular notifications on intervals along the away.
 */
abstract class Timer(millisInFuture: Long, tickInterval: Long) {


    companion object {

        private const val MSG = 1

    }


    private var mIsCancelled: Boolean = false

    private var mMinimumFinishTime: Long = 0L

    private var mMillisInFuture: Long = millisInFuture
    private var mTickInterval: Long = tickInterval

    private var mStopTimeInFuture: Long = 0L

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {

        override fun handleMessage(msg: Message?) {
            synchronized(this@Timer) {
                if(mIsCancelled) {
                    return
                }

                val millisLeft: Long = (mStopTimeInFuture - SystemClock.elapsedRealtime())

                if(millisLeft <= mMinimumFinishTime) {
                    onFinished()
                } else {
                    val tickStart: Long = SystemClock.elapsedRealtime()

                    onTick(millisLeft)

                    // Taking into account user's onTick method's execution time
                    val tickDuration: Long = (tickStart - SystemClock.elapsedRealtime())
                    var delay: Long

                    if(millisLeft < mTickInterval) {
                        // Just delay until done
                        delay = (millisLeft - tickDuration)

                        // Special case: user's onTick took more than tick interval
                        // to complete, so triggering onFinish without the delay
                        if(delay < 0L) {
                            delay = 0L
                        }
                    } else {
                        delay = (mTickInterval - tickDuration)

                        // Special case: user's onTick took more than tick interval
                        // to complete, so skipping to the next interval
                        while(delay < 0L) {
                            delay += mTickInterval
                        }
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }

    }




    @Synchronized
    fun start() {
        mIsCancelled = false

        if(mMillisInFuture <= mMinimumFinishTime) {
            onFinished()
            return
        }

        mStopTimeInFuture = (SystemClock.elapsedRealtime() + mMillisInFuture)

        mHandler.sendMessage(mHandler.obtainMessage(MSG))

        onStarted()
    }


    @Synchronized
    fun cancel() {
        mIsCancelled = true

        mHandler.removeMessages(MSG)

        onCancelled()
    }


    /**
     * Sets a minimum finish time, i.e. if time left is lesser
     * than this value, then the timer finishes. By default the value
     * is 0.
     *
     * @param minimumFinishTime The minimum finish time
     */
    fun setMinimumFinishTime(minimumFinishTime: Long) {
        mMinimumFinishTime = minimumFinishTime
    }


    /**
     * Gets called whenever the timer has been started.
     */
    open fun onStarted() {
        // Left for subclass implementations
    }


    /**
     * Gets called if the [cancel] has been called when the timer was active.
     */
    open fun onCancelled() {
        // Left for subclass implementations
    }


    /**
     * Gets called every [mTickInterval].
     *
     * @param millisUntilFinished The amount of time until finished
     */
    open fun onTick(millisUntilFinished: Long) {
        // Left for subclass implementations
    }


    /**
     * Gets called when the time has been finished.
     */
    abstract fun onFinished()


}