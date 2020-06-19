package com.stocksexchange.core.utils.listeners

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * A listener that provides hooks to some of the most
 * common gestures.
 */
class GestureListener(private val listener: Listener) : GestureDetector.SimpleOnGestureListener() {


    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        listener.onSingleTap(event)

        return super.onSingleTapConfirmed(event)
    }


    override fun onDoubleTap(event: MotionEvent): Boolean {
        listener.onDoubleTap(event)

        return super.onDoubleTap(event)
    }


    override fun onFling(startEvent: MotionEvent, endEvent: MotionEvent,
                         velocityX: Float, velocityY: Float): Boolean {
        listener.onFling(startEvent, endEvent)

        val deltaX = (startEvent.x - endEvent.x)
        val deltaY = (startEvent.y - endEvent.y)

        detectHorizontalSwipes(deltaX, startEvent, endEvent)
        detectVerticalSwipes(deltaY, startEvent, endEvent)

        return super.onFling(startEvent, endEvent, velocityX, velocityY)
    }


    private fun detectHorizontalSwipes(deltaX: Float, startEvent: MotionEvent,
                                       endEvent: MotionEvent) {
        if(deltaX > 0f) {
            listener.onSwipedToLeft(startEvent, endEvent)
        } else {
            listener.onSwipedToRight(startEvent, endEvent)
        }
    }


    private fun detectVerticalSwipes(deltaY: Float, startEvent: MotionEvent,
                                     endEvent: MotionEvent) {
        if(deltaY > 0f) {
            listener.onSwipedToTop(startEvent, endEvent)
        } else {
            listener.onSwipedToBottom(startEvent, endEvent)
        }
    }


    interface Listener {

        fun onSingleTap(motionEvent: MotionEvent) {
            // Stub
        }

        fun onDoubleTap(motionEvent: MotionEvent) {
            // Stub
        }

        fun onFling(startEvent: MotionEvent, endEvent: MotionEvent) {
            // Stub
        }

        fun onSwipedToLeft(startEvent: MotionEvent, endEvent: MotionEvent) {
            // Stub
        }

        fun onSwipedToRight(startEvent: MotionEvent, endEvent: MotionEvent) {
            // Stub
        }

        fun onSwipedToTop(startEvent: MotionEvent, endEvent: MotionEvent) {
            // Stub
        }

        fun onSwipedToBottom(startEvent: MotionEvent, endEvent: MotionEvent) {
            // Stub
        }

    }


}