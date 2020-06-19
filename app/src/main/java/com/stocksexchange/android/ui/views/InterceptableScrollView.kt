package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * An implementation of the ScrollView with a touch-intercepting
 * functionality built-in.
 */
class InterceptableScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {


    private var mIsPressedDown: Boolean = false

    /**
     * A listener to invoke when a user pressed and then released this view.
     */
    var interceptableClickListener: OnClickListener? = null




    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {

            MotionEvent.ACTION_DOWN -> {
                mIsPressedDown = true
            }

            MotionEvent.ACTION_UP -> {
                if(mIsPressedDown) {
                    interceptableClickListener?.onClick(this)
                    mIsPressedDown = false
                }
            }

        }

        return super.onTouchEvent(ev)
    }


}