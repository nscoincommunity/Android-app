package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.CandleSticksFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters

class CandleSticksFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<PriceChartDataParameters>(),
    CandleSticksFreshDataHandler {


    override fun refresh(params: PriceChartDataParameters) {
        val currencyPairId = params.currencyPairId.toString()

        // Converting to list to prevent concurrent modification exception
        val keys = mShouldLoadFreshDataMap.keys.toList()

        for(key in keys) {
            if(key.startsWith(currencyPairId)) {
                mShouldLoadFreshDataMap.remove(key)
            }
        }
    }


}