package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.android.ui.protocolselection.ProtocolSelectionItem

fun Protocol.mapToProtocolSelectionItem(): ProtocolSelectionItem {
    return ProtocolSelectionItem(this)
}


fun List<Protocol>.mapToProtocolSelectionItemList(): List<ProtocolSelectionItem> {
    return map { it.mapToProtocolSelectionItem() }
}