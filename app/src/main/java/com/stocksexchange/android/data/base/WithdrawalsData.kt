package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.Withdrawal
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters

interface WithdrawalsData<
    WithdrawalCreationResult,
    WithdrawalConfirmationEmailSendingResult,
    WithdrawalConfirmationResult,
    WithdrawalCancellationResult,
    WithdrawalsFetchingResult
> {

    suspend fun save(withdrawal: Withdrawal)

    suspend fun save(withdrawals: List<Withdrawal>)

    suspend fun create(params: WithdrawalCreationParameters): WithdrawalCreationResult

    suspend fun sendConfirmationEmail(withdrawalId: Long): WithdrawalConfirmationEmailSendingResult

    suspend fun confirm(params: WithdrawalConfirmationParameters): WithdrawalConfirmationResult

    suspend fun cancel(withdrawalId: Long): WithdrawalCancellationResult

    suspend fun deleteAll()

    suspend fun search(params: TransactionParameters): WithdrawalsFetchingResult

    suspend fun get(params: TransactionParameters): WithdrawalsFetchingResult

}