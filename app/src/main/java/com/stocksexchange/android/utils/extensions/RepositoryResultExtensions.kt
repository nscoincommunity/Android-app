package com.stocksexchange.android.utils.extensions

import com.stocksexchange.android.model.RepositoryResult
import timber.log.Timber


/**
 * Logs the repository result.
 *
 * @param key The key of the log
 *
 * @return The repository result
 */
fun <T> RepositoryResult<T>.log(key: String): RepositoryResult<T> {
    Timber.i("$key = $this")

    return this
}


/**
 * Adds a predicate to the repository result.
 *
 * @param getException The callback to fetch exception in case the predicate fails
 * @param predicate The condition that must be satisfied in order for
 * the repository result to be considered successful
 *
 * @return The repository result
 */
fun <T> RepositoryResult<T>.onSuccessOnlyIf(
    getException: (() -> Throwable),
    predicate: ((T) -> Boolean)
): RepositoryResult<T> {
    return if(isSuccessful()) {
        if(predicate(getSuccessfulResultValue())) {
            this
        } else {
            RepositoryResult(getException())
        }
    } else {
        this
    }
}


/**
 * A callback to invoke if the repository result is successful.
 *
 * @param block The block of code to run on success
 *
 * @return The repository result
 */
suspend fun <T> RepositoryResult<T>.onSuccess(block: suspend (T) -> Unit): RepositoryResult<T> {
    if(isSuccessful()) {
        block(getSuccessfulResultValue())
    }

    return this
}


/**
 * A callback to invoke if the repository result is unsuccessful.
 *
 * @param block The block of code to run on failure
 *
 * @return The repository result
 */
suspend fun <T> RepositoryResult<T>.onFailure(block: suspend (Throwable) -> Unit): RepositoryResult<T> {
    if(isErroneous()) {
        block(getErroneousResultValue())
    }

    return this
}