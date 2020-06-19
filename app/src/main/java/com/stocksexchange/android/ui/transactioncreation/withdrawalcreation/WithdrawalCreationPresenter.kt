package com.stocksexchange.android.ui.transactioncreation.withdrawalcreation

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters
import com.stocksexchange.android.mappings.mapToCurrencyIdWalletMap
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationPresenter
import com.stocksexchange.api.exceptions.rest.WalletException
import com.stocksexchange.api.exceptions.rest.WithdrawalCreationException
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.replaceCommaSpaceWithPeriod
import com.stocksexchange.core.utils.helpers.composeInvalidDataDialogMessage

class WithdrawalCreationPresenter(
    view: WithdrawalCreationContract.View,
    model: WithdrawalCreationModel,
    private val numberFormatter: NumberFormatter
) : BaseTransactionCreationPresenter<WithdrawalCreationContract.View, WithdrawalCreationModel>(view, model),
    WithdrawalCreationContract.ActionListener, WithdrawalCreationModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    override fun onDataLoadingSucceeded(data: TransactionData) {
        transactionData = data

        super.onDataLoadingSucceeded(data)
    }


    override fun getEmptyViewCaption(params: TransactionCreationParameters): String {
        return mStringProvider.getTransactionCreationEmptyCaption()
    }


    override fun getErrorViewCaption(exception: Throwable): String {
        if (transactionData.wallet.currentBalance == 0.0) {
            return mStringProvider.getString(R.string.error_nothing_to_withdraw)
        } else {
            return when (exception) {
                is WalletException -> when (exception.error) {
                    WalletException.Error.DELISTED_CURRENCY,
                    WalletException.Error.DISABLED_DEPOSITS,
                    WalletException.Error.WALLET_NOT_FOUND -> {
                        // This exception is only returned as a response from the
                        // POST /profile/wallets/{currencyId} endpoint meaning that the user
                        // tries to create a wallet when the currency is delisted or the deposits
                        // are disabled.
                        mStringProvider.getString(R.string.error_nothing_to_withdraw)
                    }

                    WalletException.Error.ADDRESS_ABSENCE -> {
                        mStringProvider.getString(R.string.error_no_withdraw_address)
                    }

                    else -> super.getErrorViewCaption(exception)
                }

                else -> super.getErrorViewCaption(exception)
            }
        }
    }


    override fun onAddressInputViewIconClicked() {
        if(!mView.checkCameraPermission()) {
            return
        }

        mView.launchQrCodeScannerActivity()
    }


    override fun onQrCodeReceived(qrCode: String) {
        if(qrCode.isBlank()) {
            return
        }

        mView.setAddressInputViewText(qrCode)
    }


    override fun onAmountInputViewLabelClicked() {
        val wallet = transactionData.wallet

        if(wallet.isCurrentBalanceEmpty) {
            return
        }

        val currentBalance = wallet.currentBalance
        val text = numberFormatter.formatAmount(currentBalance)

        mView.setAmountInputViewText(text)
    }


    override fun onAmountInputViewTextEntered(amount: String) {
        setFinalAmountText(calculateFinalAmount(amount))
    }


    override fun onAmountInputViewTextRemoved() {
        setFinalAmountText(0.0)
    }


    private fun calculateFinalAmount(amount: String): Double {
        val parsedAmount = numberFormatter.parse(amount)

        return if(parsedAmount == 0.0) {
            parsedAmount
        } else {
            val data = transactionData
            val currencySymbol = data.currency.symbol
            val protocolId = getDataLoadingParams().protocolId
            val withdrawalFeeCurrencySymbol = data.getWithdrawalCurrencySymbol(protocolId)

            if(currencySymbol == withdrawalFeeCurrencySymbol) {
                (parsedAmount - data.getWithdrawalFee(protocolId))
            } else {
                parsedAmount
            }
        }
    }


    private fun setFinalAmountText(amount: Double) {
        val currencySymbol = transactionData.currency.symbol

        mView.setFinalAmountText("${numberFormatter.formatAmount(amount)} $currencySymbol")
    }


    override fun onWithdrawButtonClicked() {
        if(mView.isDataSourceEmpty()) {
            mView.showToast(mStringProvider.getString(R.string.error_no_data_available))
            return
        }

        val addressParamValue = mView.getInputViewValue(WithdrawalInputView.ADDRESS)
        val extraParamValue = mView.getInputViewValue(WithdrawalInputView.EXTRA)
        val amountParamValue = mView.getInputViewValue(WithdrawalInputView.AMOUNT).replaceCommaSpaceWithPeriod()

        val errorsList = mutableListOf<String>()
        val erroneousInputViews = mutableListOf<WithdrawalInputView>()

        val wallet = transactionData.wallet
        val currency = transactionData.currency

        val parsedAmount = numberFormatter.parse(amountParamValue, -1.0)

        val protocolId = getDataLoadingParams().protocolId
        val withdrawalLimit = transactionData.getWithdrawalLimit(protocolId)


        // Checking the amount parameter
        val isAmountInvalid = (amountParamValue.isBlank() || (parsedAmount == -1.0) || (parsedAmount <= 0))
        val notEnoughBalance = (parsedAmount > wallet.currentBalance)
        val isEnteredAmountLessThanMinAmount = (parsedAmount < currency.minimumWithdrawalAmount)
        var isEnteredAmountTooBigThanMaxAmount = false

        if (withdrawalLimit > 0) {
            isEnteredAmountTooBigThanMaxAmount = (parsedAmount > withdrawalLimit)
        }

        if(isAmountInvalid || notEnoughBalance || isEnteredAmountLessThanMinAmount || isEnteredAmountTooBigThanMaxAmount) {
            errorsList.add(when {
                isAmountInvalid -> mStringProvider.getString(R.string.error_invalid_amount)
                notEnoughBalance -> mStringProvider.getString(R.string.error_entered_amount_excess)
                isEnteredAmountLessThanMinAmount -> {
                    val minAmountStr = numberFormatter.formatAmount(currency.minimumWithdrawalAmount)

                    mStringProvider.getString(
                        R.string.error_min_withdrawal_amount,
                        "$minAmountStr ${currency.withdrawalFeeCurrencySymbol}"
                    )
                }
                isEnteredAmountTooBigThanMaxAmount -> {
                    val maxAmountStr = numberFormatter.formatAmount(withdrawalLimit)

                    mStringProvider.getString(
                        R.string.error_max_withdrawal_amount,
                        "$maxAmountStr ${currency.withdrawalFeeCurrencySymbol}"
                    )
                }

                else -> throw IllegalStateException()
            })

            erroneousInputViews.add(WithdrawalInputView.AMOUNT)
        }

        // Checking the address parameter
        if(addressParamValue.isBlank()) {
            errorsList.add(mStringProvider.getString(R.string.error_invalid_address))
            erroneousInputViews.add(WithdrawalInputView.ADDRESS)
        }

        // Checking the extra parameter
        if(wallet.hasAdditionalWithdrawalParameter) {
            val additionalWithdrawalParameterName = wallet.strippedAdditionalWithdrawalParameterName

            // Checking the extra param and its optionality
            if(extraParamValue.isBlank() && !wallet.isAdditionalWithdrawalParameterOptional) {
                errorsList.add(mStringProvider.getString(
                    R.string.error_invalid_withdrawal_extra_parameter_template,
                    additionalWithdrawalParameterName
                ))
                erroneousInputViews.add(WithdrawalInputView.EXTRA)
            }
        }

        // Checking if there are any errors
        if(errorsList.isNotEmpty()) {
            showInvalidDataDialog(errorsList)

            mView.setInputViewState(
                state = InputViewState.ERRONEOUS,
                inputViews = erroneousInputViews
            )

            return
        }

        // Sending the request
        sendWithdrawalCreationRequest(
            parsedAmount,
            addressParamValue,
            extraParamValue
        )
    }


    private fun showInvalidDataDialog(errorsList: List<String>) {
        if(errorsList.isEmpty()) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        val title = mStringProvider.getString(R.string.invalid_data_dialog_title)
        val message = composeInvalidDataDialogMessage(
            errorsList,
            mStringProvider.getString(R.string.invalid_data_dialog_footer_text)
        )

        showInfoDialog(title, message)
    }


    private fun sendWithdrawalCreationRequest(amount: Double, address: String, extraAddress: String) {
        val params = getDataLoadingParams()
        val amountStr = numberFormatter.toApiDataFormatter { formatAmount(amount) }

        mModel.performWithdrawalCreationRequest(WithdrawalCreationParameters(
            currencyId = params.currencyId,
            protocolId = params.protocolId,
            amount = amountStr,
            address = address,
            additionalAddress = extraAddress
        ))
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            WithdrawalCreationModel.REQUEST_TYPE_WITHDRAWAL_CREATION -> {
                onWithdrawalCreationRequestSucceeded()
            }

            WithdrawalCreationModel.REQUEST_TYPE_WALLETS_FETCHING -> {
                onWalletsFetchingRequestSucceeded(response as List<Wallet>)
            }

        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            WithdrawalCreationModel.REQUEST_TYPE_WITHDRAWAL_CREATION -> {
                onWithdrawalCreationRequestFailed(exception)
            }

            else -> super.onRequestFailed(requestType, exception, metadata)

        }
    }


    private fun onWithdrawalCreationRequestSucceeded() {
        val currency = transactionData.currency
        val protocolId = getDataLoadingParams().protocolId
        val withdrawalFeeCurrencyId = transactionData.getWithdrawalFeeCurrencyId(protocolId)
        val currencyIds = mutableListOf<Int>().apply {
            add(currency.id)

            if(currency.id != withdrawalFeeCurrencyId) {
                add(withdrawalFeeCurrencyId)
            }
        }

        mModel.performWalletsFetchingRequest(currencyIds)
    }


    private fun onWalletsFetchingRequestSucceeded(wallets: List<Wallet>) {
        if(wallets.isEmpty()) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        val currency = transactionData.currency
        val protocolId = getDataLoadingParams().protocolId
        val withdrawalFeeCurrencyId = transactionData.getWithdrawalFeeCurrencyId(protocolId)
        val walletsMap = wallets.mapToCurrencyIdWalletMap()
        val wallet = walletsMap[currency.id]
        val feeWallet = walletsMap[withdrawalFeeCurrencyId]

        if(wallet != null) {
            val formattedBalance = numberFormatter.formatBalance(wallet.currentBalance)

            mView.updateAvailableBalance("$formattedBalance ${currency.symbol}")
            transactionData = transactionData.copy(wallet = wallet)

            mPerformedWalletActions.addBalanceChangedWallet(wallet)
        }

        if(feeWallet != null) {
            mPerformedWalletActions.addBalanceChangedWallet(feeWallet)
        }

        showWithdrawalCreatedDialog()
    }


    private fun showWithdrawalCreatedDialog() {
        val title = mStringProvider.getString(
            R.string.withdrawal_creation_fragment_success_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.withdrawal_creation_fragment_success_dialog_message
        )
        val negativeBtnText = mStringProvider.getString(
            R.string.withdrawal_creation_fragment_success_dialog_neutral_button_text
        )
        val positiveBtnText = mStringProvider.getString(
            R.string.withdrawal_creation_fragment_success_dialog_positive_button_text
        )

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = negativeBtnText,
            positiveBtnText = positiveBtnText,
            negativeBtnClick = {
                onWithdrawalCreatedDialogNegativeButtonClicked()
            }
        ))
    }


    private fun onWithdrawalCreatedDialogNegativeButtonClicked() {
        mView.navigateToBalanceContainerScreen()
    }


    private fun onWithdrawalCreationRequestFailed(exception: Throwable) {
        when(exception) {
            is WithdrawalCreationException -> when(exception.error) {
                WithdrawalCreationException.Error.INVALID_ADDRESS -> {
                    showErrorDialog(mStringProvider.getString(
                        R.string.error_invalid_withdrawal_address
                    ))
                }

                WithdrawalCreationException.Error.NOT_ENOUGH_COSTS_TO_PAY_FEE -> {
                    showErrorDialog(mStringProvider.getString(
                        R.string.error_not_enough_costs_to_pay_fee,
                        transactionData.currency.withdrawalFeeCurrencySymbol
                    ))
                }

                WithdrawalCreationException.Error.DESTINATION_TAG_REQUIRED -> {
                    mView.setInputViewState(
                        state = InputViewState.ERRONEOUS,
                        inputViews = listOf(WithdrawalInputView.EXTRA)
                    )

                    showErrorDialog(mStringProvider.getString(
                        R.string.error_destination_tag_required
                    ))
                }

                WithdrawalCreationException.Error.UNKNOWN -> {
                    showErrorDialog(exception.message)
                }
            }

            else -> mView.showToast(mStringProvider.getErrorMessage(exception))
        }
    }


}