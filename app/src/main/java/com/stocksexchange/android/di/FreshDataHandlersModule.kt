package com.stocksexchange.android.di

import com.stocksexchange.android.data.repositories.freshdatahandlers.concrete.*
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.*
import org.koin.dsl.module

val freshDataHandlersModule = module {

    factory<SimpleFreshDataHandler> { SimpleFreshDataHandlerImpl() }
    factory<CandleSticksFreshDataHandler> { CandleSticksFreshDataHandlerImpl() }
    factory<InboxFreshDataHandler> { InboxFreshDataHandlerImpl() }
    factory<OrderbookFreshDataHandler> { OrderbookFreshDataHandlerImpl() }
    factory<OrdersFreshDataHandler> { OrdersFreshDataHandlerImpl() }
    factory<PriceAlertFreshDataHandler> { PriceAlertFreshDataHandlerImpl() }
    factory<TradeHistoryFreshDataHandler> { TradeHistoryFreshDataHandlerImpl() }
    factory<TradingFeesFreshDataHandler> { TradingFeesFreshDataHandlerImpl() }

}