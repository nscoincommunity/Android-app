package com.stocksexchange.api.model.rest

import java.io.Serializable

interface WalletFilter : Serializable {


    companion object {

        /**
         * A filter that accepts any wallet passed to it.
         */
        val ANY_WALLET = object : WalletFilter {

            override fun isAcceptable(wallet: Wallet): Boolean = true

        }

        /**
         * A filter that allows only non-empty wallets.
         */
        val NON_EMPTY_WALLET = object : WalletFilter {

            override fun isAcceptable(wallet: Wallet): Boolean = !wallet.isEmpty

        }

        /**
         * A filter that allows only delisted wallets.
         */
        val DELISTED_WALLET = object : WalletFilter {

            override fun isAcceptable(wallet: Wallet): Boolean = wallet.isDelisted

        }

    }


    /**
     * A method to implement to determine whether the wallet
     * is acceptable by the filter in question.
     *
     * @param wallet The wallet to filter
     *
     * @return true if acceptable; false otherwise
     */
    fun isAcceptable(wallet: Wallet): Boolean


}