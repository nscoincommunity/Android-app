package com.stocksexchange.android.data.stores.withdrawals

import com.stocksexchange.api.model.rest.Withdrawal
import com.stocksexchange.api.model.rest.WithdrawalCancellationResponse
import com.stocksexchange.api.model.rest.WithdrawalConfirmationEmailSendingResponse
import com.stocksexchange.api.model.rest.WithdrawalConfirmationResponse
import com.stocksexchange.android.data.base.WithdrawalsData
import com.stocksexchange.core.model.Result

interface WithdrawalsDataStore : WithdrawalsData<
    Result<Withdrawal>,
    Result<WithdrawalConfirmationEmailSendingResponse>,
    Result<WithdrawalConfirmationResponse>,
    Result<WithdrawalCancellationResponse>,
    Result<List<Withdrawal>>
>