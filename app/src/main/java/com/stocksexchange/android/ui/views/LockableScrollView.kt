package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

/**
 * An implementation of the ScrollView with the support for
 * scrolling events as well as the ability to lock and unlock
 * scrolling behavior.
 */
class LockableScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {


    var isScrollable: Boolean = true

    var listener: Listener? = null




    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when(ev.action) {
            MotionEvent.ACTION_DOWN -> (isScrollable && super.onTouchEvent(ev))

            else -> super.onTouchEvent(ev)
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return (isScrollable && super.onInterceptTouchEvent(ev))
    }


    override fun onScrollChanged(scrollX: Int, scrollY: Int,
                                 oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)

        if(scrollY > oldScrollY) {
            listener?.onScrolledDownward()
        }

        if(scrollY < oldScrollY) {
            listener?.onScrolledUpward()
        }

        if(scrollY == 0) {
            listener?.onTopReached()
        }

        if(scrollY == (getChildAt(0).measuredHeight - measuredHeight)) {
            listener?.onBottomReached()
        }
    }


    interface Listener {

        fun onTopReached() {
            // Stub
        }

        fun onBottomReached() {
            // Stub
        }

        fun onScrolledUpward() {
            // Stub
        }

        fun onScrolledDownward() {
            // Stub
        }

    }


}