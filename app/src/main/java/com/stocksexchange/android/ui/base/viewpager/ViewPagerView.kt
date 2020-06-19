package com.stocksexchange.android.ui.base.viewpager

import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.core.utils.interfaces.Scrollable
import com.stocksexchange.core.utils.interfaces.Selectable

/**
 * A view of the MVP architecture that contains functionality
 * related to the view pager.
 */
interface ViewPagerView : BaseView, Scrollable {


    /**
     * Selects/deselects a fragment at the specified position with the specified source.
     *
     * @param position The position of the fragment
     * @param source The source of the selection
     */
    fun setFragmentSelected(position: Int, isSelected: Boolean, source: Selectable.Source)


    /**
     * Scrolls the fragment to the top position.
     *
     * @param position The position of the fragment to scroll to top
     */
    fun scrollFragmentToTop(position: Int)


}