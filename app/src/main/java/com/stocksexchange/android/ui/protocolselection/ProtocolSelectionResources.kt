package com.stocksexchange.android.ui.protocolselection

import android.content.Context
import android.graphics.drawable.Drawable
import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.ImageDownloader
import com.stocksexchange.core.utils.extensions.dimenInPx

class ProtocolSelectionResources(
    val currencyId: Int,
    val settings: Settings,
    val dimensions: List<Int>,
    val imageDownloader: ImageDownloader,
    val errorImage: Drawable?
) : ItemResources {


    companion object {

        const val DIMENSION_ICON_SIZE = 0


        fun newInstance(context: Context,
                        currencyId: Int,
                        settings: Settings,
                        imageDownloader: ImageDownloader,
                        errorImage: Drawable?): ProtocolSelectionResources {
            val dimensions = listOf(
                context.dimenInPx(R.dimen.protocol_selection_item_icon_size)
            )

            return ProtocolSelectionResources(
                currencyId = currencyId,
                settings = settings,
                dimensions = dimensions,
                imageDownloader = imageDownloader,
                errorImage = errorImage
            )
        }

    }


}