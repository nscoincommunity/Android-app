package com.stocksexchange.android.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.DashboardArgs
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.clearTop
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        dashboardArgs = getParcelableOrThrow(ExtrasKeys.KEY_DASHBOARD_ARGS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isBottomNavigationVisible = getOrThrow(PresenterStateKeys.KEY_IS_BOTTOM_NAVIGATION_VISIBLE),
        dashboardArgs = getOrThrow(PresenterStateKeys.KEY_DASHBOARD_ARGS)
    )
}


internal object ExtrasKeys {

    const val KEY_DASHBOARD_ARGS = "dashboard_args"

}


internal object PresenterStateKeys {

    const val KEY_IS_BOTTOM_NAVIGATION_VISIBLE = "is_bottom_navigation_visible"
    const val KEY_DASHBOARD_ARGS = "dashboard_args"

}


internal data class Extras(
    val dashboardArgs: DashboardArgs
)


internal data class PresenterState(
    val isBottomNavigationVisible: Boolean,
    val dashboardArgs: DashboardArgs
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_BOTTOM_NAVIGATION_VISIBLE, state.isBottomNavigationVisible)
    save(PresenterStateKeys.KEY_DASHBOARD_ARGS, state.dashboardArgs)
}


fun DashboardActivity.Companion.newInstance(
    context: Context,
    dashboardArgs: DashboardArgs = DashboardArgs()
) : Intent {
    return context.intentFor<DashboardActivity>().apply {
        putExtra(ExtrasKeys.KEY_DASHBOARD_ARGS, dashboardArgs)
    }.clearTop()
}