package com.stocksexchange.android.ui.balance

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.BalanceTab
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.WalletBalanceType
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        wasWithdrawalJustConfirmed = getBoolean(ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED),
        wasWithdrawalJustCancelled = getBoolean(ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED),
        selectedTab = getSerializableOrThrow(ExtrasKeys.KEY_SELECTED_TAB)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        wasWithdrawalJustConfirmed = getOrThrow(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED),
        wasWithdrawalJustCancelled = getOrThrow(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED),
        selectedTab = getOrThrow(PresenterStateKeys.KEY_SELECTED_TAB),
        walletsSortBalanceType = getOrThrow(PresenterStateKeys.KEY_WALLETS_SORT_BALANCE_TYPE)
    )
}


internal object ExtrasKeys {

    const val KEY_WAS_WITHDRAWAL_JUST_CONFIRMED = "was_withdrawal_just_confirmed"
    const val KEY_WAS_WITHDRAWAL_JUST_CANCELLED = "was_withdrawal_just_cancelled"
    const val KEY_SELECTED_TAB = "selected_tab"

}


internal object PresenterStateKeys {

    const val KEY_WAS_WITHDRAWAL_JUST_CONFIRMED = "was_withdrawal_just_confirmed"
    const val KEY_WAS_WITHDRAWAL_JUST_CANCELLED = "was_withdrawal_just_cancelled"
    const val KEY_SELECTED_TAB = "selected_tab"
    const val KEY_WALLETS_SORT_BALANCE_TYPE = "wallets_sort_balance_type"

}


internal data class Extras(
    val wasWithdrawalJustConfirmed: Boolean,
    val wasWithdrawalJustCancelled: Boolean,
    val selectedTab: BalanceTab
)


internal data class PresenterState(
    val wasWithdrawalJustConfirmed: Boolean,
    val wasWithdrawalJustCancelled: Boolean,
    val selectedTab: BalanceTab,
    var walletsSortBalanceType: WalletBalanceType
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED, state.wasWithdrawalJustConfirmed)
    save(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED, state.wasWithdrawalJustCancelled)
    save(PresenterStateKeys.KEY_SELECTED_TAB, state.selectedTab)
    save(PresenterStateKeys.KEY_WALLETS_SORT_BALANCE_TYPE, state.walletsSortBalanceType)
}


fun BalanceContainerFragment.Companion.newArgs(
    wasWithdrawalJustConfirmed: Boolean = false,
    wasWithdrawalJustCancelled: Boolean = false,
    selectedTab: BalanceTab = BalanceTab.WALLETS
): Bundle {
    return bundleOf(
        ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED to wasWithdrawalJustConfirmed,
        ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED to wasWithdrawalJustCancelled,
        ExtrasKeys.KEY_SELECTED_TAB to selectedTab
    )
}