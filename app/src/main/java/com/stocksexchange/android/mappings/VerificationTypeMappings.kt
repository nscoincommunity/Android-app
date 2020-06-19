package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.VerificationType
import com.stocksexchange.android.ui.verification.selection.VerificationSelectionItem

fun VerificationType.mapToVerificationSelectionItem(): VerificationSelectionItem {
    return VerificationSelectionItem(this)
}


fun List<VerificationType>.mapToVerificationSelectionItemList(): List<VerificationSelectionItem> {
    return map { it.mapToVerificationSelectionItem() }
}