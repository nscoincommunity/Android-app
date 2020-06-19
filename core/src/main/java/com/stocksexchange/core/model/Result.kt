package com.stocksexchange.core.model

@Suppress("unused")
sealed class Result<out T> {


    class Success<out T>(
        val value: T
    ) : Result<T>() {

        override fun toString(): String {
            return value.toString()
        }

    }


    class Failure(
        val exception: Throwable
    ) : Result<Nothing>() {

        override fun toString(): String {
            return exception.toString()
        }

    }


}