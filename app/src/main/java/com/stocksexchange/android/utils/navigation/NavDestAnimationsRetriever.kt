package com.stocksexchange.android.utils.navigation

import androidx.annotation.IdRes
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.model.TransitionAnimations.*

class NavDestAnimationsRetriever {


    companion object {

        private val DEFAULT_HORIZONTAL_SLIDING_ANIMATIONS_DESTINATIONS = intArrayOf(
            R.id.settingsDest, R.id.referralDest, R.id.verificationDest,
            R.id.helpDest, R.id.aboutDest, R.id.languageDest,
            R.id.orderbookDest, R.id.tradeDest, R.id.depositCreationDest,
            R.id.withdrawalCreationDest, R.id.protocolSelectionDest
        )

        private val DEFAULT_KITKAT_SCALING_ANIMATIONS_DESTINATIONS = intArrayOf(
            R.id.currencyMarketsSearchDest, R.id.priceAlertsDest, R.id.inboxDest,
            R.id.priceAlertCreationDest, R.id.ordersSearchDest, R.id.walletsSearchDest,
            R.id.transactionsSearchDest
        )

        private val DEFAULT_OVERSHOOT_SCALING_ANIMATIONS_DESTINATIONS = intArrayOf(
            R.id.currencyMarketPreviewDest
        )

    }




    fun getDefaultAnimations(@IdRes destinationId: Int): TransitionAnimations {
        return when(destinationId) {
            in DEFAULT_HORIZONTAL_SLIDING_ANIMATIONS_DESTINATIONS -> HORIZONTAL_SLIDING_ANIMATIONS
            in DEFAULT_KITKAT_SCALING_ANIMATIONS_DESTINATIONS -> KITKAT_SCALING_ANIMATIONS
            in DEFAULT_OVERSHOOT_SCALING_ANIMATIONS_DESTINATIONS -> OVERSHOOT_SCALING_ANIMATIONS

            else -> DEFAULT_ANIMATIONS
        }
    }


    fun getSettingsAnimations(@IdRes destinationId: Int): TransitionAnimations {
        return when(destinationId) {
            R.id.inboxDest -> HORIZONTAL_SLIDING_ANIMATIONS

            else -> getDefaultAnimations(destinationId)
        }
    }


}