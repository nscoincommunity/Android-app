package com.stocksexchange.android.data.repositories.withdrawals

import com.stocksexchange.api.model.rest.Withdrawal
import com.stocksexchange.api.model.rest.WithdrawalCancellationResponse
import com.stocksexchange.api.model.rest.WithdrawalConfirmationEmailSendingResponse
import com.stocksexchange.api.model.rest.WithdrawalConfirmationResponse
import com.stocksexchange.android.data.base.WithdrawalsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface WithdrawalsRepository : WithdrawalsData<
    RepositoryResult<Withdrawal>,
    RepositoryResult<WithdrawalConfirmationEmailSendingResponse>,
    RepositoryResult<WithdrawalConfirmationResponse>,
    RepositoryResult<WithdrawalCancellationResponse>,
    RepositoryResult<List<Withdrawal>>
>, Repository {

    suspend fun refresh()

}