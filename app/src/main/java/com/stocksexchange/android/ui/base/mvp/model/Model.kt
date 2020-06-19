package com.stocksexchange.android.ui.base.mvp.model

import com.stocksexchange.android.utils.SavedState
import org.koin.core.KoinComponent

/**
 * A base interface for defining an MVP architecture model.
 */
interface Model : KoinComponent {

    fun start()

    fun stop()

    fun onRestoreState(savedState: SavedState)

    fun onSaveState(savedState: SavedState)

}