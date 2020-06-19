package com.stocksexchange.core.utils.helpers

import kotlinx.coroutines.*

/**
 * Creates a launch coroutine on the main thread and immediately starts it.
 *
 * @param startOption The start option specifying how to start the coroutine
 * @param block The block of code to run inside the coroutine
 *
 * @return The coroutine job
 */
fun CoroutineScope.createUiLaunchCoroutine(
    startOption: CoroutineStart = CoroutineStart.DEFAULT,
    block: (suspend CoroutineScope.() -> Unit)
): Job {
    return launch(context = Dispatchers.Main, start = startOption, block = block)
}


/**
 * Creates a launch coroutine on the background thread and immediately starts it.
 *
 * @param startOption The start option specifying how to start the coroutine
 * @param block The block of code to run inside the coroutine
 *
 * @return The coroutine job
 */
fun CoroutineScope.createBgLaunchCoroutine(
    startOption: CoroutineStart = CoroutineStart.DEFAULT,
    block: (suspend CoroutineScope.() -> Unit)
): Job {
    return launch(context = Dispatchers.IO, start = startOption, block = block)
}


/**
 * Creates an async coroutine on the background thread.
 *
 * @param startOption The start option specifying how to start the coroutine
 * @param block The block of code to run inside the coroutine
 *
 * @return The deferred job of the coroutine
 */
fun <T> CoroutineScope.createBgAsyncCoroutine(
    startOption: CoroutineStart = CoroutineStart.DEFAULT,
    block: (() -> T)
): Deferred<T> {
    val asyncBlock: suspend CoroutineScope.() -> T = {
        block()
    }

    return async(context = Dispatchers.IO, start = startOption, block = asyncBlock)
}


/**
 * Creates a global launch coroutine on the background thread.
 *
 * @param startOption The start option specifying how to start the coroutine
 * @param block The block of code to run inside the coroutine
 *
 * @return The coroutine job
 */
fun createBgLaunchGlobalCoroutine(
    startOption: CoroutineStart = CoroutineStart.DEFAULT,
    block: (suspend CoroutineScope.() -> Unit)
): Job {
    return GlobalScope.launch(context = Dispatchers.IO, start = startOption, block = block)
}