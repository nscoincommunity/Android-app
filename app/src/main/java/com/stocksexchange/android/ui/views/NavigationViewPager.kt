package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NavigationViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {


    var isSwipeable = true




    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return (isSwipeable && super.onInterceptTouchEvent(ev))
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return (isSwipeable && super.onTouchEvent(ev))
    }


}