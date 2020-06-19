package com.stocksexchange.android.ui.referral

import com.stocksexchange.android.model.ReferralMode
import com.stocksexchange.android.utils.SavedState


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        referralMode = getOrThrow(PresenterStateKeys.KEY_REFERRAL_MODE)
    )
}


internal object PresenterStateKeys {

    const val KEY_REFERRAL_MODE = "referral_mode"

}


internal data class PresenterState(
    val referralMode: ReferralMode
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_REFERRAL_MODE, state.referralMode)
}