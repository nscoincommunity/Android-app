package com.stocksexchange.android.ui.orderbook

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import com.stocksexchange.android.ui.orderbook.OrderbookModel.ActionListener

class OrderbookModel(
    private val orderbooksRepository: OrderbooksRepository
) : BaseDataLoadingSimpleModel<
    Orderbook,
    OrderbookParameters,
    ActionListener
    >() {


    override fun canLoadData(params: OrderbookParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        return true
    }


    override suspend fun refreshData(params: OrderbookParameters) {
        orderbooksRepository.refresh(params)
    }


    override suspend fun getRepositoryResult(params: OrderbookParameters): RepositoryResult<Orderbook> {
        return orderbooksRepository.get(params).apply {
            log("orderbookRepository.get()")
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<Orderbook>


}