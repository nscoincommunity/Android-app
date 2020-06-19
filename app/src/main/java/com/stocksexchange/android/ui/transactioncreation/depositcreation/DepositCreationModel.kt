package com.stocksexchange.android.ui.transactioncreation.depositcreation

import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.api.model.rest.TransactionNotificationType
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationModel
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationModel.ActionListener
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.api.exceptions.rest.WalletException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DepositCreationModel(
    walletsRepository: WalletsRepository,
    currenciesRepository: CurrenciesRepository
) : BaseTransactionCreationModel<ActionListener>(
    walletsRepository,
    currenciesRepository
) {


    override fun canLoadData(params: TransactionCreationParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val isNewDataWithWalletLastException = (
            (dataType == DataType.NEW_DATA) &&
                (lastDataFetchingException is WalletException) &&
                ((lastDataFetchingException as WalletException).isWalletCreationDelayError ||
                (lastDataFetchingException as WalletException).isWalletNotFoundError)
            )

        return (isNewDataWithWalletLastException ||
            super.canLoadData(params, dataType, dataLoadingTrigger))
    }


    override suspend fun performDataLoading(params: TransactionCreationParameters) {
        getMainDataRepositoryResult(params)
            .onSuccessOnlyIf({ WalletException.absentAddress() }) { it.hasDepositAddressData(params.protocolId) }
            .onSuccessOnlyIf({ WalletException.disabledDeposits() }) {
                val addressData = it.getDepositAddressData(params.protocolId)
                val areDepositsEnabled = (addressData?.notificationType != TransactionNotificationType.DEPOSITS_DISABLED)

                areDepositsEnabled
            }
            .onSuccess { proceedToFetchCurrency(params, it) }
            .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    private suspend fun getMainDataRepositoryResult(params: TransactionCreationParameters): RepositoryResult<Wallet> {
        return when(params.mode) {

            TransactionCreationParameters.Mode.WALLET_RETRIEVAL -> {
                walletsRepository.get(params.walletId).apply {
                    log("walletsRepository.get(walletId: ${params.walletId})")
                }
            }

            TransactionCreationParameters.Mode.WALLET_CREATION -> {
                walletsRepository.create(params.currencyId, params.protocolId).apply {
                    log("walletsRepository.create(currencyId: ${params.currencyId}")
                }
            }

            TransactionCreationParameters.Mode.ADDRESS_CREATION -> {
                walletsRepository.createDepositAddressData(params.wallet, params.protocolId).apply {
                    log("walletsRepository.createDepositAddressData(wallet: ${params.wallet})")
                }
            }

        }
    }


    interface ActionListener : BaseTransactionCreationActionListener


}