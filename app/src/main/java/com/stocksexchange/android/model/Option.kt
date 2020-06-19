package com.stocksexchange.android.model

data class Option(
    val id: Int,
    val iconDrawableId: Int,
    val title: String,
    val status: Status = Status.DEFAULT
) {


    enum class Status {

        DEFAULT,
        HIGHLIGHTED

    }


}