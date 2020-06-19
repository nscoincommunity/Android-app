package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.TradingFeesFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters

class TradingFeesFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<TradingFeesParameters>(),
    TradingFeesFreshDataHandler