package com.stocksexchange.android.utils.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransitionAnimations

class NavOptionsCreator(
    private val navDestAnimationsRetriever: NavDestAnimationsRetriever
) {


    fun getDefaultNavOptions(@IdRes destinationId: Int): NavOptions {
        return buildNavOptions(destinationId)
    }


    fun getWithdrawalCreationNavOptions(@IdRes destinationId: Int): NavOptions {
        return when(destinationId) {
            R.id.balanceDest -> buildNavOptions(destinationId) {
                setPopUpTo(destinationId, true)
                setLaunchSingleTop(true)
            }

            else -> getDefaultNavOptions(destinationId)
        }
    }


    fun getSettingsNavOptions(@IdRes destinationId: Int): NavOptions {
        return when(destinationId) {
            R.id.inboxDest -> buildNavOptions(navDestAnimationsRetriever.getSettingsAnimations(destinationId))

            else -> getDefaultNavOptions(destinationId)
        }
    }


    private fun buildNavOptions(
        @IdRes destinationId: Int,
        builder: ((NavOptions.Builder.() -> Unit)) = {}
    ): NavOptions {
        return buildNavOptions(
            animations = navDestAnimationsRetriever.getDefaultAnimations(destinationId),
            builder = builder
        )
    }


    private fun buildNavOptions(
        animations: TransitionAnimations,
        builder: ((NavOptions.Builder.() -> Unit)) = {}
    ): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(animations.windowBEnterAnimation)
            .setExitAnim(animations.windowAExitAnimation)
            .setPopEnterAnim(animations.windowAEnterAnimation)
            .setPopExitAnim(animations.windowBExitAnimation)
            .apply(builder)
            .build()
    }


}