package com.stocksexchange.android.mappings

import com.stocksexchange.android.database.model.DatabaseAlertPrice
import com.stocksexchange.android.ui.alertprice.AlertPairItem
import com.stocksexchange.android.ui.alertprice.AlertPriceItem
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.AlertPriceComparison

fun AlertPrice.mapToDatabaseAlertPrice(): DatabaseAlertPrice {
    return DatabaseAlertPrice(
        id = id,
        currencyPairId =currencyPairId,
        currencyPairName = currencyPairName,
        comparisonType = comparisonType,
        price = price,
        active = active
    )
}


fun List<AlertPrice>.mapToDatabaseAlertPriceList(): List<DatabaseAlertPrice> {
    return map { it.mapToDatabaseAlertPrice() }
}


fun DatabaseAlertPrice.mapToAlertPrice(): AlertPrice {
    return AlertPrice(
        id = id,
        currencyPairId =currencyPairId,
        currencyPairName = currencyPairName,
        comparisonType = comparisonType,
        price = price,
        active = active
    )
}


fun List<DatabaseAlertPrice>.mapToAlertPriceList(): List<AlertPrice> {
    return map { it.mapToAlertPrice() }
}


fun AlertPairItem.mapToAlertPriceItem(): AlertPriceItem {
    return AlertPriceItem(this)
}


fun List<AlertPairItem>.mapToAlertPriceItemList(): List<AlertPriceItem> {
    return map { it.mapToAlertPriceItem() }
}


fun List<AlertPrice>.mapToAlertPriceElementList(): List<AlertPairItem> {
    return mutableListOf<AlertPairItem>().apply {
        this@mapToAlertPriceElementList.forEach {
            var pairIndex = -1
            var index = 0
            while ((pairIndex < 0) && (index < size)) {
                if (get(index).currencyPairId == it.currencyPairId) {
                    pairIndex = index
                }
                index++
            }

            if (pairIndex < 0) {
                if (it.comparisonType == AlertPriceComparison.LESS.title) {
                    add(AlertPairItem(it.currencyPairId, it.currencyPairName, it, null))
                } else {
                    add(AlertPairItem(it.currencyPairId, it.currencyPairName, null, it))
                }
            } else {
                if (it.comparisonType == AlertPriceComparison.LESS.title) {
                    get(pairIndex).lessAlertPrice = it
                } else {
                    get(pairIndex).moreAlertPrice = it
                }
            }
        }
    }
}