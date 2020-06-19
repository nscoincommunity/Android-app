package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.PriceAlertFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters

class PriceAlertFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<AlertPriceParameters>(),
    PriceAlertFreshDataHandler