package com.stocksexchange.android.ui.dashboard

import com.stocksexchange.android.model.DashboardArgs
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.core.utils.interfaces.Selectable

interface DashboardContract {


    interface View : BaseView {

        fun showBottomNavigation()

        fun hideBottomNavigation()

        fun scrollPageToTop(position: Int)

        fun restartActivity(dashboardArgs: DashboardArgs)

        fun launchVerificationPromptActivity(descriptionType: VerificationPromptDescriptionType)

        fun launchLoginActivity(dashboardArgs: DashboardArgs)

        fun notifyPageAboutSettingsChange(position: Int, newSettings: Settings)

        fun applyNewTheme(theme: Theme)

        fun setPageSelected(position: Int, isSelected: Boolean, source: Selectable.Source)

        fun setViewPagerCurrentItemPosition(position: Int, smoothScroll: Boolean)

        fun isViewPagerAdapterEmpty(): Boolean

        fun getViewPagerCurrentItemPosition(): Int

    }


    interface ActionListener {

        fun onNavigationItemSelected(id: Int): Boolean

        fun onNavigationItemReselected(id: Int)

        fun onShowBottomNavigation()

        fun onHideBottomNavigation()

    }


}