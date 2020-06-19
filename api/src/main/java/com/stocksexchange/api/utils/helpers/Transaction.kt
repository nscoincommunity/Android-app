package com.stocksexchange.api.utils.helpers

private const val INTERNAL_TRANSACTION_ID_STARTER = "payment inside"
private const val BLOCK_EXPLORER_URL_TRANSACTION_ID_PLACEHOLDER = ":tx_hash"

fun isTransactionInternal(transactionExplorerId: String?): Boolean {
    return ((transactionExplorerId != null) &&
            transactionExplorerId.startsWith(INTERNAL_TRANSACTION_ID_STARTER))
}


fun getTransactionExplorerUrl(blockExplorerUrl: String, transactionExplorerId: String): String {
    return if(blockExplorerUrl.contains(BLOCK_EXPLORER_URL_TRANSACTION_ID_PLACEHOLDER)) {
        blockExplorerUrl.replace(
            oldValue = BLOCK_EXPLORER_URL_TRANSACTION_ID_PLACEHOLDER,
            newValue = transactionExplorerId
        )
    } else {
        (blockExplorerUrl + transactionExplorerId)
    }
}


fun stripAdditionalParameterName(parameterName: String): String {
    return parameterName.replace("\\(.*?\\)".toRegex(), "").trim()
}