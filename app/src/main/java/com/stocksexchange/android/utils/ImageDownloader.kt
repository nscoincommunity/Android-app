package com.stocksexchange.android.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageDownloader(private val picasso: Picasso) {


    fun downloadImage(builder: Builder) {
        val requestCreator = picasso.load(builder.imageUrl)

        if(builder.shouldCenterCrop) {
            requestCreator.centerCrop()
        }

        if(builder.shouldCenterInside) {
            requestCreator.centerInside()
        }

        if(builder.hasSize) {
            requestCreator.resize(builder.size, builder.size)
        } else if(builder.hasWidth && builder.hasHeight) {
            requestCreator.resize(builder.width, builder.height)
        }

        if(builder.hasPlaceholderImage) {
            requestCreator.placeholder(builder.placeholderImage!!)
        }

        if(builder.hasErrorImage) {
            requestCreator.error(builder.errorImage!!)
        }

        requestCreator.into(builder.destination)
    }




    data class Builder(
        internal var shouldCenterCrop: Boolean = false,
        internal var shouldCenterInside: Boolean = false,
        internal var width: Int = -1,
        internal var height: Int = -1,
        internal var size: Int = -1,
        internal var imageUrl: String = "",
        internal var placeholderImage: Drawable? = null,
        internal var errorImage: Drawable? = null,
        internal var destination: ImageView? = null
    ) {

        val hasWidth: Boolean
            get() = (width > 0)

        val hasHeight: Boolean
            get() = (height > 0)

        val hasSize: Boolean
            get() = (size > 0)

        val hasImageUrl: Boolean
            get() = imageUrl.isNotEmpty()

        val hasPlaceholderImage: Boolean
            get() = (placeholderImage != null)

        val hasErrorImage: Boolean
            get() = (errorImage != null)

        val hasDestination: Boolean
            get() = (destination != null)


        fun centerCrop(): Builder {
            this.shouldCenterCrop = true
            return this
        }

        fun centerInside(): Builder {
            this.shouldCenterInside = true
            return this
        }

        fun width(width: Int): Builder {
            require(width > 0) { "width <= 0" }

            this.width = width
            return this
        }

        fun height(height: Int): Builder {
            require(height > 0) { "height <= 0" }

            this.height = height
            return this
        }

        fun size(size: Int): Builder {
            require(size > 0) { "size > 0" }

            this.size = size
            return this
        }

        fun placeholderImage(drawable: Drawable?): Builder {
            this.placeholderImage = drawable
            return this
        }

        fun errorImage(drawable: Drawable?): Builder {
            this.errorImage = drawable
            return this
        }

        fun imageUrl(url: String): Builder {
            require(url.isNotBlank()) { "The image url is blank" }

            this.imageUrl = url
            return this
        }

        fun destination(destination: ImageView): Builder {
            this.destination = destination
            return this
        }

        fun build(): Builder {
            if(!hasImageUrl) {
                throw IllegalArgumentException("Image url is absent")
            }

            if(!hasDestination) {
                throw IllegalArgumentException("Destination is absent")
            }

            return this
        }

    }


}