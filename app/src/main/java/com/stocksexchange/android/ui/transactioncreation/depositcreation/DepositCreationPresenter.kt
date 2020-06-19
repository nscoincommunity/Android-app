package com.stocksexchange.android.ui.transactioncreation.depositcreation

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationPresenter
import com.stocksexchange.api.exceptions.rest.WalletException
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.handlers.ClipboardHandler

class DepositCreationPresenter(
    view: DepositCreationContract.View,
    model: DepositCreationModel,
    private val connectionProvider: ConnectionProvider,
    private val clipboardHandler: ClipboardHandler
) : BaseTransactionCreationPresenter<DepositCreationContract.View, DepositCreationModel>(view, model),
    DepositCreationContract.ActionListener, DepositCreationModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    override fun getEmptyViewCaption(params: TransactionCreationParameters): String {
        return mStringProvider.getTransactionCreationEmptyCaption()
    }


    override fun getErrorViewCaption(exception: Throwable): String {
        return when(exception) {
            is WalletException -> when(exception.error) {
                WalletException.Error.WALLET_NOT_FOUND,
                WalletException.Error.ADDRESS_ABSENCE -> {
                    mStringProvider.getString(R.string.error_no_address)
                }

                WalletException.Error.DISABLED_DEPOSITS -> {
                    mStringProvider.getString(R.string.error_deposits_disabled)
                }

                else -> super.getErrorViewCaption(exception)
            }

            else -> super.getErrorViewCaption(exception)
        }
    }


    override fun onDataLoadingSucceeded(data: TransactionData) {
        val wallet = data.wallet

        // To prevent from repeatedly sending events
        // when the wallet already exists and there is not need
        // to update it, hence the second condition
        if(wallet.hasId && !transactionData.wallet.hasId) {
            mPerformedWalletActions.addIdCreatedWallet(data.wallet)
        }

        updateDataLoadingParams {
            it.copy(
                wallet = wallet,
                mode = TransactionCreationParameters.Mode.WALLET_RETRIEVAL
            )
        }

        transactionData = data

        super.onDataLoadingSucceeded(data)

        mView.updateCreateAddressButtonState(
            params = transactionCreationParams,
            data = data
        )
    }


    private fun updateDataLoadingParams(getNewParams: (TransactionCreationParameters) -> TransactionCreationParameters) {
        transactionCreationParams = getNewParams(getDataLoadingParams())
    }


    override fun onAddressParameterValueClicked(value: String) {
        copyTextToClipboard(value, mStringProvider.getString(R.string.copied_to_clipboard))
    }


    override fun onExtraParameterValueClicked(value: String) {
        copyTextToClipboard(value, mStringProvider.getString(R.string.copied_to_clipboard))
    }


    private fun copyTextToClipboard(text: String, toastText: String) {
        clipboardHandler.copyText(text)

        mView.showToast(toastText)
    }


    override fun onCopyButtonClicked() {
        val protocolId = getDataLoadingParams().protocolId
        val data = transactionData

        if(data.isEmpty || !data.wallet.hasDepositAddressData(protocolId)) {
            mView.showToast(mStringProvider.getString(R.string.error_no_data_available_to_copy))
            return
        }

        val addressData = data.wallet.getDepositAddressData(protocolId)!!

        if(addressData.hasAdditionalParameter) {
            showParameterSelectionDialog(addressData)
        } else {
            copyTextToClipboard(
                text = addressData.addressParameterValue,
                toastText = mStringProvider.getString(R.string.deposit_address_copied_to_clipboard)
            )
        }
    }


    private fun showParameterSelectionDialog(addressData: TransactionAddressData) {
        val depositAddressString = mStringProvider.getString(R.string.deposit_address)
        val additionalParamName = addressData.strippedAdditionalParameterName
        val items = arrayOf(depositAddressString, additionalParamName)
        val itemsCallback: ((String) -> Unit) = {
            val parameterValue = if(it == depositAddressString) {
                addressData.addressParameterValue
            } else {
                addressData.additionalParameterValue
            }
            val toastText = if(it == depositAddressString) {
                mStringProvider.getString(R.string.deposit_address_copied_to_clipboard)
            } else {
                mStringProvider.getString(
                    R.string.extra_parameter_value_copied_to_clipboard_template,
                    additionalParamName
                )
            }

            copyTextToClipboard(
                text = parameterValue,
                toastText = toastText
            )
        }

        mView.showMaterialDialog(MaterialDialogBuilder.listDialog(
            items = items,
            itemsCallback = itemsCallback
        ))
    }


    override fun onCreateAddressButtonClicked() {
        if(mModel.isDataLoading) {
            return
        }

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            content = mStringProvider.getString(R.string.deposit_creation_fragment_new_address_creation_dialog_content),
            negativeBtnText = mStringProvider.getString(R.string.no),
            positiveBtnText = mStringProvider.getString(R.string.yes),
            positiveBtnClick = {
                onCreateNewDepositAddressConfirmed()
            }
        ))
    }


    private fun onCreateNewDepositAddressConfirmed() {
        if(!connectionProvider.isNetworkAvailable()) {
            mView.showToast(mStringProvider.getInternetConnectionCheckMessage())
            return
        }

        updateDataLoadingParams {
            it.copy(mode = if(it.hasWalletId) {
                TransactionCreationParameters.Mode.ADDRESS_CREATION
            } else {
                TransactionCreationParameters.Mode.WALLET_CREATION
            })
        }

        resetData()

        reloadData(DataLoadingTrigger.OTHER)
    }


    private fun resetData() {
        transactionData = TransactionData()
    }


}