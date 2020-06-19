package com.stocksexchange.android.mappings

import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.ui.help.HelpItem

fun HelpItemModel.mapToHelpItem(): HelpItem {
    return HelpItem(this)
}


fun List<HelpItemModel>.mapToHelpItemList(): List<HelpItem> {
    return map { it.mapToHelpItem() }
}