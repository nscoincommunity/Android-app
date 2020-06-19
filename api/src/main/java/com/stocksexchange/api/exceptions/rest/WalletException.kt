package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class WalletException(
    val error: Error,
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable) {


    companion object {

        fun delistedCurrency(message: String = ""): WalletException {
            return WalletException(
                error = Error.DELISTED_CURRENCY,
                message = message
            )
        }


        fun disabledDeposits(message: String = ""): WalletException {
            return WalletException(
                error = Error.DISABLED_DEPOSITS,
                message = message
            )
        }


        fun walletCreationDelay(message: String = ""): WalletException {
            return WalletException(
                error = Error.WALLET_CREATION_DELAY,
                message = message
            )
        }


        fun walletNotFound(message: String = ""): WalletException {
            return WalletException(
                error = Error.WALLET_NOT_FOUND,
                message = message
            )
        }


        fun absentAddress(message: String = ""): WalletException {
            return WalletException(
                error = Error.ADDRESS_ABSENCE,
                message = message
            )
        }


        fun unknown(message: String): WalletException {
            return WalletException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }




    val isDelistedCurrencyError: Boolean
        get() = (error == Error.DELISTED_CURRENCY)


    val isDisabledDepositsError: Boolean
        get() = (error == Error.DISABLED_DEPOSITS)


    val isWalletCreationDelayError: Boolean
        get() = (error == Error.WALLET_CREATION_DELAY)


    val isWalletNotFoundError: Boolean
        get() = (error == Error.WALLET_NOT_FOUND)


    enum class Error {

        DELISTED_CURRENCY,
        DISABLED_DEPOSITS,
        WALLET_CREATION_DELAY,
        WALLET_NOT_FOUND,
        ADDRESS_ABSENCE,
        UNKNOWN

    }


}