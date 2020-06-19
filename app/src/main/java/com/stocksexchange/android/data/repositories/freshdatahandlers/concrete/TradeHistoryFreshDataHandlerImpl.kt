package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.TradeHistoryFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters

class TradeHistoryFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<TradeHistoryParameters>(),
    TradeHistoryFreshDataHandler