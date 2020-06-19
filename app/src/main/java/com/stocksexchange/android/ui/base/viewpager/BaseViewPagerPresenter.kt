package com.stocksexchange.android.ui.base.viewpager

import androidx.annotation.CallSuper
import com.stocksexchange.android.ui.base.mvp.model.Model
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.core.utils.interfaces.Selectable

/**
 * A base presenter of the MVP architecture that contains
 * functionality related to view pager.
 */
abstract class BaseViewPagerPresenter<out V, out M>(
    view: V,
    model: M
) : BasePresenter<V, M>(view, model) where
        V : ViewPagerView,
        M : Model {


    @CallSuper
    open fun onTabSelected(position: Int) {
        mView.setFragmentSelected(
            position = position,
            isSelected = true,
            source = Selectable.Source.TAB
        )
    }


    open fun onTabUnselected(position: Int) {
        mView.setFragmentSelected(
            position = position,
            isSelected = false,
            source = Selectable.Source.TAB
        )
    }


    @CallSuper
    open fun onTabReselected(position: Int) {
        onScrollToTopRequested(position)
    }


    @CallSuper
    open fun onViewPagerPageSelected(position: Int) {
        mView.setFragmentSelected(
            position = position,
            isSelected = true,
            source = Selectable.Source.SWIPE
        )
    }


    @CallSuper
    open fun onScrollToTopRequested(position: Int) {
        mView.scrollFragmentToTop(position)
    }


}