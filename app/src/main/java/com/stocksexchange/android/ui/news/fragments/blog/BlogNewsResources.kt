package com.stocksexchange.android.ui.news.fragments.blog

import android.graphics.drawable.Drawable
import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.ImageDownloader
import com.stocksexchange.core.formatters.TimeFormatter

class BlogNewsResources(
    val settings: Settings,
    val timeFormatter: TimeFormatter,
    val imageDownloader: ImageDownloader,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageError: Drawable
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings,
                        timeFormatter: TimeFormatter,
                        imageDownloader: ImageDownloader,
                        imageWidth: Int,
                        imageHeight: Int,
                        imageError: Drawable
        ): BlogNewsResources {
            return BlogNewsResources(
                settings = settings,
                timeFormatter = timeFormatter,
                imageDownloader = imageDownloader,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                imageError = imageError
            )
        }

    }


}