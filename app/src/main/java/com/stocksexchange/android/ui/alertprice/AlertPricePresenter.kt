package com.stocksexchange.android.ui.alertprice

import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.alertprice.AlertPriceModel.Companion.REQUEST_TYPE_ON_CLICK_PAIR
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.model.rest.CurrencyMarket

class AlertPricePresenter(
    view: AlertPriceContract.View,
    model: AlertPriceModel
) : BaseListDataLoadingPresenter<
    AlertPriceContract.View,
    AlertPriceModel,
    List<AlertPrice>,
    AlertPriceParameters
    >(view, model), AlertPriceContract.ActionListener, AlertPriceModel.ActionListener {


    private var mCurrentDeleteItem: AlertPrice? = null

    private var alertPriceParameters = AlertPriceParameters.getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        reloadData(DataLoadingTrigger.VIEW_SELECTION)
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when (trigger) {
            DataLoadingTrigger.START -> DataType.NEW_DATA
            DataLoadingTrigger.VIEW_SELECTION -> DataType.OLD_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: AlertPriceParameters): String {
        return mStringProvider.getPriceAlertEmptyCaption()
    }


    override fun getDataLoadingParams(): AlertPriceParameters {
        return alertPriceParameters
    }


    override fun onItemMoreDeleted(item: AlertPairItem) {
        item.moreAlertPrice?.let {
            mCurrentDeleteItem = it
            mModel.deleteItem(it.id)
        }
    }


    override fun onItemLessDeleted(item: AlertPairItem) {
        item.lessAlertPrice?.let {
            mCurrentDeleteItem = it
            mModel.deleteItem(it.id)
        }
    }


    override fun onItemClick(pairId: Int) {
        mModel.onClickItem(pairId)
    }


    override fun onRequestSent(requestType: Int) {
        when(requestType) {
            AlertPriceModel.REQUEST_TYPE_DELETE_ITEM -> {
                mView.showToolbarProgressBar()
            }
        }
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when (requestType) {
            REQUEST_TYPE_ON_CLICK_PAIR -> {
                mView.navigateToCurrencyMarketPreviewScreen(response as CurrencyMarket)
            }
        }
    }


    override fun onResponseReceived(requestType: Int) {
        when(requestType) {
            AlertPriceModel.REQUEST_TYPE_DELETE_ITEM -> {
                mView.hideToolbarProgressBar()
                mCurrentDeleteItem?.let {
                    mView.deleteItem(it)
                    mCurrentDeleteItem = null
                }
            }

        }
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            alertPriceParameters = it.alertPriceParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            alertPriceParameters = alertPriceParameters
        ))
    }


}