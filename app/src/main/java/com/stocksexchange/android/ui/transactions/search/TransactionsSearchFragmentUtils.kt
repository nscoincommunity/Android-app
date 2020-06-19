package com.stocksexchange.android.ui.transactions.search

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        transactionType = getSerializableOrThrow(ExtrasKeys.KEY_TRANSACTION_TYPE)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        transactionType = getOrThrow(PresenterStateKeys.KEY_TRANSACTION_TYPE)
    )
}


internal object ExtrasKeys {

    const val KEY_TRANSACTION_TYPE = "transaction_type"

}


internal object PresenterStateKeys {

    const val KEY_TRANSACTION_TYPE = "transaction_type"

}


internal data class Extras(
    val transactionType: TransactionType
)


internal data class PresenterState(
    val transactionType: TransactionType
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_TRANSACTION_TYPE, state.transactionType)
}


fun TransactionsSearchFragment.Companion.newArgs(
    transactionType: TransactionType
) : Bundle {
    return bundleOf(
        ExtrasKeys.KEY_TRANSACTION_TYPE to transactionType
    )
}