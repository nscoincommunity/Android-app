package com.stocksexchange.android.data.stores.withdrawals

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class WithdrawalsServerDataStore(
    private val stexRestApi: StexRestApi
) : WithdrawalsDataStore {


    override suspend fun save(withdrawal: Withdrawal) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(withdrawals: List<Withdrawal>) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(params: WithdrawalCreationParameters): Result<Withdrawal> {
        return performBackgroundOperation {
            stexRestApi.createWithdrawal(params)
        }
    }


    override suspend fun sendConfirmationEmail(withdrawalId: Long): Result<WithdrawalConfirmationEmailSendingResponse> {
        return performBackgroundOperation {
            stexRestApi.sendWithdrawalConfirmationEmail(withdrawalId)
        }
    }


    override suspend fun confirm(params: WithdrawalConfirmationParameters): Result<WithdrawalConfirmationResponse> {
        return performBackgroundOperation {
            stexRestApi.confirmWithdrawal(params)
        }
    }


    override suspend fun cancel(withdrawalId: Long): Result<WithdrawalCancellationResponse> {
        return performBackgroundOperation {
            stexRestApi.cancelWithdrawal(withdrawalId)
        }
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: TransactionParameters): Result<List<Withdrawal>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TransactionParameters): Result<List<Withdrawal>> {
        return performBackgroundOperation {
            stexRestApi.getWithdrawals(params)
        }
    }


}