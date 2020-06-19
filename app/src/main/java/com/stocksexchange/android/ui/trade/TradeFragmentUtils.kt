package com.stocksexchange.android.ui.trade

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.*
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        selectedPrice = getDouble(ExtrasKeys.KEY_SELECTED_PRICE),
        selectedAmount = getDouble(ExtrasKeys.KEY_SELECTED_AMOUNT),
        tradeScreenOpener = getSerializableOrThrow(ExtrasKeys.KEY_TRADE_SCREEN_OPENER),
        selectedTradeType = getSerializableOrThrow(ExtrasKeys.KEY_SELECTED_TRADE_TYPE),
        selectedOrderType = getSerializableOrThrow(ExtrasKeys.KEY_SELECTED_ORDER_TYPE),
        currencyMarket = getParcelableOrThrow(ExtrasKeys.KEY_CURRENCY_MARKET)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        areWalletsInitialized = getOrThrow(PresenterStateKeys.KEY_ARE_WALLETS_INITIALIZED),
        isInitialPriceSet = getOrThrow(PresenterStateKeys.KEY_IS_INITIAL_PRICE_SET),
        selectedPrice = getOrThrow(PresenterStateKeys.KEY_SELECTED_PRICE),
        selectedAmount = getOrThrow(PresenterStateKeys.KEY_SELECTED_AMOUNT),
        tradeScreenOpener = getOrThrow(PresenterStateKeys.KEY_TRADE_SCREEN_OPENER),
        selectedTradeType = getOrThrow(PresenterStateKeys.KEY_SELECTED_TRADE_TYPE),
        selectedOrderType = getOrThrow(PresenterStateKeys.KEY_SELECTED_ORDER_TYPE),
        currencyMarket = getOrThrow(PresenterStateKeys.KEY_CURRENCY_MARKET),
        baseCurrencyWallet = getOrThrow(PresenterStateKeys.KEY_BASE_CURRENCY_WALLET),
        quoteCurrencyWallet = getOrThrow(PresenterStateKeys.KEY_QUOTE_CURRENCY_WALLET),
        performedCurrencyMarketActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_CURRENCY_MARKET_ACTIONS),
        performedOrderActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS),
        tradeTypeValuableFormDataMap = getOrThrow(PresenterStateKeys.KEY_TRADE_TYPE_VALUABLE_FORM_DATA_MAP)
    )
}


internal object ExtrasKeys {

    const val KEY_SELECTED_PRICE = "selected_price"
    const val KEY_SELECTED_AMOUNT = "selected_amount"
    const val KEY_TRADE_SCREEN_OPENER = "trade_screen_opener"
    const val KEY_SELECTED_TRADE_TYPE = "selected_trade_type"
    const val KEY_SELECTED_ORDER_TYPE = "selected_order_type"
    const val KEY_CURRENCY_MARKET = "currency_market"

}


internal object PresenterStateKeys {

    const val KEY_ARE_WALLETS_INITIALIZED = "are_wallets_initialized"
    const val KEY_IS_INITIAL_PRICE_SET = "is_initial_price_set"
    const val KEY_SELECTED_PRICE = "selected_price"
    const val KEY_SELECTED_AMOUNT = "selected_amount"
    const val KEY_TRADE_SCREEN_OPENER = "trade_screen_opener"
    const val KEY_SELECTED_TRADE_TYPE = "selected_trade_type"
    const val KEY_SELECTED_ORDER_TYPE = "selected_order_type"
    const val KEY_CURRENCY_MARKET = "currency_market"
    const val KEY_BASE_CURRENCY_WALLET = "base_currency_wallet"
    const val KEY_QUOTE_CURRENCY_WALLET = "quote_currency_wallet"
    const val KEY_PERFORMED_CURRENCY_MARKET_ACTIONS = "performed_currency_market_actions"
    const val KEY_PERFORMED_ORDER_ACTIONS = "performed_order_actions"
    const val KEY_TRADE_TYPE_VALUABLE_FORM_DATA_MAP = "trade_type_valuable_form_data_map"

}


internal data class Extras(
    val selectedPrice: Double,
    val selectedAmount: Double,
    val tradeScreenOpener: TradeScreenOpener,
    val selectedTradeType: TradeType,
    val selectedOrderType: OrderType,
    val currencyMarket: CurrencyMarket
)


internal data class PresenterState(
    val areWalletsInitialized: Boolean,
    val isInitialPriceSet: Boolean,
    val selectedPrice: Double,
    val selectedAmount: Double,
    val tradeScreenOpener: TradeScreenOpener,
    val selectedTradeType: TradeType,
    val selectedOrderType: OrderType,
    val currencyMarket: CurrencyMarket,
    val baseCurrencyWallet: Wallet,
    val quoteCurrencyWallet: Wallet,
    val performedCurrencyMarketActions: PerformedCurrencyMarketActions,
    val performedOrderActions: PerformedOrderActions,
    val tradeTypeValuableFormDataMap: MutableMap<TradeType, ValuableTradeFormData>
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_ARE_WALLETS_INITIALIZED, state.areWalletsInitialized)
    save(PresenterStateKeys.KEY_IS_INITIAL_PRICE_SET, state.isInitialPriceSet)
    save(PresenterStateKeys.KEY_SELECTED_PRICE, state.selectedPrice)
    save(PresenterStateKeys.KEY_SELECTED_AMOUNT, state.selectedAmount)
    save(PresenterStateKeys.KEY_TRADE_SCREEN_OPENER, state.tradeScreenOpener)
    save(PresenterStateKeys.KEY_SELECTED_TRADE_TYPE, state.selectedTradeType)
    save(PresenterStateKeys.KEY_SELECTED_ORDER_TYPE, state.selectedOrderType)
    save(PresenterStateKeys.KEY_CURRENCY_MARKET, state.currencyMarket)
    save(PresenterStateKeys.KEY_BASE_CURRENCY_WALLET, state.baseCurrencyWallet)
    save(PresenterStateKeys.KEY_QUOTE_CURRENCY_WALLET, state.quoteCurrencyWallet)
    save(PresenterStateKeys.KEY_PERFORMED_CURRENCY_MARKET_ACTIONS, state.performedCurrencyMarketActions)
    save(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS, state.performedOrderActions)
    save(PresenterStateKeys.KEY_TRADE_TYPE_VALUABLE_FORM_DATA_MAP, state.tradeTypeValuableFormDataMap)
}


fun TradeFragment.Companion.newArgs(
    tradeScreenOpener: TradeScreenOpener,
    tradeType: TradeType,
    currencyMarket: CurrencyMarket,
    selectedPrice: Double,
    orderType: OrderType = OrderType.LIMIT,
    selectedAmount: Double
): Bundle {
    return bundleOf(
        ExtrasKeys.KEY_SELECTED_PRICE to selectedPrice,
        ExtrasKeys.KEY_TRADE_SCREEN_OPENER to tradeScreenOpener,
        ExtrasKeys.KEY_SELECTED_TRADE_TYPE to tradeType,
        ExtrasKeys.KEY_SELECTED_ORDER_TYPE to orderType,
        ExtrasKeys.KEY_CURRENCY_MARKET to currencyMarket,
        ExtrasKeys.KEY_SELECTED_AMOUNT to selectedAmount
    )
}