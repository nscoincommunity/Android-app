package com.stocksexchange.android.model

import com.stocksexchange.core.model.Result

data class RepositoryResult<T>(
    var cacheResult: Result<T>? = null,
    var serverResult: Result<T>? = null,
    var databaseResult: Result<T>? = null
) {


    companion object {


        /**
         * Creates a new successful repository result by figuring out the type of the
         * previous successful repository result and assigning a new successful value
         * to the previously found result type.
         *
         * @param successfulResult The previous successful repository result to use to
         * figure out the type of the result itself (cache, server, or database)
         * @param successfulValue The new value to assign to a previously found result
         * type (cache, server, or database)
         *
         * @return The new successful repository result
         */
        fun <In, Out> newSuccessfulInstance(
            successfulResult: RepositoryResult<In>,
            successfulValue: Out
        ): RepositoryResult<Out> {
            return newSuccessfulInstance(
                potentialSuccessfulResults = listOf(successfulResult),
                successfulValue = successfulValue
            )
        }


        /**
         * Creates a new successful repository result by figuring out the type of the
         * previous successful repository result and assigning a new successful value
         * to the previously found result type.
         *
         * @param potentialSuccessfulResults The previous potential successful repository
         * results to traverse through to find the first successful one and use it
         * to figure out the type of the result itself (cache, server, or database)
         * @param successfulValue The new value to assign to a previously found result
         * type (cache, server, or database)
         *
         * @return The new successful repository result
         */
        fun <Out> newSuccessfulInstance(
            potentialSuccessfulResults: List<RepositoryResult<*>>,
            successfulValue: Out
        ) : RepositoryResult<Out> {
            val successfulRepoResult = potentialSuccessfulResults.first { it.isSuccessful() }
            val successfulValueResult = Result.Success(successfulValue)

            return when {
                successfulRepoResult.isCacheResultSuccessful() -> RepositoryResult(cacheResult = successfulValueResult)
                successfulRepoResult.isDatabaseResultSuccessful() -> RepositoryResult(databaseResult = successfulValueResult)
                successfulRepoResult.isServerResultSuccessful() -> RepositoryResult(serverResult = successfulValueResult)

                else -> throw IllegalStateException()
            }
        }


        /**
         * Creates a new erroneous repository result by figuring out the type of the
         * previous erroneous repository result and assigning the erroneous value
         * to the previously found result type.
         *
         * @param erroneousResult The previous erroneous repository result to use to
         * figure out the type of the result itself (cache, server, or database)
         *
         * @return The new erroneous repository result
         */
        fun <Out> newErroneousInstance(
            erroneousResult: RepositoryResult<*>
        ): RepositoryResult<Out> {
            return newErroneousInstance(potentialErroneousResults = listOf(erroneousResult))
        }


        /**
         * Creates a new erroneous repository result by figuring out the type of the
         * previous erroneous repository result and assigning the erroneous value
         * to the previously found result type.
         *
         * @param potentialErroneousResults The previous potential erroneous repository
         * results to traverse through to find the first erroneous one and use it
         * to figure out the type of the result itself (cache, server, or database)
         *
         * @return The new erroneous repository result
         */
        fun <Out> newErroneousInstance(
            potentialErroneousResults: List<RepositoryResult<*>>
        ) : RepositoryResult<Out> {
            val erroneousResult = potentialErroneousResults.first { it.isErroneous() }
            val erroneousValue = erroneousResult.getErroneousResult()

            return when {
                erroneousResult.isCacheResultErroneous() -> RepositoryResult(cacheResult = erroneousValue)
                erroneousResult.isDatabaseResultErroneous() -> RepositoryResult(databaseResult = erroneousValue)
                erroneousResult.isServerResultErroneous() -> RepositoryResult(serverResult = erroneousValue)

                else -> throw IllegalStateException()
            }
        }


    }


    constructor(data: T): this(Result.Success(data))


    constructor(exception: Throwable): this(Result.Failure(exception))


    constructor(result: Result<T>): this(cacheResult = result)


    fun getSuccessfulResult(): Result.Success<T> {
        return when {
            isCacheResultSuccessful() -> (cacheResult as Result.Success<T>)
            isServerResultSuccessful() -> (serverResult as Result.Success<T>)
            else -> (databaseResult as Result.Success<T>)
        }
    }


    fun getSuccessfulResultValue(): T {
        return getSuccessfulResult().value
    }


    fun getErroneousResult(): Result.Failure {
        return when {
            isCacheResultErroneous() -> (cacheResult as Result.Failure)
            isServerResultErroneous() -> (serverResult as Result.Failure)
            else -> (databaseResult as Result.Failure)
        }
    }


    fun getErroneousResultValue(): Throwable {
        return getErroneousResult().exception
    }


    fun isCacheResultSuccessful(): Boolean {
        return (cacheResult is Result.Success)
    }


    fun isCacheResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (cacheResult == null)) || (cacheResult is Result.Failure))
    }


    fun isServerResultSuccessful(): Boolean {
        return (serverResult is Result.Success)
    }


    fun isServerResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (serverResult == null)) || (serverResult is Result.Failure))
    }


    fun isDatabaseResultSuccessful(): Boolean {
        return (databaseResult is Result.Success)
    }


    fun isDatabaseResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (databaseResult == null)) || (databaseResult is Result.Failure))
    }


    /**
     * If either cache, server or database result is successful,
     * then this method will return true. Otherwise, return false.
     *
     * @return true if at least one result is successful; false otherwise
     */
    fun isSuccessful(): Boolean {
        return (isCacheResultSuccessful() ||
                isServerResultSuccessful() ||
                isDatabaseResultSuccessful())
    }


    /**
     * If cache, server, and database results are erroneous,
     * then this method will return true. Otherwise, return false.
     *
     * @return true if all results are erroneous; false otherwise
     */
    fun isErroneous(): Boolean {
        return (isCacheResultErroneous(true) &&
                isServerResultErroneous(true) &&
                isDatabaseResultErroneous(true))
    }


}