package com.stocksexchange.core

import android.os.Build

object Constants {

    const val CURRENCY_MARKET_SEPARATOR = "_"

    val API_VERSION = Build.VERSION.SDK_INT
    val AT_LEAST_KITKAT = (API_VERSION >= Build.VERSION_CODES.KITKAT)
    val AT_LEAST_LOLLIPOP = (API_VERSION >= Build.VERSION_CODES.LOLLIPOP)
    val AT_LEAST_MARSHMALLOW = (API_VERSION >= Build.VERSION_CODES.M)
    val AT_LEAST_NOUGAT_V1 = (API_VERSION >= Build.VERSION_CODES.N)
    val AT_LEAST_NOUGAT_V2 = (API_VERSION >= Build.VERSION_CODES.N_MR1)
    val AT_LEAST_OREO = (API_VERSION >= Build.VERSION_CODES.O)
    val AT_LEAST_PIE = (API_VERSION >= Build.VERSION_CODES.P)
    val AT_LEAST_10 = (API_VERSION >= Build.VERSION_CODES.Q)

}