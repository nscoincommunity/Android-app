package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.events.*
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.model.PerformedOrderActions
import org.greenrobot.eventbus.EventBus

/**
 * Handles the [CurrencyMarketEvent] by storing it inside the [PerformedCurrencyMarketActions].
 *
 * @param event The event to handle
 * @param performedActions The container to store the event into
 */
fun handleCurrencyMarketEvent(event: CurrencyMarketEvent, performedActions: PerformedCurrencyMarketActions) {
    when(event.action) {

        CurrencyMarketEvent.Action.UPDATED -> {
            performedActions.addUpdatedCurrencyMarket(event.attachment)
        }

        CurrencyMarketEvent.Action.FAVORITED -> {
            performedActions.addFavoriteCurrencyMarket(event.attachment)
            performedActions.removeUnfavoriteCurrencyMarket(event.attachment)
        }

        CurrencyMarketEvent.Action.UNFAVORITED -> {
            performedActions.addUnfavoriteCurrencyMarket(event.attachment)
            performedActions.removeFavoriteCurrencyMarket(event.attachment)
        }

    }
}


/**
 * Handles the [OrderEvent] by storing it inside the [PerformedOrderActions].
 *
 * @param event The event to handle
 * @param performedActions The container to store the event into
 */
fun handleOrderEvent(event: OrderEvent, performedActions: PerformedOrderActions) {
    when(event.action) {

        OrderEvent.Action.FILLED_AMOUNT_UPDATED -> {
            performedActions.addFilledAmountChangedOrder(event.attachment)
        }

        OrderEvent.Action.STATUS_UPDATED -> {
            performedActions.addStatusChangedOrder(event.attachment)
        }

    }
}


/**
 * Handles [PerformedCurrencyMarketActionsEvent] by basically posting the appropriate
 * [CurrencyMarketEvent] events.
 *
 * @param performedActionsEvent The event to handle
 * @param consumerCount The consumer count for market events
 */
fun handlePerformedCurrencyMarketActionsEvent(
    performedActionsEvent: PerformedCurrencyMarketActionsEvent,
    consumerCount: Int = 0
) {
    val sourceTag = performedActionsEvent.sourceTag
    val attachment = performedActionsEvent.attachment
    val sendEvent: ((CurrencyMarketEvent) -> Unit) = {
        EventBus.getDefault().postSticky(it)
    }

    if(attachment.hasUpdatedCurrencyMarkets()) {
        for(currencyMarket in attachment.updatedCurrencyMarketsMap.values) {
            sendEvent(CurrencyMarketEvent.update(currencyMarket, sourceTag, consumerCount))
        }
    }

    if(attachment.hasFavoriteCurrencyMarkets()) {
        for(currencyMarket in attachment.favoriteCurrencyMarketsMap.values) {
            sendEvent(CurrencyMarketEvent.favorite(currencyMarket, sourceTag, consumerCount))
        }
    }

    if(attachment.hasUnfavoriteCurrencyMarkets()) {
        for(currencyMarket in attachment.unfavoriteCurrencyMarketsMap.values) {
            sendEvent(CurrencyMarketEvent.unfavorite(currencyMarket, sourceTag, consumerCount))
        }
    }
}


/**
 * Handles [PerformedWalletActionsEvent] by basically posting the appropriate
 * [WalletEvent] events.
 *
 * @param performedActionsEvent The event to handle
 */
fun handlePerformedWalletActionsEvent(
    performedActionsEvent: PerformedWalletActionsEvent
) {
    val sourceTag = performedActionsEvent.sourceTag
    val attachment = performedActionsEvent.attachment
    val sendEvent: ((WalletEvent) -> Unit) = {
        EventBus.getDefault().postSticky(it)
    }

    if(attachment.hasIdCreatedWallets()) {
        for(wallet in attachment.idCreatedWalletsMap.values) {
            sendEvent(WalletEvent.createId(wallet, sourceTag))
        }
    }

    if(attachment.hasBalanceChangedWallets()) {
        for(wallet in attachment.balanceChangedWalletsMap.values) {
            sendEvent(WalletEvent.updateBalance(wallet, sourceTag))
        }
    }
}


/**
 * Handles [PerformedOrderActionsEvent] by basically posting the appropriate
 * [OrderEvent] events.
 *
 * @param performedActionsEvent The event to handle
 * @param consumerCount The consumer count for order events
 */
fun handlePerformedOrderActionsEvent(
    performedActionsEvent: PerformedOrderActionsEvent,
    consumerCount: Int = 0,
    stickyMode: Boolean = false
) {
    val sourceTag = performedActionsEvent.sourceTag
    val attachment = performedActionsEvent.attachment
    val sendEvent: ((OrderEvent) -> Unit) = {
        if(stickyMode) {
            EventBus.getDefault().postSticky(it)
        } else {
            EventBus.getDefault().post(it)
        }
    }

    if(attachment.hasStatusChangedOrders()) {
        for(order in attachment.statusChangedOrdersMap.values) {
            sendEvent(OrderEvent.updateStatus(order, sourceTag, consumerCount))
        }
    }

    if(attachment.hasFilledAmountChangedOrders()) {
        for(order in attachment.filledAmountChangedOrdersMap.values) {
            sendEvent(OrderEvent.updateFilledAmount(order, sourceTag, consumerCount))
        }
    }
}