package com.stocksexchange.android.ui.views.base.interfaces

/**
 * A view interface that contains methods common for
 * all custom created views.
 */
interface BaseView {


    /**
     * Initializes the view.
     */
    fun init() {
        // Stub
    }


    /**
     * Retrieves a layout resource ID of the view.
     *
     * @return The layout resource ID
     */
    fun getLayoutResourceId(): Int


}