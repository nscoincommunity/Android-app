package com.stocksexchange.core.utils

import android.graphics.Bitmap
import java.io.Serializable
import kotlin.math.ceil

object BitmapUtils {


    fun resizeBitmapBasedOnThresholdSize(originalBitmap: Bitmap, thresholdSize: Size, recycleOriginalBitmap: Boolean,
                                         centerCrop: Boolean): Bitmap? {
        val bitmapSize = Size(originalBitmap.width, originalBitmap.height)

        if(isBitmapSizeSatisfiable(bitmapSize, thresholdSize)) {
            return originalBitmap
        }

        val resizedBitmap = if(bitmapSize.isLandscape) {
            resizeBitmapBasedOnWidth(originalBitmap, thresholdSize, centerCrop)
        } else {
            resizeBitmapBasedOnHeight(originalBitmap, thresholdSize, centerCrop)
        }

        if((originalBitmap != resizedBitmap) && recycleOriginalBitmap) {
            originalBitmap.recycle()
        }

        return resizedBitmap
    }


    private fun isBitmapSizeSatisfiable(bitmapSize: Size, thresholdSize: Size): Boolean {
        return ((bitmapSize.width <= thresholdSize.width) && (bitmapSize.height <= thresholdSize.height))
    }


    fun resizeBitmapBasedOnWidth(bitmap: Bitmap, desiredSize: Size, centerCrop: Boolean): Bitmap? {
        return resizeBitmap(ScalingType.BASED_ON_WIDTH, bitmap, desiredSize, centerCrop)
    }


    fun resizeBitmapBasedOnHeight(bitmap: Bitmap, desiredSize: Size, centerCrop: Boolean): Bitmap? {
        return resizeBitmap(ScalingType.BASED_ON_HEIGHT, bitmap, desiredSize, centerCrop)
    }


    fun resizeBitmap(scalingType: ScalingType, bitmap: Bitmap, desiredSize: Size,
                     centerCrop: Boolean): Bitmap? {
        if(areParametersInvalid(bitmap, desiredSize)) {
            return null
        }

        if(isBitmapAlreadyOfDesiredSize(bitmap, desiredSize)) {
            return bitmap.copy(bitmap.config, bitmap.isMutable)
        }

        var scaledSize = Size.scale(
                scalingType = scalingType,
                originalWidth = bitmap.width,
                originalHeight = bitmap.height,
                value = when (scalingType) {
                    ScalingType.BASED_ON_WIDTH -> desiredSize.width
                    ScalingType.BASED_ON_HEIGHT -> desiredSize.height
                }
        )

        if(shouldReadjustScaledSizeForCropping(scaledSize, desiredSize) && centerCrop) {
            scaledSize = readjustScaledSizeForCropping(scalingType, scaledSize, desiredSize)
        }

        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            scaledSize.width,
            scaledSize.height,
            true
        )

        if(shouldNotCropBitmap(scaledSize, desiredSize) || !centerCrop) {
            return resizedBitmap
        }

        val croppedBitmap = Bitmap.createBitmap(
            resizedBitmap,
            ((scaledSize.width - desiredSize.width) / 2),
            ((scaledSize.height - desiredSize.height) / 2),
            desiredSize.width,
            desiredSize.height
        )

        if((resizedBitmap != bitmap) && (resizedBitmap != croppedBitmap)) {
            resizedBitmap.recycle()
        }

        return croppedBitmap
    }


    private fun areParametersInvalid(bitmap: Bitmap, desiredSize: Size): Boolean {
        return ((bitmap.width == 0) || (bitmap.height == 0) || (desiredSize.width == 0) || (desiredSize.height == 0))
    }


    private fun isBitmapAlreadyOfDesiredSize(bitmap: Bitmap, desiredSize: Size): Boolean {
        return ((bitmap.width == desiredSize.width) && (bitmap.height == desiredSize.height))
    }


    private fun shouldReadjustScaledSizeForCropping(scaledSize: Size, desiredSize: Size): Boolean {
        return ((scaledSize.width < desiredSize.width) || (scaledSize.height < desiredSize.height))
    }


    private fun readjustScaledSizeForCropping(scalingType: ScalingType, scaledSize: Size, desiredSize: Size): Size {
        val scaleRatio: Float = when(scalingType) {
            ScalingType.BASED_ON_WIDTH -> {
                val initialScaleRatio = (desiredSize.height.toFloat() / scaledSize.height)

                if((scaledSize.width * initialScaleRatio) < desiredSize.width) {
                    initialScaleRatio * (desiredSize.width / (scaledSize.width * initialScaleRatio))
                } else {
                    initialScaleRatio
                }
            }

            ScalingType.BASED_ON_HEIGHT -> {
                val initialScaleRatio = (desiredSize.width.toFloat() / scaledSize.width)

                if((scaledSize.height * initialScaleRatio) < desiredSize.height) {
                    initialScaleRatio * (desiredSize.height / (scaledSize.height * initialScaleRatio))
                } else {
                    initialScaleRatio
                }
            }
        }

        return Size(
                width = ceil(scaledSize.width * scaleRatio).toInt(),
                height = ceil(scaledSize.height * scaleRatio).toInt()
        )
    }


    private fun shouldNotCropBitmap(scaledSize: Size, desiredSize: Size): Boolean {
        return (scaledSize == desiredSize)
    }


    data class Size(val width: Int = 0, val height: Int = 0) : Serializable {


        companion object {


            fun scale(scalingType: ScalingType, originalWidth: Int,
                      originalHeight: Int, value: Int): Size {
                return scale(scalingType, Size(originalWidth, originalHeight), value)
            }


            fun scale(scalingType: ScalingType, originalSize: Size, value: Int): Size {
                val scaleRatio: Float = when(scalingType) {
                    ScalingType.BASED_ON_WIDTH -> (originalSize.width.toFloat() / value)
                    ScalingType.BASED_ON_HEIGHT -> (originalSize.height.toFloat() / value)
                }

                return Size(
                        width = (originalSize.width / scaleRatio).toInt(),
                        height = (originalSize.height / scaleRatio).toInt()
                )
            }

        }


        val isPortrait: Boolean
            get() = (height > width)


        val isLandscape: Boolean
            get() = (width > height)


        val isSquare: Boolean
            get() = (width == height)


    }


    enum class ScalingType {

        BASED_ON_WIDTH,
        BASED_ON_HEIGHT

    }


}