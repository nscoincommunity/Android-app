package com.stocksexchange.android.database.tables.base

import com.stocksexchange.core.model.Result
import com.stocksexchange.core.exceptions.NotFoundException

abstract class BaseTable {


    /**
     * Packages the database result inside the container class.
     *
     * @param convert The function that applies transformations to the input
     * to convert it to the output
     * @param precondition The optional condition used for checking
     * converted output
     *
     * @return The result or error packaged inside an instance of Result class
     */
    protected fun<In, Out> In?.toResult(
        convert: In.() -> Out,
        precondition: ((Out) -> Boolean) = { true }
    ): Result<Out> {
        return this?.convert()
            ?.takeIf { precondition(it) }
            ?.let { Result.Success(it) }
            ?: Result.Failure(getNotFoundException())
    }


    /**
     * Same as [toResult] but works with list data.
     */
    protected fun <In : List<*>, Out : List<*>> In.toResult(
        convert: (In.() -> Out)
    ): Result<Out> {
        return convert()
            .takeIf { it.isNotEmpty() }
            ?.let { Result.Success(it) }
            ?: Result.Failure(getNotFoundException())
    }


    /**
     * Same as [toResult] but works with list data and does
     * not have an output format, meaning its input format
     * is its output format.
     */
    protected fun <T : List<*>> T.toResult(): Result<T> {
        return toResult { this }
    }


    /**
     * Retrieves an exception to denote that the data could not
     * be found. Should be overridden to provide a custom exception.
     *
     * @return The exception
     */
    protected open fun getNotFoundException(): Exception {
        return NotFoundException()
    }


}