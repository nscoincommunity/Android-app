package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import com.crashlytics.android.Crashlytics

/**
 * An implementation of a ViewPager widget that has support
 * for custom scrolling.
 */
class CustomViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {


    private var isSwipingEnabled: Boolean = true

    private var mCustomScroller: CustomScroller? = null




    override fun onFinishInflate() {
        super.onFinishInflate()

        initCustomScroller()
    }


    private fun initCustomScroller() {
        try {
            val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true

            val interpolatorField = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true

            mCustomScroller = CustomScroller(context, (interpolatorField.get(null) as? Interpolator))
            scrollerField.set(this, mCustomScroller)
        } catch(exception: Exception) {
            Crashlytics.logException(exception)
        }
    }


    fun enableSwiping() {
        isSwipingEnabled = true
    }


    fun disableSwiping() {
        isSwipingEnabled = false
    }


    /**
     * Sets a visible current item.
     *
     * @param itemPosition The position of the item
     * @param smoothScroll Whether to scroll smoothly
     * @param scrollFactor The factor of the scrolling
     */
    fun setCurrentItem(itemPosition: Int, smoothScroll: Boolean, scrollFactor: Double) {
        if(scrollFactor != CustomScroller.DEFAULT_SCROLL_FACTOR) {
            mCustomScroller?.scrollFactor = scrollFactor

            addOnPageChangeListener(object : SimpleOnPageChangeListener() {

                override fun onPageScrollStateChanged(state: Int) {
                    if(state != SCROLL_STATE_IDLE) {
                        return
                    }

                    mCustomScroller?.resetScrollFactor()
                    removeOnPageChangeListener(this)
                }

            })
        }

        setCurrentItem(itemPosition, smoothScroll)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return (isSwipingEnabled && super.onTouchEvent(event))
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return (isSwipingEnabled && super.onInterceptTouchEvent(event))
    }


    private class CustomScroller @JvmOverloads constructor(
        context: Context,
        interpolator: Interpolator? = null,
        flyWheel: Boolean = false
    ) : Scroller(context, interpolator, flyWheel) {


        companion object {

            const val DEFAULT_SCROLL_FACTOR = 1.0

        }


        internal var scrollFactor: Double = DEFAULT_SCROLL_FACTOR


        fun resetScrollFactor() {
            scrollFactor = DEFAULT_SCROLL_FACTOR
        }


        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, (duration * scrollFactor).toInt())
        }


    }


}