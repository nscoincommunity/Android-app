package com.stocksexchange.android.ui.base.mvp.presenters

import com.stocksexchange.android.utils.SavedState

/**
 * A base interface for defining an MVP architecture presenter.
 */
interface Presenter {

    fun start()

    fun stop()

    fun onViewSelected()

    fun onViewUnselected()

    fun onRestoreState(savedState: SavedState)

    fun onSaveState(savedState: SavedState)

    fun onRecycle()

}