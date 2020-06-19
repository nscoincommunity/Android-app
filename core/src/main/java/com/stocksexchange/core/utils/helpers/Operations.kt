package com.stocksexchange.core.utils.helpers

import com.stocksexchange.core.model.Result
import kotlinx.coroutines.supervisorScope
import timber.log.Timber

/**
 * Executes code inside block function on a background thread and provides
 * hooks to get notified when the operation has succeeded or when the
 * operation has failed.
 *
 * @param onSuccess The listener to get notified on success
 * @param onError The listener to get notified on failure
 * @param block The block of code to run on the background thread
 */
suspend fun executeBackgroundOperation(onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null,
                                       block: (() -> Unit)) = supervisorScope {
    try {
        createBgAsyncCoroutine(block = block).await()

        onSuccess?.invoke()
    } catch(exception: Throwable) {
        Timber.e(exception, "An error occurred while executing background operation. Error: $exception")

        onError?.invoke(exception)
    }
}


/**
 * Runs code inside the block function on a background thread and returns
 * an instance of [Result] class.
 *
 * @param block The block of code to run which returns a result of type [Result]
 *
 * @return The instance of [Result] class returned from the block function
 * or [Result.Failure] if an error has occurred
 */
suspend fun <T> performBackgroundOperation(block: (() -> Result<T>)): Result<T> = supervisorScope {
    try {
        createBgAsyncCoroutine(block = block).await()
    } catch(exception: Throwable) {
        Timber.e(exception, "An error occurred while doing background operation. Error: $exception")

        Result.Failure(exception)
    }
}