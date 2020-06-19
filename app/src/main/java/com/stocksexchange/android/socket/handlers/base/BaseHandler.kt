package com.stocksexchange.android.socket.handlers.base

import com.google.gson.Gson
import com.stocksexchange.android.events.BaseEvent
import com.stocksexchange.core.utils.helpers.createBgLaunchGlobalCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import org.json.JSONObject
import timber.log.Timber

/**
 * A base handler class containing common functionality for all
 * handlers.
 */
@Suppress("LeakingThis")
abstract class BaseHandler<SocketData>(
    protected val gson: Gson
) : Handler, BaseEvent.OnConsumeListener {


    /**
     * The set containing asynchronous coroutine jobs to execute.
     */
    protected val mAsyncJobsSet: LinkedHashSet<Job> = linkedSetOf()




    /**
     * Performs asynchronous operation by packaging it inside a coroutine job.
     *
     * @param block The block of code to execute
     */
    protected fun performAsync(block: suspend CoroutineScope.() -> Unit) {
        val job = createBgLaunchGlobalCoroutine(
            startOption = CoroutineStart.LAZY,
            block = block
        )

        job.invokeOnCompletion {
            synchronized(mAsyncJobsSet) {
                mAsyncJobsSet.remove(job)

                if(mAsyncJobsSet.isNotEmpty()) {
                    mAsyncJobsSet.first().start()
                }
            }
        }

        synchronized(mAsyncJobsSet) {
            mAsyncJobsSet.add(job)

            if((mAsyncJobsSet.size - 1) == 0) {
                job.start()
            }
        }
    }


    final override fun onSocketDataReceived(rawSocketData: Array<out Any>) {
        // Fetching and converting the data
        val channel = (rawSocketData[0] as String)
        val socketData = convertSocketData(rawSocketData[1] as JSONObject)

        // Logging
        Timber.i("${getLoggingKey()}(channel: $channel, data: $socketData)")

        // Perform async operation
        performAsync {
            onSocketDataReceivedAsync(channel, socketData)
        }
    }


    /**
     * A callback that gets called from inside [onSocketDataReceived] that is
     * responsible for handling the received socket event.
     *
     * @param channel The channel of the event
     * @param socketData The socket data
     */
    protected abstract suspend fun onSocketDataReceivedAsync(channel: String, socketData: SocketData)


    override fun onConsumed() {
        // Stub
    }


    /**
     * Converts the socket data json object into a data object of this handler.
     *
     * @param socketDataJsonObj The json object to convert
     */
    protected abstract fun convertSocketData(socketDataJsonObj: JSONObject): SocketData


    override fun canHandleSocketEvent(socketEvent: String): Boolean {
        return (getSocketEvent().eventName == socketEvent)
    }


    protected abstract fun getLoggingKey(): String


}