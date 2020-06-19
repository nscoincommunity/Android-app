package com.stocksexchange.api.model.rest

import com.stocksexchange.api.R

enum class FiatCurrency(
    val symbol: String,
    val symbolPosition: FiatCurrencySymbolPosition,
    val shouldAddExtraSpace: Boolean,
    val stringId: Int
) {


    AUD(
        symbol = "A$",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_aud
    ),

    BRL(
        symbol = "R$",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_brl
    ),

    CAD(
        symbol = "C$",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_cad
    ),

    CNY(
        symbol = "元",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_cny
    ),

    EUR(
        symbol = "€",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_eur
    ),

    INR(
        symbol = "₹",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_inr
    ),

    IDR(
        symbol = "Rp",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_idr
    ),

    JPY(
        symbol = "¥",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_jpy
    ),

    GBP(
        symbol = "£",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_gbp
    ),

    RUB(
        symbol = "\u20BD",
        symbolPosition = FiatCurrencySymbolPosition.END,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_rub
    ),

    KRW(
        symbol = "₩",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_krw
    ),

    UAH(
        symbol = "₴",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_uah
    ),

    USD(
        symbol = "$",
        symbolPosition = FiatCurrencySymbolPosition.START,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_usd
    ),

    VND(
        symbol = "₫",
        symbolPosition = FiatCurrencySymbolPosition.END,
        shouldAddExtraSpace = false,
        stringId = R.string.fiat_currency_vnd
    )


}