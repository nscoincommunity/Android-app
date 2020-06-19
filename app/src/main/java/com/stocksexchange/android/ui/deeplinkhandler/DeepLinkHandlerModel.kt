package com.stocksexchange.android.ui.deeplinkhandler

import com.stocksexchange.android.Constants.STEX_ACCOUNT_VERIFICATION_URL_PREFIX
import com.stocksexchange.android.Constants.STEX_ACCOUNT_VERIFICATION_URL_TOKEN_PRECURSOR
import com.stocksexchange.android.Constants.STEX_MARKET_PREVIEW_ADVANCED_TRADE_URL_PATH_SEGMENT
import com.stocksexchange.android.Constants.STEX_MARKET_PREVIEW_BASIC_TRADE_URL_PATH_SEGMENT
import com.stocksexchange.android.Constants.STEX_MARKET_PREVIEW_URL_SYMBOLS_PRECURSOR
import com.stocksexchange.android.Constants.STEX_PASSWORD_RESET_URL_PREFIX
import com.stocksexchange.android.Constants.STEX_PASSWORD_RESET_URL_TOKEN_PRECURSOR
import com.stocksexchange.android.Constants.STEX_WITHDRAWAL_CANCELLATION_ID_PRECURSOR
import com.stocksexchange.android.Constants.STEX_WITHDRAWAL_CANCELLATION_URL_PREFIX
import com.stocksexchange.android.Constants.STEX_WITHDRAWAL_CONFIRMATION_DATA_PRECURSOR
import com.stocksexchange.android.Constants.STEX_WITHDRAWAL_CONFIRMATION_URL_PREFIX
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepository
import com.stocksexchange.android.model.DeepLinkData
import com.stocksexchange.android.model.DeepLinkType
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.deeplinkhandler.DeepLinkHandlerModel.ActionListener

