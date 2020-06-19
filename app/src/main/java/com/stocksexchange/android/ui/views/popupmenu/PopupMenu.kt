package com.stocksexchange.android.ui.views.popupmenu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.extensions.getDottedLineDrawable
import com.stocksexchange.core.utils.extensions.getLayoutInflater

/**
 * A custom popup menu.
 */
@SuppressLint("PrivateResource,RestrictedApi")
class PopupMenu private constructor(
    private val context: Context,
    private val gravity: Int
) {


    companion object {

        fun newInstance(
            context: Context,
            gravity: Int = (Gravity.END or Gravity.TOP)
        ): PopupMenu {
            return PopupMenu(context, gravity)
        }

    }


    private var mItemTextColor: Int = 0

    private var mPopupMenuItemMinWidth: Int = 0
    private var mPopupMenuItemMaxWidth: Int = 0

    private var mPopupMenuWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /**
     * A horizontal offset for this popup, in pixels.
     */
    var horizontalOffset: Int = 0

    /**
     * A vertical offset for this popup, in pixels.
     */
    var verticalOffset: Int = 0

    private var mItems: MutableList<BaseItem<*, *, *>> = mutableListOf()

    private var mSeparatorDrawable: Drawable? = null

    private lateinit var mAnchorView: View

    private var mPopupWindow: PopupWindow

    private var mAdapter: PopupRecyclerViewAdapter

    var onItemClickListener: ((PopupMenuItem) -> Unit)? = null




    init {
        mPopupWindow = PopupWindow(context, null, 0, 0)
        mPopupWindow.isFocusable = true

        with(context.resources) {
            mPopupWindow.elevation = getDimension(R.dimen.popup_menu_elevation)

            mPopupMenuItemMinWidth = getDimensionPixelSize(R.dimen.popup_menu_item_min_width)
            mPopupMenuItemMaxWidth = getDimensionPixelSize(R.dimen.popup_menu_item_max_width)
        }

        mAdapter = PopupRecyclerViewAdapter(context, mItems)
        mAdapter.setResources(PopupResources.STUB_RESOURCES)
        mAdapter.onItemClickListener = { _, item, _ ->
            onItemClickListener?.invoke(item)

            dismiss()
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun measureMenuWidth(): Int {
        val parent = FrameLayout(context)
        val layoutInflater = context.getLayoutInflater()
        val resources: PopupResources = mAdapter.resources!!

        val itemWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val itemHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        var item: BaseItem<Any, BaseItem.ViewHolder<Any>, ItemResources>
        var viewHolder: BaseItem.ViewHolder<*>

        var measuredItemWidth: Int
        var maxItemWidth: Int = mPopupMenuItemMinWidth

        for(i in 0 until mAdapter.itemCount) {
            item = (mAdapter.getItem(i)!! as BaseItem<Any, BaseItem.ViewHolder<Any>, ItemResources>)
            viewHolder = item.init(null, parent, layoutInflater, resources)

            item.bind(null, viewHolder, resources)
            viewHolder.itemView.measure(itemWidthMeasureSpec, itemHeightMeasureSpec)

            measuredItemWidth = viewHolder.itemView.measuredWidth

            if(measuredItemWidth >= mPopupMenuItemMaxWidth) {
                return mPopupMenuItemMaxWidth
            } else if(measuredItemWidth > maxItemWidth) {
                maxItemWidth = measuredItemWidth
            }
        }

        return maxItemWidth
    }


    private fun calculateMenuWidth() {
        val measuredMenuWidth = measureMenuWidth()
        val popupBackground = mPopupWindow.background
        val rectangle = Rect()

        mPopupMenuWidth = if(popupBackground != null) {
            popupBackground.getPadding(rectangle)
            (measuredMenuWidth + rectangle.left + rectangle.right)
        } else {
            measuredMenuWidth
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun measureMenuHeight(): Int {
        val parent = FrameLayout(context)
        val layoutInflater = context.getLayoutInflater()
        val resources: PopupResources = mAdapter.resources!!
        val itemWidthMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val maxMenuHeight: Int = mPopupWindow.getMaxAvailableHeight(mAnchorView, verticalOffset)
        var itemHeightMeasureSpec: Int

        var item: BaseItem<Any, BaseItem.ViewHolder<Any>, ItemResources>
        var viewHolder: BaseItem.ViewHolder<*>

        var itemView: View
        var childLayoutParams: ViewGroup.LayoutParams?
        var measuredHeight = 0

        for(i in 0 until mAdapter.itemCount) {
            item = (mAdapter.getItem(i)!! as BaseItem<Any, BaseItem.ViewHolder<Any>, ItemResources>)
            viewHolder = item.init(null, parent, layoutInflater, resources)

            item.bind(null, viewHolder, resources)

            itemView = viewHolder.itemView

            childLayoutParams = itemView.layoutParams

            if(childLayoutParams == null) {
                childLayoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                itemView.layoutParams = childLayoutParams
            }

            itemHeightMeasureSpec = if(childLayoutParams.height > 0) {
                View.MeasureSpec.makeMeasureSpec(childLayoutParams.height, View.MeasureSpec.EXACTLY)
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }

            itemView.measure(itemWidthMeasureSpec, itemHeightMeasureSpec)

            measuredHeight += itemView.measuredHeight

            if(childLayoutParams is ViewGroup.MarginLayoutParams) {
                measuredHeight += (childLayoutParams.topMargin + childLayoutParams.bottomMargin)
            }

            if(measuredHeight >= maxMenuHeight) {
                return maxMenuHeight
            }
        }

        return measuredHeight
    }


    fun show(anchorView: View) {
        mAnchorView = anchorView
        PopupWindowCompat.setWindowLayoutType(mPopupWindow, WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL)

        val recyclerView: RecyclerView = (View.inflate(context, R.layout.popup_menu, null) as RecyclerView)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mPopupWindow.contentView = recyclerView
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.width = mPopupMenuWidth
        mPopupWindow.height = measureMenuHeight()
        mPopupWindow.showAsDropDown(mAnchorView, horizontalOffset, verticalOffset, gravity)
    }


    fun dismiss() {
        mPopupWindow.dismiss()
        mPopupWindow.contentView = null
    }


    fun addItem(item: BaseItem<*, *, *>) {
        mAdapter.addItem(item)
        calculateMenuWidth()
    }


    private fun updateResources() {
        mAdapter.setResources(PopupResources.newInstance(
            itemTextColor = mItemTextColor,
            separatorDrawable = mSeparatorDrawable
        ))
    }


    fun setBackgroundColor(@ColorInt color: Int) {
        mPopupWindow.setBackgroundDrawable(color.toDrawable())
    }


    fun setItemTextColor(@ColorInt color: Int) {
        mItemTextColor = color
        updateResources()
    }


    fun setSeparatorColor(@ColorInt color: Int) {
        mSeparatorDrawable = context.getDottedLineDrawable(color)
        updateResources()
    }


    fun setItems(items: List<BaseItem<*, *, *>>) {
        mAdapter.items = items
        calculateMenuWidth()
    }


}