package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.api.model.rest.WalletMode
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel
import com.stocksexchange.android.ui.wallets.fragment.WalletsModel.ActionListener
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WalletsModel(
    private val walletsRepository: WalletsRepository
) : BaseDataLoadingModel<
    List<Wallet>,
    WalletParameters,
    ActionListener
    >() {


    override fun canLoadData(params: WalletParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val walletMode = params.mode
        val searchQuery = params.searchQuery

        val isWalletSearch = (walletMode == WalletMode.SEARCH)
        val isNewData = (dataType == DataType.NEW_DATA)

        val isWalletSearchWithNoQuery = (isWalletSearch && searchQuery.isBlank())
        val isWalletSearchNewData = (isWalletSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)

        return (!isWalletSearchWithNoQuery
                && !isWalletSearchNewData
                && (!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger))
    }


    override suspend fun refreshData(params: WalletParameters) {
        walletsRepository.refresh()
    }


    override suspend fun performDataLoading(params: WalletParameters) {
        when(params.mode) {
            WalletMode.STANDARD -> walletsRepository.getAll(params)
            WalletMode.SEARCH -> walletsRepository.search(params)
        }.log("walletsRepository.get(params: $params)")
        .onSuccess {
            withContext(Dispatchers.Main) {
                handleSuccessfulResponse(adjustWallets(wallets = it, params = params))
            }
        }
        .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    private fun adjustWallets(wallets: List<Wallet>,
                              params: WalletParameters): List<Wallet> {
        return wallets.filter {
            params.emptyWalletsFilter.isAcceptable(it)
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<List<Wallet>>


}