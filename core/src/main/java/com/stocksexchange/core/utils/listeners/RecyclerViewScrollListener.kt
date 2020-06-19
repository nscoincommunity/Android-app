package com.stocksexchange.core.utils.listeners

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * An implementation of the scroll listener of the RecyclerView
 * that provides callbacks about major events.
 */
class RecyclerViewScrollListener private constructor() : RecyclerView.OnScrollListener() {


    private var mShouldNotifyOnReachingEndsRepeatedly: Boolean = false

    private var mFirstVisiblePosition: Int = -1
    private var mPreviousFirstVisiblePosition: Int = -1
    private var mLastVisiblePosition: Int = -1
    private var mPreviousLastVisiblePosition: Int = -1
    private var mVisibleChildrenCount: Int = 0
    private var mTotalItemCount: Int = 0
    private var mPreviousTotalItemCount: Int = 0

    private var mStateListener: StateListener? = null

    private var mChild: View? = null




    constructor(stateListener: StateListener,
                shouldNotifyOnReachingEndsRepeatedly: Boolean = false): this() {
        mShouldNotifyOnReachingEndsRepeatedly = shouldNotifyOnReachingEndsRepeatedly
        mStateListener = stateListener
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // Stub
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if(dy > 0) {
            mStateListener?.onScrolledDownwards(recyclerView, dy)

            mVisibleChildrenCount = recyclerView.childCount
            mPreviousFirstVisiblePosition = getFirstVisiblePosition(recyclerView)
            mLastVisiblePosition = getLastVisiblePosition(recyclerView)
            mTotalItemCount = recyclerView.adapter!!.itemCount

            if((mLastVisiblePosition == (mTotalItemCount - 1)) &&
                ((mLastVisiblePosition != mPreviousLastVisiblePosition) || shouldNotifyOnReachingEnds())) {
                mChild = recyclerView.getChildAt(mVisibleChildrenCount - 1)
                mPreviousLastVisiblePosition = mLastVisiblePosition
                mPreviousTotalItemCount = mTotalItemCount

                mStateListener?.onBottomReached(
                    recyclerView,
                    ((mChild!!.top + mChild!!.measuredHeight) == recyclerView.measuredHeight)
                )
            } else if(mLastVisiblePosition == (mTotalItemCount / 2)) {
                mStateListener?.onMiddleReached(recyclerView, Direction.DOWNWARDS)
            }
        } else if (dy < 0) {
            mStateListener?.onScrolledUpwards(recyclerView, dy)

            mVisibleChildrenCount = recyclerView.childCount
            mPreviousFirstVisiblePosition = getFirstVisiblePosition(recyclerView)
            mLastVisiblePosition = getLastVisiblePosition(recyclerView)
            mTotalItemCount = recyclerView.adapter!!.itemCount

            if((mFirstVisiblePosition == 0) &&
                ((mFirstVisiblePosition != mPreviousFirstVisiblePosition) || shouldNotifyOnReachingEnds())) {
                mChild = recyclerView.getChildAt(0)
                mPreviousFirstVisiblePosition = mFirstVisiblePosition
                mPreviousTotalItemCount = mTotalItemCount

                mStateListener?.onTopReached(recyclerView, (mChild!!.top == 0))
            } else if(mFirstVisiblePosition == (mTotalItemCount / 2)) {
                mStateListener?.onMiddleReached(recyclerView, Direction.UPWARDS)
            }
        }
    }


    private fun getFirstVisiblePosition(recyclerView: RecyclerView): Int {
        return if(recyclerView.childCount == 0) {
            -1
        } else {
            recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))
        }
    }


    private fun getLastVisiblePosition(recyclerView: RecyclerView): Int {
        return if(recyclerView.childCount == 0) {
            -1
        } else {
            recyclerView.getChildAdapterPosition(recyclerView.getChildAt(recyclerView.childCount - 1))
        }
    }


    private fun shouldNotifyOnReachingEnds(): Boolean {
        if(mShouldNotifyOnReachingEndsRepeatedly) {
            return true
        }

        return (mPreviousTotalItemCount != mTotalItemCount)
    }


    interface StateListener {

        fun onScrolledUpwards(recyclerView: RecyclerView, deltaY: Int)

        fun onScrolledDownwards(recyclerView: RecyclerView, deltaY: Int)

        fun onTopReached(recyclerView: RecyclerView, reachedCompletely: Boolean)

        fun onMiddleReached(recyclerView: RecyclerView, direction: Direction)

        fun onBottomReached(recyclerView: RecyclerView, reachedCompletely: Boolean)

    }


    enum class Direction {

        UNSPECIFIED,
        UPWARDS,
        DOWNWARDS

    }


}