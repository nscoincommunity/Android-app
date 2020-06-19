package com.stocksexchange.android.data.stores.withdrawals

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.database.tables.WithdrawalsTable
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class WithdrawalsDatabaseDataStore(
    private val withdrawalsTable: WithdrawalsTable
) : WithdrawalsDataStore {


    override suspend fun save(withdrawal: Withdrawal) {
        executeBackgroundOperation {
            withdrawalsTable.save(withdrawal)
        }
    }


    override suspend fun save(withdrawals: List<Withdrawal>) {
        executeBackgroundOperation {
            withdrawalsTable.save(withdrawals)
        }
    }


    override suspend fun create(params: WithdrawalCreationParameters): Result<Withdrawal> {
        throw UnsupportedOperationException()
    }


    override suspend fun sendConfirmationEmail(withdrawalId: Long): Result<WithdrawalConfirmationEmailSendingResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun confirm(params: WithdrawalConfirmationParameters): Result<WithdrawalConfirmationResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun cancel(withdrawalId: Long): Result<WithdrawalCancellationResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            withdrawalsTable.deleteAll()
        }
    }


    override suspend fun search(params: TransactionParameters): Result<List<Withdrawal>> {
        return performBackgroundOperation {
            withdrawalsTable.search(params)
        }
    }


    override suspend fun get(params: TransactionParameters): Result<List<Withdrawal>> {
        return performBackgroundOperation {
            withdrawalsTable.get(params)
        }
    }


}