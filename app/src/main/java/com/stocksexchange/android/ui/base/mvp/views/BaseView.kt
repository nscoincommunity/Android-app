package com.stocksexchange.android.ui.base.mvp.views

import com.stocksexchange.android.model.MaterialDialogBuilder

/**
 * A base view of the MVP architecture.
 */
interface BaseView {

    fun showToast(message: String)

    fun showLongToast(message: String)

    fun showMaterialDialog(builder: MaterialDialogBuilder)

    fun hideMaterialDialog()

    /**
     * Returns true if we have successfully navigated back.
     * False otherwise.
     */
    fun navigateBack(): Boolean

    fun isInitialized(): Boolean

}