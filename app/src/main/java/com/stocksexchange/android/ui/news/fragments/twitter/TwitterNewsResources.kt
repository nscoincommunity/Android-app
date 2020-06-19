package com.stocksexchange.android.ui.news.fragments.twitter

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.ImageDownloader
import com.stocksexchange.core.formatters.TimeFormatter

class TwitterNewsResources(
    val settings: Settings,
    val timeFormatter: TimeFormatter,
    val imageDownloader: ImageDownloader,
    val imageWidth: Int,
    val imageHeight: Int
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings,
                        timeFormatter: TimeFormatter,
                        imageDownloader: ImageDownloader,
                        imageWidth: Int,
                        imageHeight: Int
        ): TwitterNewsResources {
            return TwitterNewsResources(
                settings = settings,
                timeFormatter = timeFormatter,
                imageDownloader = imageDownloader,
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        }

    }


}