class DeepLinkHandlerModel(
    private val userAdmissionRepository: UserAdmissionRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository,
    private val withdrawalsRepository: WithdrawalsRepository
) : BaseModel<ActionListener>() {


    companion object {

        private val URI_SEGMENT_DELIMITERS = charArrayOf('/', '?', '=', '&')

        const val REQUEST_TYPE_ACCOUNT_VERIFICATION = 0
        const val REQUEST_TYPE_CURRENCY_MARKET_RETRIEVAL = 1
        const val REQUEST_TYPE_WITHDRAWAL_CONFIRMATION = 2
        const val REQUEST_TYPE_WITHDRAWAL_CANCELLATION = 3

    }




    fun getLinkDataForUri(uri: String): DeepLinkData {
        if(uri.isBlank()) {
            return DeepLinkData(DeepLinkType.INVALID)
        }

        return when {
            uri.contains(STEX_ACCOUNT_VERIFICATION_URL_PREFIX) -> {
                getLinkDataForAccountVerificationUri(uri)
            }

            uri.contains(STEX_PASSWORD_RESET_URL_PREFIX) -> {
                getLinkDataForPasswordResetUri(uri)
            }

            uri.contains(STEX_MARKET_PREVIEW_BASIC_TRADE_URL_PATH_SEGMENT) ||
            uri.contains(STEX_MARKET_PREVIEW_ADVANCED_TRADE_URL_PATH_SEGMENT) -> {
                getLinkDataForMarketPreviewUri(uri)
            }

            uri.contains(STEX_WITHDRAWAL_CONFIRMATION_URL_PREFIX) -> {
                getLinkDataForWithdrawalConfirmationUri(uri)
            }

            uri.contains(STEX_WITHDRAWAL_CANCELLATION_URL_PREFIX) -> {
                getLinkDataForWithdrawalCancellationUri(uri)
            }

            else -> DeepLinkData(DeepLinkType.INVALID)
        }
    }


    private fun getLinkDataForAccountVerificationUri(uri: String): DeepLinkData {
        val uriSegments = uri.split(*URI_SEGMENT_DELIMITERS)
        var verificationToken = ""
        var precursorFound = false

        for(uriSegment in uriSegments) {
            if(uriSegment == STEX_ACCOUNT_VERIFICATION_URL_TOKEN_PRECURSOR) {
                precursorFound = true
            } else if(precursorFound) {
                if(verificationToken.isBlank()) {
                    verificationToken = uriSegment

                    break
                }
            }
        }

        return if(verificationToken.isNotBlank()) {
            DeepLinkData(DeepLinkType.ACCOUNT_VERIFICATION, verificationToken)
        } else {
            DeepLinkData(DeepLinkType.INVALID)
        }
    }


    private fun getLinkDataForPasswordResetUri(uri: String): DeepLinkData {
        val uriSegments = uri.split(*URI_SEGMENT_DELIMITERS)
        var passwordResetToken = ""
        var precursorFound = false

        for(uriSegment in uriSegments) {
            if(uriSegment == STEX_PASSWORD_RESET_URL_TOKEN_PRECURSOR) {
                precursorFound = true
            } else if(precursorFound) {
                if(passwordResetToken.isBlank()) {
                    passwordResetToken = uriSegment

                    break
                }
            }
        }

        return if(passwordResetToken.isNotBlank()) {
            DeepLinkData(DeepLinkType.PASSWORD_RESET, passwordResetToken)
        } else {
            DeepLinkData(DeepLinkType.INVALID)
        }
    }


    private fun getLinkDataForMarketPreviewUri(uri: String): DeepLinkData {
        val uriSegments = uri.split(*URI_SEGMENT_DELIMITERS)
        var baseCurrencySymbol = ""
        var quoteCurrencySymbol = ""
        var precursorFound = false

        for(uriSegment in uriSegments) {
            if(uriSegment == STEX_MARKET_PREVIEW_URL_SYMBOLS_PRECURSOR) {
                precursorFound = true
            } else if(precursorFound) {
                if(quoteCurrencySymbol.isBlank()) {
                    quoteCurrencySymbol = uriSegment
                } else {
                    baseCurrencySymbol = uriSegment

                    break
                }
            }
        }

        return if(baseCurrencySymbol.isNotBlank() && quoteCurrencySymbol.isNotBlank()) {
            DeepLinkData(DeepLinkType.MARKET_PREVIEW, Pair(baseCurrencySymbol, quoteCurrencySymbol))
        } else {
            DeepLinkData(DeepLinkType.INVALID)
        }
    }


    private fun getLinkDataForWithdrawalConfirmationUri(uri: String): DeepLinkData {
        val uriSegments = uri.split(*URI_SEGMENT_DELIMITERS)
        var withdrawalId = ""
        var confirmationToken = ""
        var precursorFound = false

        for(uriSegment in uriSegments) {
            if(uriSegment == STEX_WITHDRAWAL_CONFIRMATION_DATA_PRECURSOR) {
                precursorFound = true
            } else if(precursorFound) {
                if(withdrawalId.isBlank()) {
                    withdrawalId = uriSegment
                } else {
                    confirmationToken = uriSegment

                    break
                }
            }
        }

        return if(withdrawalId.isNotBlank() && (withdrawalId.toLongOrNull() != null) && confirmationToken.isNotBlank()) {
            DeepLinkData(DeepLinkType.WITHDRAWAL_CONFIRMATION, Pair(withdrawalId.toLong(), confirmationToken))
        } else {
            DeepLinkData(DeepLinkType.INVALID)
        }
    }


    private fun getLinkDataForWithdrawalCancellationUri(uri: String): DeepLinkData {
        val uriSegments = uri.split(*URI_SEGMENT_DELIMITERS)
        var withdrawalId = ""
        var precursorFound = false

        for(uriSegment in uriSegments) {
            if(uriSegment == STEX_WITHDRAWAL_CANCELLATION_ID_PRECURSOR) {
                precursorFound = true
            } else if(precursorFound) {
                if(withdrawalId.isBlank()) {
                    withdrawalId = uriSegment

                    break
                }
            }
        }

        return if(withdrawalId.isNotBlank() && (withdrawalId.toLongOrNull() != null)) {
            DeepLinkData(DeepLinkType.WITHDRAWAL_CANCELLATION, withdrawalId.toLong())
        } else {
            DeepLinkData(DeepLinkType.INVALID)
        }
    }


    fun performAccountVerificationRequest(verificationToken: String) {
        performRequest(
            requestType = REQUEST_TYPE_ACCOUNT_VERIFICATION,
            params = verificationToken
        )
    }


    fun performCurrencyMarketRetrievalRequest(pair: Pair<String, String>) {
        performRequest(
            requestType = REQUEST_TYPE_CURRENCY_MARKET_RETRIEVAL,
            params = pair
        )
    }


    fun performWithdrawalConfirmationRequest(params: WithdrawalConfirmationParameters) {
        performRequest(
            requestType = REQUEST_TYPE_WITHDRAWAL_CONFIRMATION,
            params = params
        )
    }


    fun performWithdrawalCancellationRequest(withdrawalId: Long) {
        performRequest(
            requestType = REQUEST_TYPE_WITHDRAWAL_CANCELLATION,
            params = withdrawalId
        )
    }


    @Suppress("UNCHECKED_CAST")
    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_ACCOUNT_VERIFICATION -> {
                userAdmissionRepository.verifyAccount(params as String).apply {
                    log("userAdmissionRepository.verifyAccount(params: $params)")
                }
            }

            REQUEST_TYPE_CURRENCY_MARKET_RETRIEVAL -> {
                val symbolsPair = (params as Pair<String, String>)
                val baseSymbol = symbolsPair.first
                val quoteSymbol = symbolsPair.second

                currencyMarketsRepository.getCurrencyMarket(baseSymbol, quoteSymbol).apply {
                    log("currencyMarketsRepository.getCurrencyMarket(base: $baseSymbol, quote: $quoteSymbol)")
                }
            }

            REQUEST_TYPE_WITHDRAWAL_CONFIRMATION -> {
                withdrawalsRepository.confirm(params as WithdrawalConfirmationParameters).apply {
                    log("withdrawalsRepository.confirm(params: $params)")
                }
            }

            REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                withdrawalsRepository.cancel(params as Long).apply {
                    log("withdrawalsRepository.cancel(params: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    interface ActionListener : BaseActionListener


}