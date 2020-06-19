package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.android.database.model.DatabaseInbox
import com.stocksexchange.android.ui.inbox.InboxItem

fun Inbox.mapToDatabaseDeposit(): DatabaseInbox {
    return DatabaseInbox(
        id = id,
        title = title,
        desc = desc,
        date = date,
        channel = channel,
        readAt = readAt,
        coin = coin,
        coinFullName = coinFullName,
        amount = amount,
        orderId = orderId,
        currencyPair = currencyPair,
        type = type,
        orderAmount = orderAmount,
        price = price,
        expectedAmount = expectedAmount,
        fee = fee,
        inOrders = inOrders,
        ip = ip,
        location = location,
        browser = browser,
        browserVersion = browserVersion,
        device = device,
        platform = platform
    )
}


fun List<Inbox>.mapToDatabaseInboxList(): List<DatabaseInbox> {
    return map { it.mapToDatabaseDeposit() }
}


fun DatabaseInbox.mapToInbox(): Inbox {
    return Inbox(
        id = id,
        title = title,
        desc = desc,
        date = date,
        channel = channel,
        readAt = readAt,
        coin = coin,
        coinFullName = coinFullName,
        amount = amount,
        orderId = orderId,
        currencyPair = currencyPair,
        type = type,
        orderAmount = orderAmount,
        price = price,
        expectedAmount = expectedAmount,
        fee = fee,
        inOrders = inOrders,
        ip = ip,
        location = location,
        browser = browser,
        browserVersion = browserVersion,
        device = device,
        platform = platform
    )
}


fun List<DatabaseInbox>.mapToInboxList(): List<Inbox> {
    return map { it.mapToInbox() }
}


fun Inbox.mapToInboxItem(): InboxItem {
    return InboxItem(this)
}


fun List<Inbox>.mapToInboxItemList(): List<InboxItem> {
    return map { it.mapToInboxItem() }
}