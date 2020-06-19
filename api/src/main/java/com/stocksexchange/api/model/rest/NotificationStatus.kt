package com.stocksexchange.api.model.rest

import android.annotation.SuppressLint

enum class NotificationStatus {

    ENABLE,
    DISABLE;


    @SuppressLint("DefaultLocale")
    override fun toString(): String {
        return name.toLowerCase()
    }


}