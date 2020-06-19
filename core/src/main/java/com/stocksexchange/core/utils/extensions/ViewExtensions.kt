@file:Suppress("UsePropertyAccessSyntax", "NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior
import com.stocksexchange.core.utils.helpers.crossFade


inline fun View.dpToPx(value: Int): Int = context.dpToPx(value)


inline fun View.dpToPx(value: Float): Float = context.dpToPx(value)


inline fun View.spToPx(value: Int): Int = context.spToPx(value)


inline fun View.spToPx(value: Float): Float = context.spToPx(value)


inline fun View.dimen(@DimenRes resourceId: Int): Float = context.getDimension(resourceId)


inline fun View.dimenInPx(@DimenRes resourceId: Int): Int = context.dimenInPx(resourceId)


inline fun View.getColor(@ColorRes color: Int): Int = context.getCompatColor(color)


inline fun View.getCompatDrawable(@DrawableRes id: Int): Drawable? = context.getCompatDrawable(id)


inline fun View.makeVisible() { isVisible = true }


inline fun View.makeInvisible() { isInvisible = true }


inline fun View.makeGone() { isGone = true }


/**
 * Enables the view by setting its [View.isEnabled] property
 * to true and, optionally, changing its alpha.
 *
 * @param changeAlpha Whether to change the alpha of the view.
 * Default is false.
 * @param alpha The new alpha value for the view if [changeAlpha]
 * parameter is true. Default is 0.5f.
 * @param childrenToo Whether to enable children as well
 * Default is false.
 */
fun View.enable(changeAlpha: Boolean = false, alpha: Float = 1f,
                childrenToo: Boolean = false) {
    if(!isEnabled) {
        isEnabled = true

        if(changeAlpha) {
            setAlpha(alpha)
        }

        if(childrenToo && (this is ViewGroup)) {
            for(child in children) {
                child.enable()
            }
        }
    }
}


/**
 * Disables the view by setting its [View.isEnabled] property
 * to false and, optionally, changing its alpha.
 *
 * @param changeAlpha Whether to change the alpha of the view.
 * Default is false.
 * @param alpha The new alpha value for the view if [changeAlpha]
 * parameter is true. Default is 0.5f.
 * @param childrenToo Whether to disable children as well.
 * Default is false.
 */
fun View.disable(changeAlpha: Boolean = false, alpha: Float = 0.5f,
                 childrenToo: Boolean = false) {
    if(isEnabled) {
        isEnabled = false

        if(changeAlpha) {
            setAlpha(alpha)
        }

        if(childrenToo && (this is ViewGroup)) {
            for(child in children) {
                child.disable()
            }
        }
    }
}


/**
 * Sets the view's horizontal and vertical scale.
 *
 * @param scale The new scale to assign
 */
fun View.setScale(scale: Float) {
    scaleX = scale
    scaleY = scale
}


fun View.setMargins(
    leftMargin: Int? = null,
    topMargin: Int? = null,
    rightMargin: Int? = null,
    bottomMargin: Int? = null
) {
    if(layoutParams !is ViewGroup.MarginLayoutParams) {
        return
    }

    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        if(leftMargin != null) {
            this.marginStart = leftMargin
        }

        if(topMargin != null) {
            this.topMargin = topMargin
        }

        if(rightMargin != null) {
            this.marginEnd = rightMargin
        }

        if(bottomMargin != null) {
            this.bottomMargin = bottomMargin
        }
    }
}


fun View.setLeftMargin(leftMargin: Int) {
    setMargins(leftMargin = leftMargin)
}


fun View.setTopMargin(topMargin: Int) {
    setMargins(topMargin = topMargin)
}


fun View.setRightMargin(rightMargin: Int) {
    setMargins(rightMargin = rightMargin)
}


fun View.setBottomMargin(bottomMargin: Int) {
    setMargins(bottomMargin = bottomMargin)
}


fun View.setHorizontalMargin(margin: Int) {
    setMargins(
        leftMargin = margin,
        rightMargin = margin
    )
}


fun View.setVerticalMargin(margin: Int) {
    setMargins(
        topMargin = margin,
        bottomMargin = margin
    )
}


fun View.clearMargins() {
    setMargins(0, 0, 0, 0)
}


fun View.clearTopMargin() {
    setTopMargin(0)
}


fun View.clearBottomMargin() {
    setBottomMargin(0)
}


fun View.clearVerticalMargins() {
    setVerticalMargin(0)
}


fun View.clearLeftMargin() {
    setLeftMargin(0)
}


fun View.clearRightMargin() {
    setRightMargin(0)
}


fun View.clearHorizontalMargins() {
    setHorizontalMargin(0)
}


fun View.setPadding(padding: Int) {
    setPaddingRelative(padding, padding, padding, padding)
}


fun View.setLeftPadding(padding: Int) {
    setPaddingRelative(padding, paddingTop, paddingEnd, paddingBottom)
}


