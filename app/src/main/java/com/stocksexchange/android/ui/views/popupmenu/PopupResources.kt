package com.stocksexchange.android.ui.views.popupmenu

import android.graphics.drawable.Drawable
import com.arthurivanets.adapster.markers.ItemResources

class PopupResources(
    val colors: List<Int>,
    val drawables: List<Drawable?>
) : ItemResources {


    companion object {

        const val COLOR_ITEM_TEXT_COLOR = 0
        const val DRAWABLE_SEPARATOR = 0


        val STUB_RESOURCES = PopupResources(
            colors = listOf(),
            drawables = listOf()
        )


        fun newInstance(
            itemTextColor: Int,
            separatorDrawable: Drawable?
        ): PopupResources {
            val colors = listOf(itemTextColor)
            val drawables = listOf(separatorDrawable)

            return PopupResources(colors, drawables)
        }

    }


}