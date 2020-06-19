package com.stocksexchange.android.mappings

import com.stocksexchange.android.model.LanguageItemModel
import com.stocksexchange.android.ui.language.LanguageItem

fun LanguageItemModel.mapToLanguageItem(): LanguageItem {
    return LanguageItem(this)
}


fun List<LanguageItemModel>.mapToLanguageItemList(): List<LanguageItem> {
    return map { it.mapToLanguageItem() }
}