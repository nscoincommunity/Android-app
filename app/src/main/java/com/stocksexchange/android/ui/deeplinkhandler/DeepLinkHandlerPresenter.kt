package com.stocksexchange.android.ui.deeplinkhandler

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.UserAdmissionError
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.android.model.DeepLinkData
import com.stocksexchange.android.model.DeepLinkType
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.api.exceptions.rest.AccountVerificationException
import com.stocksexchange.api.exceptions.rest.WithdrawalProcessingException

class DeepLinkHandlerPresenter(
    view: DeepLinkHandlerContract.View,
    model: DeepLinkHandlerModel
) : BasePresenter<DeepLinkHandlerContract.View, DeepLinkHandlerModel>(view, model), DeepLinkHandlerContract.ActionListener,
    DeepLinkHandlerModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    override fun onUriRetrieved(uri: String) {
        val deepLinkData = mModel.getLinkDataForUri(uri)

        when(deepLinkData.deepLinkType) {
            DeepLinkType.INVALID -> onInvalidDeepLinkTypeReceived()
            DeepLinkType.ACCOUNT_VERIFICATION -> onAccountVerificationDeepLinkTypeReceived(deepLinkData)
            DeepLinkType.PASSWORD_RESET -> onPasswordResetDeepLinkTypeReceived(deepLinkData)
            DeepLinkType.MARKET_PREVIEW -> onMarketPreviewDeepLinkTypeReceived(deepLinkData)
            DeepLinkType.WITHDRAWAL_CONFIRMATION -> onWithdrawalConfirmationDeepLinkTypeReceived(deepLinkData)
            DeepLinkType.WITHDRAWAL_CANCELLATION -> onWithdrawalCancellationDeepLinkTypeReceived(deepLinkData)
        }
    }


    private fun onInvalidDeepLinkTypeReceived() {
        finishWithError(mStringProvider.getString(R.string.error_invalid_deep_link))
    }


    private fun onAccountVerificationDeepLinkTypeReceived(deepLinkData: DeepLinkData) {
        val verificationToken = (deepLinkData.data as String)

        mModel.performAccountVerificationRequest(verificationToken)
    }


    private fun onPasswordResetDeepLinkTypeReceived(deepLinkData: DeepLinkData) {
        mView.launchPasswordRecoveryActivity((deepLinkData.data as String))
    }


    @Suppress("UNCHECKED_CAST")
    private fun onMarketPreviewDeepLinkTypeReceived(deepLinkData: DeepLinkData) {
        val symbolsPair = (deepLinkData.data as Pair<String, String>)

        mModel.performCurrencyMarketRetrievalRequest(symbolsPair)
    }


    @Suppress("UNCHECKED_CAST")
    private fun onWithdrawalConfirmationDeepLinkTypeReceived(deepLinkData: DeepLinkData) {
        val data: Pair<Long, String> = (deepLinkData.data as Pair<Long, String>)
        val params = WithdrawalConfirmationParameters(data.first, data.second)

        mModel.performWithdrawalConfirmationRequest(params)
    }


    private fun onWithdrawalCancellationDeepLinkTypeReceived(deepLinkData: DeepLinkData) {
        val withdrawalId = (deepLinkData.data as Long)

        mModel.performWithdrawalCancellationRequest(withdrawalId)
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            DeepLinkHandlerModel.REQUEST_TYPE_ACCOUNT_VERIFICATION -> {
                onAccountVerificationRequestSucceeded()
            }

            DeepLinkHandlerModel.REQUEST_TYPE_CURRENCY_MARKET_RETRIEVAL -> {
                onCurrencyMarketRetrievalRequestSucceeded(response as CurrencyMarket)
            }

            DeepLinkHandlerModel.REQUEST_TYPE_WITHDRAWAL_CONFIRMATION -> {
                onWithdrawalConfirmationRequestSucceeded()
            }

            DeepLinkHandlerModel.REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                onWithdrawalCancellationRequestSucceeded()
            }

        }
    }


    private fun onAccountVerificationRequestSucceeded() {
        mView.launchLoginActivity()
        mView.finishActivity()
    }


    private fun onCurrencyMarketRetrievalRequestSucceeded(currencyMarket: CurrencyMarket) {
        mView.navigateToCurrencyMarketPreviewScreen(currencyMarket)
        mView.finishActivity()
    }


    private fun onWithdrawalConfirmationRequestSucceeded() {
        mView.navigateToWithdrawalsScreen(wasWithdrawalJustConfirmed = true)
        mView.finishActivity()
    }


    private fun onWithdrawalCancellationRequestSucceeded() {
        mView.navigateToWithdrawalsScreen(wasWithdrawalJustCancelled = true)
        mView.finishActivity()
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            DeepLinkHandlerModel.REQUEST_TYPE_ACCOUNT_VERIFICATION -> {
                onAccountVerificationFailed(exception)
            }

            DeepLinkHandlerModel.REQUEST_TYPE_CURRENCY_MARKET_RETRIEVAL -> {
                onCurrencyMarketRetrievalFailed(exception)
            }

            DeepLinkHandlerModel.REQUEST_TYPE_WITHDRAWAL_CONFIRMATION -> {
                onWithdrawalConfirmationFailed(exception)
            }

            DeepLinkHandlerModel.REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                onWithdrawalCancellationFailed(exception)
            }

        }
    }


    private fun onAccountVerificationFailed(exception: Throwable) {
        finishWithError(when(exception) {
            is AccountVerificationException -> when(exception.error) {
                AccountVerificationException.Error.MULTIPLE -> {
                    val firstErrorMessage = exception.errorMessages.first()
                    val error = UserAdmissionError.newAccountVerificationError(firstErrorMessage)

                    if(error == UserAdmissionError.UNKNOWN) {
                        firstErrorMessage
                    } else {
                        mStringProvider.getString(error.stringId)
                    }
                }

                AccountVerificationException.Error.USER_NOT_FOUND,
                AccountVerificationException.Error.UNKNOWN -> {
                    exception.message
                }

            }

            else -> mStringProvider.getErrorMessage(exception)
        })
    }


    private fun onCurrencyMarketRetrievalFailed(exception: Throwable) {
        finishWithError(mStringProvider.getErrorMessage(exception))
    }


    private fun onWithdrawalConfirmationFailed(exception: Throwable) {
        onWithdrawalOperationFailed(exception)
    }


    private fun onWithdrawalCancellationFailed(exception: Throwable) {
        onWithdrawalOperationFailed(exception)
    }


    private fun onWithdrawalOperationFailed(exception: Throwable) {
        finishWithError(when(exception) {
            is WithdrawalProcessingException -> when(exception.error) {
                WithdrawalProcessingException.Error.PROCESSING -> {
                    mStringProvider.getString(R.string.error_withdrawal_deep_link_error)
                }

                WithdrawalProcessingException.Error.UNKNOWN -> exception.message

                else -> mStringProvider.getErrorMessage(exception)
            }

            else -> mStringProvider.getErrorMessage(exception)
        })
    }


    private fun finishWithError(errorMessage: String) {
        mView.finishActivityWithError(errorMessage)
    }


}