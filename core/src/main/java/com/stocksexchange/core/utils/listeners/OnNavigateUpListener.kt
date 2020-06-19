package com.stocksexchange.core.utils.listeners

/**
 * A listener to use to get notified when the navigate
 * up button is pressed.
 */
interface OnNavigateUpListener {

    /**
     * Handles the navigate up event.
     *
     * @return true if we have navigated up successfully;
     * false otherwise
     */
    fun onNavigateUpPressed(): Boolean

}