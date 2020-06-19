package com.stocksexchange.core.utils.listeners

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

/**
 * A listener to use when there is a need to get notified
 * when the main view showing animation has been started
 * and ended.
 */
class OnMainViewShowAnimationListener<DataSource : Enum<*>>(
    val dataSource: DataSource,
    val callback: Callback<DataSource>
) : AnimatorListenerAdapter() {


    override fun onAnimationStart(animator: Animator) {
        onAnimationStart()
    }


    /**
     * A callback to invoke to denote that the animation has been
     * started without providing an animator.
     */
    fun onAnimationStart() {
        callback.onMainViewShowAnimationStarted(dataSource)
    }


    override fun onAnimationEnd(animator: Animator) {
        onAnimationEnd()
    }


    /**
     * A callback to invoke to denote that the animation has been
     * ended without providing an animator.
     */
    fun onAnimationEnd() {
        callback.onMainViewShowAnimationEnded(dataSource)
    }


    interface Callback<DataSource : Enum<*>> {

        fun onMainViewShowAnimationStarted(dataSource: DataSource)

        fun onMainViewShowAnimationEnded(dataSource: DataSource)

    }


}