package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsTwitterItemModel(
    @SerializedName("name") val name: String,
    @SerializedName("twitter_name") val twitterName: String,
    @SerializedName("base_url") val baseUrl: String,
    @SerializedName("message") val message: String,
    @SerializedName("twit_published_timestamp") val twitPublishTimestamp: Long, // in seconds
    @SerializedName("img_urls") val _imageUrls: List<String>?
) : Parcelable {


    val hasImageUrls: Boolean
        get() = imageUrls.isNotEmpty()


    val publicationTimestampInMillis: Long
        get() = (twitPublishTimestamp * 1000L)


    val imageUrls: List<String>
        get() = (_imageUrls ?: listOf())


}