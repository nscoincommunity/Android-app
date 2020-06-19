package com.stocksexchange.android.utils

import com.stocksexchange.core.handlers.PreferenceHandler

class TipShowingTracker(
    private val preferenceHandler: PreferenceHandler
) {


    companion object {

        private const val KEY_PREVIEW_ORDER_CANCELLATION = "preview_order_cancellation"

    }




    fun setPreviewOrderCancellationTipShown() {
        preferenceHandler.put(KEY_PREVIEW_ORDER_CANCELLATION, true)
    }


    fun wasPreviewOrderCancellationTipShown(): Boolean {
        return preferenceHandler.get(KEY_PREVIEW_ORDER_CANCELLATION, false)
    }


}