fun View.setTopPadding(padding: Int) {
    setPaddingRelative(paddingStart, padding, paddingEnd, paddingBottom)
}


fun View.setRightPadding(padding: Int) {
    setPaddingRelative(paddingStart, paddingTop, padding, paddingBottom)
}


fun View.setBottomPadding(padding: Int) {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, padding)
}


fun View.setHorizontalPadding(padding: Int) {
    setPaddingRelative(padding, paddingTop, padding, paddingBottom)
}


fun View.setVerticalPadding(padding: Int) {
    setPaddingRelative(paddingStart, padding, paddingEnd, padding)
}


fun View.clearPadding() {
    setPadding(0)
}


fun View.addPadding(
    leftPadding: Int? = null,
    topPadding: Int? = null,
    rightPadding: Int? = null,
    bottomPadding: Int? = null
) {
    var currentLeftPadding = paddingStart
    var currentTopPadding = paddingTop
    var currentRightPadding = paddingEnd
    var currentBottomPadding = paddingBottom

    if(leftPadding != null) {
        currentLeftPadding += leftPadding
    }

    if(topPadding != null) {
        currentTopPadding += topPadding
    }

    if(rightPadding != null) {
        currentRightPadding += rightPadding
    }

    if(bottomPadding != null) {
        currentBottomPadding += bottomPadding
    }

    setPaddingRelative(
        currentLeftPadding,
        currentTopPadding,
        currentRightPadding,
        currentBottomPadding
    )
}


fun View.addLeftPadding(padding: Int) {
    addPadding(leftPadding = padding)
}


fun View.addTopPadding(padding: Int) {
    addPadding(topPadding = padding)
}


fun View.addRightPadding(padding: Int) {
    addPadding(rightPadding = padding)
}


fun View.addBottomPadding(padding: Int) {
    addPadding(bottomPadding = padding)
}


fun View.addVerticalPadding(padding: Int) {
    addPadding(topPadding = padding, bottomPadding = padding)
}


fun View.addHorizontalPadding(padding: Int) {
    addPadding(leftPadding = padding, rightPadding = padding)
}


fun View.setWidth(width: Int) {
    updateLayoutParams {
        this.width = width
    }
}


fun View.setHeight(height: Int) {
    updateLayoutParams {
        this.height = height
    }
}


fun View.setSize(size: Int) {
    setWidth(size)
    setHeight(size)
}


/**
 * Cross fades itself.
 *
 * @param duration The duration for the cross fade animation. Default is 300 milliseconds.
 * @param interpolator The interpolator for the animation. Default is LinearInterpolator.
 * @param onFinish The callback to invoke when the animation has been finished
 * @param onBeforeShowingView The callback to get notified when the view showing
 * is about to start
 */
fun View.crossFadeItself(
    duration: Long = 300L,
    interpolator: Interpolator = LinearInterpolator(),
    onFinish: (() -> Unit)? = null,
    onBeforeShowingView: () -> Unit
) {
    crossFade(
        viewToHide = this,
        viewToShow = this,
        duration = duration,
        interpolator = interpolator,
        onFinish = onFinish,
        onBeforeShowingView = onBeforeShowingView
    )
}


fun View.postAction(action: (() -> Unit)) {
    post(action)
}


fun View.postActionDelayed(timeInMillis: Long, action: (() -> Unit)) {
    postDelayed(action, timeInMillis)
}


fun View.onFetchScrollableViewInHierarchy(callback: (View) -> Unit) {
    val parentViewGroup = ((parent as? ViewGroup) ?: return)
    val parentLayoutParams = parentViewGroup.layoutParams
    val parentHasCoordinatorLayoutParams = (parentLayoutParams is CoordinatorLayout.LayoutParams)

    if(!parentHasCoordinatorLayoutParams) return

    val coordinatorLayoutParams = ((parentLayoutParams as? CoordinatorLayout.LayoutParams) ?: return)
    val parentHasAppBarLayoutScrollingViewBehavior = (coordinatorLayoutParams.behavior is ScrollingViewBehavior)

    if(!parentHasAppBarLayoutScrollingViewBehavior) return

    val coordinatorLayout = ((parentViewGroup.parent as? CoordinatorLayout) ?: return)
    val appBarLayout = ((coordinatorLayout.children.firstOrNull { it is AppBarLayout }) ?: return)
    val appBarLayoutViewGroup = (appBarLayout as ViewGroup)
    val scrollableView = (appBarLayoutViewGroup.children.firstOrNull {
        val scrollFlags = (it.layoutParams as? AppBarLayout.LayoutParams)?.scrollFlags
        val hasScrollFlagSet = (scrollFlags?.containsBits(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) == true)

        hasScrollFlagSet
    } ?: return)

    if(scrollableView.isLaidOut) {
        callback(scrollableView)
    } else {
        postAction { callback(scrollableView) }
    }
}