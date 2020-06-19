package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Inbox(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("date") val date: String,
    @SerializedName("channel") val channel: String?,
    @SerializedName("read_at") val readAt: String?,
    @SerializedName("coin") val coin: String?,
    @SerializedName("coin_full_name") val coinFullName: String?,
    @SerializedName("amount") val amount: String?,
    @SerializedName("order_id") val orderId: String?,
    @SerializedName("currency_pair") val currencyPair: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("order_amount") val orderAmount: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("expected_amount") val expectedAmount: String?,
    @SerializedName("fee") val fee: String?,
    @SerializedName("in_orders") val inOrders: String?,
    @SerializedName("ip") val ip: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("browser") val browser: String?,
    @SerializedName("browser_version") val browserVersion: String?,
    @SerializedName("device") val device: String?,
    @SerializedName("platform") val platform: String?
) : Parcelable {


    companion object {

        val STUB_INBOX = Inbox(
            id = "-1",
            title = "",
            desc = "",
            date = "",
            channel = "",
            readAt = null,
            coin = null,
            coinFullName = null,
            amount = null,
            orderId = null,
            currencyPair = null,
            type = null,
            orderAmount = null,
            price = null,
            expectedAmount = null,
            fee = null,
            inOrders = null,
            ip = null,
            location = null,
            browser = null,
            browserVersion = null,
            device = null,
            platform = null
        )

    }


    @IgnoredOnParcel
    private var itemDetailsVisible = false


    @IgnoredOnParcel
    private var itemRead = false




    fun hideItemDetails() {
        itemDetailsVisible = false
    }


    fun showItemDetails() {
        itemDetailsVisible = true
    }


    fun revertItemDetails() {
        itemDetailsVisible = !itemDetailsVisible
    }


    fun isItemDetailsShown(): Boolean = itemDetailsVisible


    fun isItemRead(): Boolean {
        if (readAt == null || readAt.isEmpty()) {
            return itemRead
        }
        return true
    }


    fun setItemRead() {
        itemRead = true
    }


}