package com.stocksexchange.core.utils.listeners

/**
 * A listener to use to get notified when the back
 * button is pressed.
 */
interface OnBackPressListener {

    /**
     * Handles the press back event.
     *
     * @return true if the event has been consumed; false otherwise
     */
    fun onBackPressed(): Boolean

}