package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.OrderbookFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters

class OrderbookFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<OrderbookParameters>(),
    OrderbookFreshDataHandler