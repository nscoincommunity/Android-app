package com.stocksexchange.core.utils.extensions

import android.os.Parcelable
import android.view.View
import com.stocksexchange.core.Constants


/**
 * Fetches the proper state from the parcelable.
 *
 * Note: This method is needed because of crashes
 * happening on Android Lollipop devices.
 *
 * @return The proper state of this parcelable
 */
fun Parcelable?.fetchProperState(): Parcelable? {
    if(this == null) {
        return null
    }

    return if(Constants.AT_LEAST_MARSHMALLOW) {
        this
    } else {
        View.BaseSavedState.EMPTY_STATE
    }
}