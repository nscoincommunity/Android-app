package com.stocksexchange.core.handlers

import com.stocksexchange.core.utils.helpers.createBgLaunchCoroutine
import com.stocksexchange.core.utils.helpers.createUiLaunchCoroutine
import kotlinx.coroutines.*

class CoroutineHandler {


    private val mScope = CoroutineScope(SupervisorJob())




    fun createUiLaunchCoroutine(
        startOption: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend (() -> Unit)
    ) : Job {
        return mScope.createUiLaunchCoroutine(startOption) { block() }
    }


    fun createBgLaunchCoroutine(
        startOption: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend (() -> Unit)
    ) : Job {
        return mScope.createBgLaunchCoroutine(startOption) { block() }
    }


    /**
     * Cancels all currently running children coroutines.
     *
     * Calling mScope.cancel() was not used because after cancelling, no
     * new coroutines can be started with the scope the cancellation
     * method was invoked on. Therefore, cancellation is directly invoked
     * on the context of the scope, which allows us to start new coroutines
     * after cancellation has finished.
     */
    fun cancelChildren() {
        mScope.coroutineContext.cancelChildren()
    }


}