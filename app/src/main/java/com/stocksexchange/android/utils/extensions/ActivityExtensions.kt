package com.stocksexchange.android.utils.extensions

import android.app.Activity
import androidx.annotation.ColorInt
import com.stocksexchange.android.R
import com.stocksexchange.android.model.SystemWindowType
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.core.utils.extensions.setNavigationBarColor
import com.stocksexchange.core.utils.extensions.setStatusBarColor
import com.stocksexchange.core.utils.extensions.setTaskDescriptionCompat


/**
 * Overrides the animations of the entering window.
 *
 * @param transitionAnimations The animations to use for the entering window
 */
fun Activity.overrideEnterTransition(transitionAnimations: TransitionAnimations) {
    if(transitionAnimations.id == TransitionAnimations.DEFAULT_ANIMATIONS.id) {
        return
    }

    overridePendingTransition(
        transitionAnimations.windowBEnterAnimation,
        transitionAnimations.windowAExitAnimation
    )
}


/**
 * Overrides the animations of the exiting window.
 *
 * @param transitionAnimations The animations to use for the exiting window
 */
fun Activity.overrideExitTransition(transitionAnimations: TransitionAnimations) {
    if(transitionAnimations.id == TransitionAnimations.DEFAULT_ANIMATIONS.id) {
        return
    }

    overridePendingTransition(
        transitionAnimations.windowAEnterAnimation,
        transitionAnimations.windowBExitAnimation
    )
}


fun Activity.setRecentAppsToolbarColor(@ColorInt color: Int) {
    setTaskDescriptionCompat(
        label = getString(R.string.app_name),
        iconId = R.mipmap.ic_launcher,
        primaryColor = color
    )
}


fun Activity.setSystemWindowColor(@ColorInt color: Int, type: SystemWindowType) {
    when(type) {
        SystemWindowType.STATUS_BAR -> setStatusBarColor(color)
        SystemWindowType.NAVIGATION_BAR -> setNavigationBarColor(color)
        SystemWindowType.RECENT_APPS_TOOLBAR -> setRecentAppsToolbarColor(color)
    }
}