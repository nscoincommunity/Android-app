package com.stocksexchange.android.ui.transactioncreation.withdrawalcreation

import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepository
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationModel
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationModel.ActionListener
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.utils.extensions.onSuccessOnlyIf
import com.stocksexchange.api.exceptions.rest.WalletException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WithdrawalCreationModel(
    walletsRepository: WalletsRepository,
    currenciesRepository: CurrenciesRepository,
    private val withdrawalsRepository: WithdrawalsRepository
) : BaseTransactionCreationModel<ActionListener>(
    walletsRepository,
    currenciesRepository
) {


    companion object {

        const val REQUEST_TYPE_WITHDRAWAL_CREATION = 0
        const val REQUEST_TYPE_WALLETS_FETCHING = 1

    }




    fun performWithdrawalCreationRequest(params: WithdrawalCreationParameters) {
        performRequest(
            requestType = REQUEST_TYPE_WITHDRAWAL_CREATION,
            params = params
        )
    }


    fun performWalletsFetchingRequest(params: List<Int>) {
        performRequest(
            requestType = REQUEST_TYPE_WALLETS_FETCHING,
            params = params
        )
    }


    @Suppress("UNCHECKED_CAST")
    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_WITHDRAWAL_CREATION -> {
                withdrawalsRepository.create(params as WithdrawalCreationParameters)
            }

            REQUEST_TYPE_WALLETS_FETCHING -> {
                // Refreshing to retrieve the most up-to-date data
                walletsRepository.refresh()

                // Actual fetching
                walletsRepository.getByCurrencyIds(params as List<Int>)
            }

            else -> throw IllegalStateException()

        }
    }


    override suspend fun performDataLoading(params: TransactionCreationParameters) {
        walletsRepository.get(params.walletId)
            .log("walletsRepository.get(walletId: ${params.walletId})")
            .onSuccess { proceedToFetchCurrency(params, it) }
            .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    interface ActionListener : BaseTransactionCreationActionListener


}