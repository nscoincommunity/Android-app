package com.stocksexchange.core.utils.listeners

/**
 * A listener used for controlling the visibility of the
 * progress bar.
 */
interface ProgressBarListener {

    fun showProgressBar()

    fun hideProgressBar()

    fun setInboxCountMessage(count: Int)

}