package com.stocksexchange.android.ui.transactioncreation.base

import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationModel.BaseTransactionCreationActionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A base model of the MVP architecture that contains common
 * functionality for creating transactions (deposits and withdrawals).
 */
abstract class BaseTransactionCreationModel<
    ActionListener : BaseTransactionCreationActionListener
>(
    protected val walletsRepository: WalletsRepository,
    private val currenciesRepository: CurrenciesRepository
) : BaseDataLoadingModel<
    TransactionData,
    TransactionCreationParameters,
    ActionListener
    >() {


    override suspend fun refreshData(params: TransactionCreationParameters) {
        walletsRepository.refresh()
        currenciesRepository.refresh()
    }


    protected suspend fun proceedToFetchCurrency(params: TransactionCreationParameters,
                                                 wallet: Wallet) {
        val currencyResult = currenciesRepository.get(params.currencyId).apply {
            log("currenciesRepository.get(currencyId: ${params.currencyId})")
        }

        withContext(Dispatchers.Main) {
            currencyResult
                .onSuccess { handleSuccessfulResponse(TransactionData(wallet, it)) }
                .onFailure { handleUnsuccessfulResponse(it) }
        }
    }


    interface BaseTransactionCreationActionListener : BaseDataLoadingActionListener<TransactionData>


}