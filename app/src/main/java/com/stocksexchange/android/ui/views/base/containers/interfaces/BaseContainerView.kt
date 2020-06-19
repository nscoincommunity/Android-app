package com.stocksexchange.android.ui.views.base.containers.interfaces

import com.stocksexchange.android.ui.views.base.interfaces.BaseView
import com.stocksexchange.core.utils.interfaces.HasAttributes
import org.koin.core.KoinComponent

/**
 * A base interface that encapsulates all of the functionality
 * a base container view should implement.
 */
interface BaseContainerView : BaseView, HasAttributes, KoinComponent