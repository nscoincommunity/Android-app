package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class BalanceTab(
    @StringRes val titleId: Int
) {

    WALLETS(
        titleId = R.string.wallets
    ),
    DEPOSITS(
        titleId = R.string.deposits
    ),
    WITHDRAWALS(
        titleId = R.string.withdrawals
    )

}