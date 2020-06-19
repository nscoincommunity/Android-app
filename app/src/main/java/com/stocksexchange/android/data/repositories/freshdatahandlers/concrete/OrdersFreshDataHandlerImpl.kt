package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.OrdersFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.OrderParameters

class OrdersFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<OrderParameters>(),
    OrdersFreshDataHandler