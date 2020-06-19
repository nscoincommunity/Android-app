package com.stocksexchange.core.utils.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.stocksexchange.core.R
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible

private const val DEFAULT_DURATION = 300L
private val DEFAULT_INTERPOLATOR = LinearInterpolator()

private val TAG_IS_VISIBLE = R.string.tag_is_visible
private val TAG_IS_ANIMATING = R.string.tag_is_animating


/**
 * Cross fades two views.
 *
 * @param viewToHide The view to hide by animating its alpha to 0
 * @param viewToShow The view to show by animating its alpha to 1
 * @param duration The duration of the animation. Default is 300 milliseconds.
 * @param interpolator The interpolator for the animation. Default is [LinearInterpolator].
 * @param onFinish The callback to invoke when the animation has been finished
 * @param onBeforeShowingView The callback to get notified when the view showing
 * is about to start
 */
fun crossFade(
    viewToHide: View,
    viewToShow: View,
    duration: Long = DEFAULT_DURATION,
    interpolator: Interpolator = DEFAULT_INTERPOLATOR,
    onFinish: (() -> Unit)? = null,
    onBeforeShowingView: (() -> Unit)
) {
    // Cancelling the ongoing animations (if any)
    viewToHide.animate().cancel()

    if(viewToHide.id != viewToShow.id) {
        viewToShow.animate().cancel()
    }

    // Calculating the duration of the fade animation
    val animationDuration = (duration / 2L)

    // Creating a listener for a fade in animation (if needed)
    val fadeInAnimationListener = if(onFinish == null) {
        null
    } else {
        object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                onFinish()
            }

        }
    }

    // Preparing and starting the fade out animation
    viewToHide.animate()
        .alpha(0f)
        .setListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                // Calling the listener to notify that the view showing
                // is about to start
                onBeforeShowingView()

                // Preparing and starting the fade in animation
                viewToShow.animate()
                    .alpha(1f)
                    .setListener(fadeInAnimationListener)
                    .setInterpolator(interpolator)
                    .setDuration(animationDuration)
                    .start()
            }

        })
        .setInterpolator(interpolator)
        .setDuration(duration)
        .start()
}


/**
 * Shows the action button.
 *
 * @param view The button to show
 * @param duration The duration of the animation
 * @param animate Whether to animate the process or not
 */
fun showActionButton(view: View, duration: Long, animate: Boolean) {
    if(isViewVisible(view) || isViewAnimating(view)) {
        return
    }

    if(animate) {
        view.translationY = view.height.toFloat()
        view.makeVisible()
        setAnimating(view, true)

        view.animate()
            .translationY(0f)
            .setInterpolator(LinearInterpolator())
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    setViewVisible(view, true)
                    setAnimating(view, false)
                }

            })
            .start()
    } else {
        view.translationY = 0f
        view.makeVisible()
        setViewVisible(view, true)
    }
}


/**
 * Hides the action button.
 *
 * @param view The button to hide
 * @param duration The duration of the animation
 * @param animate Whether to animate the process or not
 */
fun hideActionButton(view: View, duration: Long, animate: Boolean) {
    if(!isViewVisible(view) || isViewAnimating(view)) {
        return
    }

    if(animate) {
        view.translationY = 0f
        setAnimating(view, true)

        view.animate()
            .translationY(view.height.toFloat())
            .setInterpolator(LinearInterpolator())
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    view.makeGone()

                    setViewVisible(view, false)
                    setAnimating(view, false)
                }

            })
            .start()
    } else {
        view.translationY = view.height.toFloat()
        view.makeGone()
        setViewVisible(view, false)
    }
}


/**
 * Sets a tag of the view specifying whether the view is
 * visible or not.
 *
 * @param view The view itself
 * @param isVisible Whether the view is visible or not
 */
fun setViewVisible(view: View, isVisible: Boolean) {
    view.setTag(TAG_IS_VISIBLE, isVisible)
}


/**
 * Sets a tag of the view specifying whether the view
 * is currently being animated or not.
 *
 * @param view The view itself
 * @param isAnimating Whether the view is being animated or not
 */
fun setAnimating(view: View, isAnimating: Boolean) {
    view.setTag(TAG_IS_ANIMATING, isAnimating)
}


/**
 * Determines whether the view is visible by checking the
 * previous set tag.
 *
 * @param view The view itself
 *
 * @return true if visible; false otherwise
 */
fun isViewVisible(view: View): Boolean {
    return ((view.getTag(TAG_IS_VISIBLE) != null) && (view.getTag(TAG_IS_VISIBLE) as Boolean))
}


/**
 * Determines whether the view is being animated by checking
 * the previous set tag.
 *
 * @param view The view itself
 *
 * @return true if being animated; false otherwise
 */
fun isViewAnimating(view: View): Boolean {
    return ((view.getTag(TAG_IS_ANIMATING) != null) && (view.getTag(TAG_IS_ANIMATING) as Boolean))
}