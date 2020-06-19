package com.stocksexchange.android.ui.transactions.fragment

import android.os.Parcelable
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.data.repositories.deposits.DepositsRepository
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepository
import com.stocksexchange.android.mappings.mapToIdCurrencyMap
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel
import com.stocksexchange.android.ui.transactions.fragment.TransactionsModel.ActionListener
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionsModel(
    private val currenciesRepository: CurrenciesRepository,
    private val depositsRepository: DepositsRepository,
    private val withdrawalsRepository: WithdrawalsRepository
) : BaseDataLoadingModel<
    List<Transaction>,
    TransactionParameters,
    ActionListener
    >() {


    companion object {

        const val REQUEST_TYPE_WITHDRAWAL_CONFIRMATION_EMAIL = 0
        const val REQUEST_TYPE_WITHDRAWAL_CANCELLATION = 1

    }




    override fun canLoadData(params: TransactionParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val transactionMode = params.mode
        val searchQuery = params.searchQuery

        val isTransactionSearch = (transactionMode == TransactionMode.SEARCH)
        val isNewData = (dataType == DataType.NEW_DATA)

        val isTransactionSearchWithNoQuery = (isTransactionSearch && searchQuery.isBlank())
        val isTransactionSearchNewData = (isTransactionSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)

        return (!isTransactionSearchWithNoQuery
                && !isTransactionSearchNewData
                && (!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger))
    }


    override suspend fun refreshData(params: TransactionParameters) {
        when(params.type) {
            TransactionType.DEPOSITS -> depositsRepository.refresh()
            TransactionType.WITHDRAWALS -> withdrawalsRepository.refresh()
        }
    }


    override suspend fun performDataLoading(params: TransactionParameters) {
        when(params.mode) {
            TransactionMode.STANDARD -> when(params.type) {
                TransactionType.DEPOSITS -> depositsRepository.get(params)
                TransactionType.WITHDRAWALS -> withdrawalsRepository.get(params)
            }

            TransactionMode.SEARCH -> when(params.type) {
                TransactionType.DEPOSITS -> depositsRepository.search(params)
                TransactionType.WITHDRAWALS -> withdrawalsRepository.search(params)
            }
        }.log(getLogKey(params))
        .onSuccess { items ->
            val currenciesRepositoryResult = currenciesRepository.getAll().apply {
                log("currenciesRepository.getAll()")
            }

            withContext(Dispatchers.Main) {
                currenciesRepositoryResult
                    .onSuccess { currencies ->
                        handleSuccessfulResponse(toTransactions(
                            params = params,
                            items = items,
                            currencies = currencies
                        ))
                    }
                    .onFailure { handleUnsuccessfulResponse(it) }
            }
        }
        .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    private fun getLogKey(params: TransactionParameters): String {
        return when(params.mode) {
            TransactionMode.STANDARD -> when(params.type) {
                TransactionType.DEPOSITS -> "depositsRepository.get(params: $params)"
                TransactionType.WITHDRAWALS -> "withdrawalsRepository.get(params: $params)"
            }

            TransactionMode.SEARCH -> when(params.type) {
                TransactionType.DEPOSITS -> "depositsRepository.search(params: $params)"
                TransactionType.WITHDRAWALS -> "withdrawalsRepository.search(params: $params)"
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun toTransactions(params: TransactionParameters, items: List<Parcelable>,
                               currencies: List<Currency>): List<Transaction> {
        val type = params.type
        val idCurrencyMap = currencies.mapToIdCurrencyMap()

        var deposit: Deposit = Deposit.STUB_DEPOSIT
        var withdrawal: Withdrawal = Withdrawal.STUB_WITHDRAWAl
        var currency: Currency?
        var protocolId: Int

        return mutableListOf<Transaction>().apply {
            for(item in items) {
                when(type) {

                    TransactionType.DEPOSITS -> {
                        deposit = (item as Deposit)
                        currency = idCurrencyMap[deposit.currencyId]
                        protocolId = deposit.protocolId
                    }

                    TransactionType.WITHDRAWALS -> {
                        withdrawal = (item as Withdrawal)
                        currency = idCurrencyMap[withdrawal.currencyId]
                        protocolId = withdrawal.addressData.protocolId
                    }

                }

                add(Transaction(
                    type = type,
                    blockExplorerUrl = (currency?.getBlockExplorerUrl(protocolId) ?: ""),
                    deposit = deposit,
                    withdrawal = withdrawal
                ))
            }
        }
    }


    fun performWithdrawalConfirmationEmailSending(withdrawalId: Long) {
        performRequest(
            requestType = REQUEST_TYPE_WITHDRAWAL_CONFIRMATION_EMAIL,
            params = withdrawalId
        )
    }


    fun performWithdrawalCancellation(withdrawalId: Long, transaction: Transaction) {
        performRequest(
            requestType = REQUEST_TYPE_WITHDRAWAL_CANCELLATION,
            params = withdrawalId,
            metadata = transaction
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_WITHDRAWAL_CONFIRMATION_EMAIL -> {
                withdrawalsRepository.sendConfirmationEmail(params as Long).apply {
                    log("withdrawalsRepository.sendConfirmationEmail(id: $params)")
                }
            }

            REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                withdrawalsRepository.cancel(params as Long).apply {
                    log("withdrawalsRepository.cancel(id: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    fun saveWithdrawal(withdrawal: Withdrawal, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            withdrawalsRepository.save(withdrawal)

            onFinish()
        }
    }


    interface ActionListener: BaseDataLoadingActionListener<List<Transaction>>


}